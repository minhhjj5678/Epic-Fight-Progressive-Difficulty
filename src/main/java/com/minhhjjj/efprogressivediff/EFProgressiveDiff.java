package com.minhhjjj.efprogressivediff;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import javax.annotation.Nonnull;

import com.minhhjjj.efprogressivediff.attachment.PlayerDataAttachment;
import com.minhhjjj.efprogressivediff.command.DifficultyCommand;
import com.minhhjjj.efprogressivediff.config.PDClientConfig;
import com.minhhjjj.efprogressivediff.config.PDConfig;

@Mod(EFProgressiveDiff.MODID)
public class EFProgressiveDiff
{
    public static final String MODID = "efprogressivediff";

	public EFProgressiveDiff(@Nonnull IEventBus modEventBus, ModContainer modContainer) {
        PlayerDataAttachment.ATTACHMENT_TYPES.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, PDConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, PDClientConfig.SPEC);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
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

    @SuppressWarnings("removal")
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
           
        }

        
    }
}