package com.minhhjjj.efprogressivediff;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;
import com.minhhjjj.efprogressivediff.config.PDConfig;

@Mod(EFProgressiveDiff.MODID)
public class EFProgressiveDiff
{
    public static final String MODID = "efprogressivediff";

    public EFProgressiveDiff(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        context.registerConfig(ModConfig.Type.COMMON, PDConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
       
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
       
    }

    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerDataCapability.class);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
           
        }
    }
}
