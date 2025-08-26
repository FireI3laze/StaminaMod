package com.fireblaze.exhausted.comfort;

import com.fireblaze.exhausted.config.Settings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;


import java.util.*;

public class ComfortHelper {

    // Komfortblöcke (Glas, Blumentopf, Teppich, Crafting Table, Furnace, Chest, Tür)
    private static boolean isComfortBlock(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);

        String registryName = id.toString(); // z.B. "minecraft:oak_door" oder "mod:magic_chest"

        // Check Blocks
        if (Settings.getComfortBlocks().contains(registryName)) return true;

        // Check Groups
        for (String keyword : Settings.getComfortBlockGroups()) {
            if (registryName.contains(keyword)) return true;
        }

        // Storage Block Toggle
        if (Settings.isComfortStorageAllowed() && block instanceof EntityBlock entityBlock) {
            BlockEntity be = entityBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
            if (be instanceof Container) return true;
        }

        return false;
    }


    // Monster check
    public static boolean monstersNearby(ServerLevel level, Player player) {
        return (!level.getEntities(player, player.getBoundingBox().inflate(Settings.getComfortSettings("monsterRangeTolerance")),
                e -> e.getType().getCategory() == MobCategory.MONSTER).isEmpty());
    }

    // Check Deckenhöhe
    public static boolean hasCeilingHeight(ServerLevel level, Player player, int minHeight) {
        BlockPos pos = player.blockPosition();
        for (int y = 1; y <= minHeight; y++) {
            if (level.isEmptyBlock(pos.above(y))) continue;
            return false;
        }
        return true;
    }

    // Raumgröße (sehr basic, Floodfill wäre genauer)
    public static boolean hasEnoughSpace(ServerLevel level, Player player, int minAirBlocks) {
        RoomScanResults result = lastRoomScans.get(player);
        if (result == null) {
            // Falls noch kein Scan gemacht wurde → einmalig scannen
            result = scanRoom(level, player);
        }

        return result.airBlocks >= minAirBlocks;
    }

    // Wände analysieren (sehr basic: check um Spieler herum)

    // Speichert temporär ersetzte Blöcke für Reset
    private static final Map<ServerLevel, Map<BlockPos, BlockState>> replacedBlocks = new HashMap<>();
    private static final Map<Player, RoomScanResults> lastRoomScans = new HashMap<>();

    // === Ergebnisobjekt für Floodfill ===
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

    public static RoomScanResults getLastScan(Player player) {
        return lastRoomScans.get(player);
    }

    // === Scan-Methode ===
    public static RoomScanResults scanRoom(ServerLevel level, Player player) {
        int lightSum = 0;
        int lightCount = 0;

        RoomScanResults result = new RoomScanResults();
        Set<BlockPos> badBlocksSet = new HashSet<>();
        Set<BlockPos> goodBlocksSet = new HashSet<>();
        int maxAirBlocks = 500;

        BlockPos start = player.blockPosition();
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
                    lightSum += level.getBrightness(LightLayer.BLOCK, neighbor);
                    lightCount++;
                } else {
                    result.totalBlocks++;
                    if (isCaveBlock(level, neighbor)) {
                        result.badBlocks++;
                        badBlocksSet.add(neighbor);
                    } else {
                        result.goodBlocks++;
                        goodBlocksSet.add(neighbor);
                    }
                    lightSum += level.getBrightness(LightLayer.BLOCK, neighbor);
                    lightCount++;
                }

                if (result.airBlocks >= maxAirBlocks) break;

                if (state.getBlock() instanceof ChestBlock) {
                    result.chestPositions.add(neighbor);
                }
            }
        }

        if(lightCount > 0){
            result.averageLight = lightSum / (float) lightCount;
        } else {
            result.averageLight = 0;
        }

        result.visitedPositions = new HashSet<>(visited);


        // Blöcke hervorheben
        // highlightBlocks(level, badBlocksSet, goodBlocksSet);
        System.out.println(result.badBlocks + " | " + (result.totalBlocks - result.badBlocks));
        result.decorated = result.totalBlocks == 0 || ((float) result.badBlocks / result.totalBlocks) <= 0.5f;

        // Scan-Ergebnis speichern
        lastRoomScans.put(player, result);

        return result;
    }

    // === Wrapper für direkten Boolean ===
    public static boolean hasDecoratedWalls(ServerLevel level, Player player) {
        return scanRoom(level, player).decorated;
    }

    // === Ceiling-Check (min. 3 Blöcke über Spielerhöhe) ===
    public static boolean hasCeilingHeight(ServerLevel level, Player player) {
        BlockPos pos = player.blockPosition();
        for (int i = 1; i <= 3; i++) {
            if (level.getBlockState(pos.above(i)).isAir()) {
                return false;
            }
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

    // Rücksetzen aller ersetzten Blöcke
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


    // Lichtlevel Durchschnitt prüfen
    public static boolean hasGoodLighting(Player player) {
        RoomScanResults result = lastRoomScans.get(player);

        // 1. Lichtlevel prüfen (für Nacht / Höhlen)
        if (result != null && result.averageLight >= 3.5f) {
            return true;
        }

        Level level = player.level();
        BlockPos pos = player.blockPosition().above();

        // 2. Tagsüber prüfen: freier Himmel oder nur transparente Blöcke
        if (level.isDay()) {
            while (pos.getY() < level.getMaxBuildHeight()) {
                BlockState state = level.getBlockState(pos);

                // Undurchsichtige Blöcke abbrechen
                if (!state.isAir() && !state.is(Blocks.GLASS) && !state.is(BlockTags.LEAVES) && !state.is(Blocks.TORCH)) {
                    return false;
                }

                pos = pos.above();
            }

            // Nur Luft/Transparente Blöcke bis zum freien Himmel
            return true;
        }

        // Nacht und kein ausreichendes Licht
        return false;
    }



    // Essen in Kiste?
    public static boolean chestWithFoodNearby(ServerLevel level, Player player) {
        RoomScanResults scan = lastRoomScans.get(player);
        if (scan == null) return false;

        for (BlockPos pos : scan.chestPositions) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ChestBlockEntity chest) {
                for (int i = 0; i < chest.getContainerSize(); i++) {
                    ItemStack stack = chest.getItem(i);
                    if (stack.isEdible()) return true;
                }
            }
        }
        return false;
    }



    // Tier in der Nähe
    public static boolean hasFriendlyAnimalNearby(ServerLevel level, Player player) {
        RoomScanResults scan = lastRoomScans.get(player);
        if (scan == null || scan.visitedPositions == null) return false;

        for (BlockPos pos : scan.visitedPositions) {
            AABB box = new AABB(pos);
            List<Entity> entities = level.getEntities((Entity) null, box, e ->
                    e.getType().getCategory() == MobCategory.CREATURE
                            || e.getType() == EntityType.WOLF
                            || e.getType() == EntityType.CAT
            );

            if (!entities.isEmpty()) {
                return true;
            }
        }

        return false;
    }


    // Komfortblöcke zählen
    public static int countComfortBlocks(ServerLevel level, Player player) {
        RoomScanResults scan = lastRoomScans.get(player);
        if (scan == null || scan.visitedPositions == null) return 0;
        Settings.loadComfortSettings();

        int count = 0;
        for (BlockPos pos : scan.visitedPositions) {
            BlockState state = level.getBlockState(pos);
            if (isComfortBlock(state.getBlock())) {
                count++;
            }
        }

        return count;
    }


    public static boolean hasBoostBlock(ServerLevel level, Player player) {
        RoomScanResults scan = lastRoomScans.get(player);
        if (scan == null || scan.visitedPositions == null) return false;

        // Dimension bestimmen
        String dim = level.dimension().location().toString();
        Map<String, String> boostMap = Settings.getDimensionBoostBlocks();

        // Für diese Dimension den gewünschten Boost-Block holen
        String blockId = boostMap.get(dim);
        if (blockId == null) return false;

        Block targetBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockId));
        if (targetBlock == null) return false;

        for (BlockPos pos : scan.visitedPositions) {
            BlockState state = level.getBlockState(pos);

            if (state.is(targetBlock)) {
                System.out.println("Boost block found: " + blockId);
                return true;
            }
        }

        return false;
    }

}
