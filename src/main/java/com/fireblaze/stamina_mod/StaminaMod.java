package com.fireblaze.stamina_mod;

import com.fireblaze.stamina_mod.config.StaminaConfig;
import com.fireblaze.stamina_mod.networking.ModMessages;
import com.fireblaze.stamina_mod.entity.ModEntities;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import com.fireblaze.stamina_mod.capability.StaminaProvider;
import com.fireblaze.stamina_mod.events.PlayerTickHandler;
import com.fireblaze.stamina_mod.events.PlayerSleepHandler;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(StaminaMod.MODID)
public class StaminaMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "stamina_mod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public StaminaMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Registries
        ModEntities.ENTITIES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(PlayerTickHandler.class);
        PlayerSleepHandler.register();
        MinecraftForge.EVENT_BUS.register(new ModEvents());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StaminaConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            // TODO: Später Setup-Aufgaben
        });

        ModMessages.register();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        // TODO: Creative Tab Einträge
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Server startet...");
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        LOGGER.info("{} ist dem Server beigetreten", player.getName().getString());
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Client-Setup (Render, Keybinds)
        }
    }
}
