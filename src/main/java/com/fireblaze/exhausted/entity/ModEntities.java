package com.fireblaze.exhausted.entity;

import com.fireblaze.exhausted.Exhausted;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Exhausted.MODID);

    /*
    public static final RegistryObject<EntityType<SeatEntity>> SEAT = ENTITIES.register(
            "seat",
            () -> EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                    .sized(0.001f, 0.001f) // Winzig und unsichtbar
                    .build(StaminaMod.MODID + ":seat")
    );*/

    public static final RegistryObject<EntityType<SeatEntity>> SEAT =
            ENTITIES.register("seat", () ->
                    EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                            .sized(0.001f, 0.001f) // praktisch unsichtbar
                            .clientTrackingRange(64)   // damit Mount/Despawn sauber ankommen
                            .updateInterval(1)

                            .build(ResourceLocation.fromNamespaceAndPath(Exhausted.MODID, "seat").toString())
            );
}
