package com.fireblaze.exhausted.entity;

import com.fireblaze.exhausted.capability.StaminaProvider;
import com.fireblaze.exhausted.comfort.ComfortProvider;
import com.fireblaze.exhausted.events.PlayerTickHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import com.fireblaze.exhausted.config.Settings;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SeatEntity extends Entity {

    private Player playerToMove = null;
    private int moveDelay = -1; // -1 = kein Move geplant
    private static final Map<UUID, Integer> tickCounter = new HashMap<>();

    public SeatEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        // Falls ein Move geplant ist (Spieler nach Tick auf Sitzposition setzen)
        if (moveDelay > 0) {
            moveDelay--;
        } else if (moveDelay == 0 && playerToMove != null) {
            BlockPos posBelow = this.blockPosition();
            BlockState state = this.level().getBlockState(posBelow);
            VoxelShape shape = state.getCollisionShape(this.level(), posBelow);
            double height = shape.isEmpty() ? 1.0 : shape.max(net.minecraft.core.Direction.Axis.Y);

            playerToMove.setPos(playerToMove.getX(), posBelow.getY() + height, playerToMove.getZ());
            playerToMove = null;
            moveDelay = -1;

            // Sitz löschen
            this.discard();
        }

        // Sitz löschen, wenn leer und kein Move geplant
        if (this.getPassengers().isEmpty() && moveDelay == -1) {
            this.discard();
            return;
        }

        // Tick-Logik für Sitzbonus / Regeneration
        if (this.getFirstPassenger() != null) {
            Player player = (Player) this.getFirstPassenger();
            UUID id = player.getUUID();

            // Tick-Counter hochzählen
            int counter = tickCounter.getOrDefault(id, 0) + 1;
            tickCounter.put(id, counter);

            // Nur alle 5 Ticks ausführen
            if (counter < PlayerTickHandler.tickMultiplier) return;
            tickCounter.put(id, 0); // reset

            BlockPos posBelow = this.blockPosition();
            BlockState state = this.level().getBlockState(posBelow);
            float hardness = state.getDestroySpeed(this.level(), posBelow);
            hardness = Math.max(0.5f, hardness);

            float hardnessFactor = hardness > 0 ? 1.0f / hardness : 1.0f;

            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                player.getCapability(ComfortProvider.COMFORT_CAP).ifPresent(cap -> {
                    double comfort = cap.getComfortLevel(); // nur abrufen, nicht neu berechnen
                    stamina.rest(player, (float) (comfort * Settings.getRegenerationConfigs("comfortRegMultiplier") * PlayerTickHandler.tickMultiplier), (hardnessFactor / 1.5f));
                });
            });
        }
    }

    @Override
    protected void defineSynchedData() { }

    @Override
    protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) { }

    @Override
    protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) { }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);

        if (passenger instanceof Player player) {
            // Spieler merken, aber Bewegung erst im nächsten Tick ausführen
            this.playerToMove = player;
            this.moveDelay = 1;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
