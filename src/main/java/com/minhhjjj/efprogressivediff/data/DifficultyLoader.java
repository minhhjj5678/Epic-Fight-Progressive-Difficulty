package com.minhhjjj.efprogressivediff.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID, bus =  Mod.EventBusSubscriber.Bus.FORGE)
public class DifficultyLoader extends SimpleJsonResourceReloadListener {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String FOLDER = "mechanics";
    private static final Gson GSON = new Gson();
    public static final Map<ResourceLocation, DifficultyModifier> DIFFICULTY_MODIFIER = new HashMap<>();

    public DifficultyLoader() {
        super(GSON, FOLDER);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        DIFFICULTY_MODIFIER.clear();

        jsonMap.forEach((key, jsonElement) -> {
            try {
                DifficultyModifier mod = GSON.fromJson(jsonElement, DifficultyModifier.class);
                DIFFICULTY_MODIFIER.put(key, mod);
            } catch (Exception e) {
                LOGGER.error("Failed to parse difficulty modifier from {}", key, e);
            }
        });
        LOGGER.info("Loaded {} difficulty modifiers", DIFFICULTY_MODIFIER.size());
    }

    @SubscribeEvent
    public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new DifficultyLoader());
    }
}
