package com.fireblaze.stamina_mod.entity;

import com.fireblaze.stamina_mod.capability.StaminaProvider;
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
import com.fireblaze.stamina_mod.config.Settings;

public class SeatEntity extends Entity {

    private Player playerToMove = null;
    private int moveDelay = -1; // -1 = kein Move geplant

    public SeatEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {

            // Falls ein Move geplant ist
            if (moveDelay > 0) {
                moveDelay--;
            } else if (moveDelay == 0 && playerToMove != null) {

                // Position des Blocks unter der Sitz-Entity
                BlockPos posBelow = this.blockPosition();
                BlockState state = this.level().getBlockState(posBelow);
                VoxelShape shape = state.getCollisionShape(this.level(), posBelow);

                // Standardhöhe 1.0 falls Shape leer ist (z.B. Luftblock)
                double height = shape.isEmpty() ? 1.0 : shape.max(net.minecraft.core.Direction.Axis.Y);

                // Spieler auf die korrekte Höhe setzen
                playerToMove.setPos(
                        playerToMove.getX(),
                        posBelow.getY() + height,
                        playerToMove.getZ()
                );

                playerToMove = null;
                moveDelay = -1;

                // Sitz jetzt löschen
                this.discard();
            }

            // Sitz löschen, wenn leer und kein Move geplant ist
            if (this.getPassengers().isEmpty() && moveDelay == -1) {
                this.discard();
            }
            else {
                if (this.getFirstPassenger() == null) return;
                Player player = (Player) this.getFirstPassenger();

                BlockPos posBelow = this.blockPosition();
                BlockState state = this.level().getBlockState(posBelow);
                float hardness = state.getDestroySpeed(this.level(), posBelow);

                player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                    float hardnessFactor = hardness > 0 ? 1.0f / hardness : 1.0f;

                    // Radius um die Sitz-Position prüfen
                    boolean campfireNearby = false;
                    BlockPos pos = this.blockPosition();
                    int radius = 4;

                    // Prüfe X und Z in einem Radius von 4, Y nur +-1 vom Sitzblock
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = 0; y <= 2; y++) {  // nur ein Block unterhalb bis ein Block oberhalb
                            for (int z = -radius; z <= radius; z++) {
                                BlockPos checkPos = pos.offset(x, y, z);
                                if (this.level().getBlockState(checkPos).getBlock() instanceof net.minecraft.world.level.block.CampfireBlock) {
                                    campfireNearby = true;
                                    break;  // springt aus der Z-Schleife
                                }
                            }
                            if (campfireNearby) break; // springt aus der Y-Schleife
                        }
                        if (campfireNearby) break; // springt aus der X-Schleife
                    }

                    if (campfireNearby) {
                        stamina.rest((float) (Settings.getRegenerationConfigs("sitBonus") + Settings.getRegenerationConfigs("campfireBonus")), hardnessFactor); // Beispiel: Bonus-Regeneration pro Tick
                    } else {
                        stamina.rest((float) Settings.getRegenerationConfigs("sitBonus"), hardnessFactor); // Normal-Regeneration
                    }
                });
            }
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
