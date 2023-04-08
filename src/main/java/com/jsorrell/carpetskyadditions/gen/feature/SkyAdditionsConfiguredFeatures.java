package com.jsorrell.carpetskyadditions.gen.feature;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public abstract class SkyAdditionsConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPAWN_PLATFORM = feature("spawn_platform");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SKY_ISLAND = feature("sky_island");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GATEWAY_ISLAND = feature("end_gateway_island");

    private static ResourceKey<ConfiguredFeature<?, ?>> feature(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new SkyAdditionsResourceLocation(path));
    }
}
