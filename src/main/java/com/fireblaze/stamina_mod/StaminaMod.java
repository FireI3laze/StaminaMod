package com.fireblaze.stamina_mod;

import com.fireblaze.stamina_mod.capability.Stamina;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraft.resources.ResourceLocation;
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

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(PlayerTickHandler.class);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        PlayerSleepHandler.register();

        System.out.println("event busses registered");
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        // wounded management
        //float armDamage = getBodyPartDamage(player, "arm");
        float armDamage = 0;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            BlockState placedBlock = event.getPlacedBlock();

            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                stamina.consume(1.0f, 0.02f, armDamage);
                System.out.println("[Block placed] stamina:" + stamina.getShortStamina());
            });
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
