package com.envygames.dimensionalpha.data;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.envygames.dimensionalpha.worldgen.ConfigGate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public final class ModCompatBiomeModifierProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public ModCompatBiomeModifierProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "neoforge/biome_modifier");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        JsonObject json = new JsonObject();

        JsonArray conditions = new JsonArray();
        JsonObject modLoaded = new JsonObject();
        modLoaded.addProperty("type", "neoforge:mod_loaded");
        modLoaded.addProperty("modid", "egores");
        conditions.add(modLoaded);
        json.add("neoforge:conditions", conditions);

        json.addProperty("type", DimensionalphaMod.id("configurable_add_features").toString());
        json.addProperty("biomes", DimensionalphaMod.id("eg_surface").toString());
        json.addProperty("step", "underground_ores");
        json.addProperty("config_gate", ConfigGate.DIMENSION_ALPHA_EXTERNAL_RESOURCES.getSerializedName());

        JsonArray features = new JsonArray();
        features.add("egores:silver_ore");
        json.add("features", features);

        Path path = pathProvider.json(DimensionalphaMod.id("egores_compat"));
        return DataProvider.saveStable(output, json, path);
    }

    @Override
    public String getName() {
        return "Dimension Alpha compat biome modifiers";
    }
}
