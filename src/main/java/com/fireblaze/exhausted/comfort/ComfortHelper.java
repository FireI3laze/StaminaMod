package com.fireblaze.exhausted.comfort;

import com.fireblaze.exhausted.config.Settings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class ComfortHelper {

    // Komfortblöcke (Glas, Blumentopf, Teppich, Crafting Table, Furnace, Chest, Tür)
    private static boolean isComfortBlock(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        String registryName = id.toString();

        if (Settings.getComfortBlocks().contains(registryName)) return true;

        for (String keyword : Settings.getComfortBlockGroups()) {
            if (registryName.contains(keyword)) return true;
        }

        if (Settings.isComfortStorageAllowed() && block instanceof EntityBlock entityBlock) {
            BlockEntity be = entityBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
            if (be instanceof Container) return true;
        }

        return false;
    }

    // Monster check basierend auf BlockPos
    public static boolean monstersNearby(ServerLevel level, BlockPos pos) {
        double range = Settings.getComfortSettings("monsterRangeTolerance");
        AABB box = new AABB(pos).inflate(range);
        return !level.getEntities((Entity) null, box, e -> e.getType().getCategory() == MobCategory.MONSTER).isEmpty();
    }

    // Check Deckenhöhe basierend auf BlockPos
    public static boolean hasCeilingHeight(ServerLevel level, BlockPos pos, int minHeight) {
        for (int y = 1; y <= minHeight; y++) {
            if (!level.isEmptyBlock(pos.above(y))) return false;
        }
        return true;
    }

    // Raumgröße (basierend auf BlockPos)
    public static boolean hasEnoughSpace(ServerLevel level, BlockPos pos, int minAirBlocks) {
        RoomScanResults result = lastRoomScansByPos.getOrDefault(level, Collections.emptyMap()).get(pos);
        if (result == null) result = scanRoom(level, pos);
        return result.airBlocks >= minAirBlocks;
    }

    // Temporäre Daten für Scan
    private static final Map<ServerLevel, Map<BlockPos, RoomScanResults>> lastRoomScansByPos = new HashMap<>();
    private static final Map<ServerLevel, Map<BlockPos, BlockState>> replacedBlocks = new HashMap<>();

    // Scan-Ergebnisobjekt
    public static class RoomScanResults {
        public int airBlocks;
        public int totalBlocks;
        public int badBlocks;
        public int goodBlocks;
        public boolean decorated;
        public float averageLight;
        public List<BlockPos> chestPositions = new ArrayList<>();
        public Set<BlockPos> visitedPositions;
    }

    public static RoomScanResults getLastScan(ServerLevel level, BlockPos pos) {
        return lastRoomScansByPos.getOrDefault(level, Collections.emptyMap()).get(pos);
    }

    // === Scan-Methode basierend auf BlockPos ===
    public static RoomScanResults scanRoom(ServerLevel level, BlockPos start) {
        Settings.loadComfortSettings();

        int lightSum = 0;
        int lightCount = 0;

        RoomScanResults result = new RoomScanResults();
        Set<BlockPos> badBlocksSet = new HashSet<>();
        Set<BlockPos> goodBlocksSet = new HashSet<>();
        int maxAirBlocks = 500;

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty() && result.airBlocks < maxAirBlocks) {
            BlockPos current = queue.poll();

            for (BlockPos neighbor : List.of(
                    current.north(), current.south(),
                    current.east(), current.west(),
                    current.above(), current.below()
            )) {
                if (visited.contains(neighbor)) continue;
                visited.add(neighbor);

                BlockState state = level.getBlockState(neighbor);

                lightSum += level.getBrightness(LightLayer.BLOCK, neighbor);
                lightCount++;

                if (state.isAir()) {
                    result.airBlocks++;
                    queue.add(neighbor);
                } else {
                    result.totalBlocks++;
                    ResourceLocation blockId = level.registryAccess()
                            .registryOrThrow(Registries.BLOCK)
                            .getKey(state.getBlock());
                    if (blockId != null && Settings.getWallBlocksBlacklist().contains(blockId.toString())) {
                        result.badBlocks++;
                        badBlocksSet.add(neighbor);
                    } else {
                        result.goodBlocks++;
                        goodBlocksSet.add(neighbor);
                    }
                }

                if (state.getBlock() instanceof ChestBlock) result.chestPositions.add(neighbor);
            }
        }

        result.visitedPositions = new HashSet<>(visited);
        result.averageLight = lightCount > 0 ? lightSum / (float) lightCount : 0;
        result.decorated = result.totalBlocks == 0 || ((float) result.badBlocks / result.totalBlocks) <= 0.5f;

        lastRoomScansByPos.computeIfAbsent(level, l -> new HashMap<>()).put(start, result);
        return result;
    }

    // Wrapper für Boolean
    public static boolean hasDecoratedWalls(ServerLevel level, BlockPos pos) {
        return scanRoom(level, pos).decorated;
    }

    public static boolean hasCeilingHeight(ServerLevel level, BlockPos pos) {
        for (int i = 1; i <= 3; i++) {
            if (level.getBlockState(pos.above(i)).isAir()) return false;
        }
        return true;
    }

    private static void highlightBlocks(ServerLevel level, Set<BlockPos> badBlocks, Set<BlockPos> goodBlocks) {
        replacedBlocks.putIfAbsent(level, new HashMap<>());
        Map<BlockPos, BlockState> levelReplacements = replacedBlocks.get(level);

        for (BlockPos pos : badBlocks) {
            levelReplacements.putIfAbsent(pos, level.getBlockState(pos));
            level.setBlock(pos, Blocks.RED_WOOL.defaultBlockState(), 3);
        }
        for (BlockPos pos : goodBlocks) {
            if (!(level.getBlockState(pos).getBlock() instanceof ChestBlock)) {
                levelReplacements.putIfAbsent(pos, level.getBlockState(pos));
                level.setBlock(pos, Blocks.GREEN_WOOL.defaultBlockState(), 3);
            }
        }
    }

    public static void resetHighlightedBlocks(ServerLevel level) {
        if (!replacedBlocks.containsKey(level)) return;

        Map<BlockPos, BlockState> levelReplacements = replacedBlocks.get(level);
        for (Map.Entry<BlockPos, BlockState> entry : levelReplacements.entrySet()) {
            level.setBlock(entry.getKey(), entry.getValue(), 3);
        }
        replacedBlocks.remove(level);
    }

    private static boolean isCaveBlock(ServerLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);
        return state.is(Blocks.STONE) || state.is(Blocks.DEEPSLATE) || state.is(Blocks.GRAVEL) ||
                state.is(Blocks.DIRT) || state.is(Blocks.TUFF) || state.is(Blocks.NETHERRACK) || state.is(Blocks.SOUL_SAND) ||
                state.is(Blocks.SOUL_SOIL) || state.is(Blocks.BASALT) || state.is(Blocks.BLACKSTONE) ||
                state.is(Blocks.END_STONE) || state.getBlock().getDescriptionId().toLowerCase().contains("ore");
    }

    public static boolean hasGoodLighting(ServerLevel level, BlockPos pos) {
        RoomScanResults result = getLastScan(level, pos);

        if (result != null && result.averageLight >= 3.5f) return true;

        while (pos.getY() < level.getMaxBuildHeight()) {
            BlockState state = level.getBlockState(pos);
            if (!state.isAir() && !state.is(Blocks.GLASS) && !state.is(BlockTags.LEAVES) && !state.is(Blocks.TORCH)) return false;
            pos = pos.above();
        }
        return true;
    }

    public static boolean chestWithFoodNearby(ServerLevel level, BlockPos pos) {
        RoomScanResults scan = getLastScan(level, pos);
        if (scan == null) return false;

        for (BlockPos p : scan.chestPositions) {
            BlockEntity be = level.getBlockEntity(p);
            if (be instanceof ChestBlockEntity chest) {
                for (int i = 0; i < chest.getContainerSize(); i++) {
                    ItemStack stack = chest.getItem(i);
                    if (stack.isEdible()) return true;
                }
            }
        }
        return false;
    }

    public static boolean hasFriendlyAnimalNearby(ServerLevel level, BlockPos pos) {
        RoomScanResults scan = getLastScan(level, pos);
        if (scan == null || scan.visitedPositions == null) return false;

        for (BlockPos p : scan.visitedPositions) {
            AABB box = new AABB(p);
            List<Entity> entities = level.getEntities((Entity) null, box, e ->
                    e.getType().getCategory() == MobCategory.CREATURE ||
                            e.getType() == EntityType.WOLF ||
                            e.getType() == EntityType.CAT
            );
            if (!entities.isEmpty()) return true;
        }
        return false;
    }

    public static double countComfortBlocks(ServerLevel level, BlockPos pos) {
        RoomScanResults scan = getLastScan(level, pos);
        if (scan == null || scan.visitedPositions == null) return 0;
        Settings.loadComfortSettings();

        double count = 0;
        Map<Block, Integer> seenBlocks = new HashMap<>();

        for (BlockPos p : scan.visitedPositions) {
            BlockState state = level.getBlockState(p);
            Block block = state.getBlock();
            if (isComfortBlock(block)) {
                int occurrences = seenBlocks.getOrDefault(block, 0);
                count += occurrences == 0 ? 1 : 0.5;
                seenBlocks.put(block, occurrences + 1);
            }
        }
        return count;
    }

    public static boolean hasBoostBlock(ServerLevel level, BlockPos pos) {
        RoomScanResults scan = getLastScan(level, pos);
        if (scan == null || scan.visitedPositions == null) return false;

        String dim = level.dimension().location().toString();
        Map<String, String> boostMap = Settings.getDimensionBoostBlocks();
        String blockId = boostMap.get(dim);
        if (blockId == null) return false;

        Block targetBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockId));
        if (targetBlock == null) return false;

        for (BlockPos p : scan.visitedPositions) {
            if (level.getBlockState(p).is(targetBlock)) return true;
        }
        return false;
    }
}
