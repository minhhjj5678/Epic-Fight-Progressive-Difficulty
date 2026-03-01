package com.minhhjjj.efprogressivediff;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;
import com.minhhjjj.efprogressivediff.command.DifficultyCommand;
import com.minhhjjj.efprogressivediff.config.PDClientConfig;
import com.minhhjjj.efprogressivediff.config.PDConfig;
import com.minhhjjj.efprogressivediff.network.PacketHandler;

@Mod(EFProgressiveDiff.MODID)
public class EFProgressiveDiff
{
    public static final String MODID = "efprogressivediff";

    @SuppressWarnings("removal")
	public EFProgressiveDiff(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::onRegisterCapabilities);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PDConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PDClientConfig.SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(PacketHandler::init);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        DifficultyCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
       
    }

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
