package com.minhhjjj.efprogressivediff.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
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
public class DataListener extends SimpleJsonResourceReloadListener {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String FOLDER = "mechanics";
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, (JsonDeserializer<ResourceLocation>) (json, type, context) -> ResourceLocation.tryParse(json.getAsString()))
            .create();
    public static final Map<ResourceLocation, DifficultyModifier> DIFFICULTY_MODIFIER = new HashMap<>();
    public static final Map<ResourceLocation, MultiplierModifier> MULTIPLIER_MODIFIER = new HashMap<>();
    public static final Map<ResourceLocation, AttributeModifier> ATTRIBUTE_MODIFIER = new HashMap<>();

    public DataListener() {
        super(GSON, FOLDER);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        DIFFICULTY_MODIFIER.clear();
        MULTIPLIER_MODIFIER.clear();
        ATTRIBUTE_MODIFIER.clear();

        jsonMap.forEach((key, jsonElement) -> {
            try {
                if (key.equals(ResourceLocation.fromNamespaceAndPath(EFProgressiveDiff.MODID, DifficultyModifier.FILE))) {
                    DifficultyModifier mod = GSON.fromJson(jsonElement, DifficultyModifier.class);
                    DIFFICULTY_MODIFIER.put(key, mod);
                } else if (key.equals(ResourceLocation.fromNamespaceAndPath(EFProgressiveDiff.MODID, MultiplierModifier.FILE))) {
                    MultiplierModifier mod = GSON.fromJson(jsonElement, MultiplierModifier.class);
                    MULTIPLIER_MODIFIER.put(key, mod);
                } else {
                    AttributeModifier mod = GSON.fromJson(jsonElement, AttributeModifier.class);
                    ATTRIBUTE_MODIFIER.put(key, mod);
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to parse difficulty modifier from {}", key, e);
            }
        });
        LOGGER.info("Loaded total {} modifiers", DIFFICULTY_MODIFIER.size() + MULTIPLIER_MODIFIER.size() +  ATTRIBUTE_MODIFIER.size());
    }

    @SubscribeEvent
    public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new DataListener());
    }
}
