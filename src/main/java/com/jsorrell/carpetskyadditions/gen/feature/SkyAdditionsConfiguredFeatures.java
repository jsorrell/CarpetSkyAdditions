package com.jsorrell.carpetskyadditions.gen.feature;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public abstract class SkyAdditionsConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> SPAWN_PLATFORM = feature("spawn_platform");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SKY_ISLAND = feature("sky_island");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GATEWAY_ISLAND = feature("end_gateway_island");

    private static RegistryKey<ConfiguredFeature<?, ?>> feature(String path) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new SkyAdditionsIdentifier(path));
    }
}
