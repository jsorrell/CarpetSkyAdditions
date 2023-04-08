package com.jsorrell.carpetskyadditions.gen.feature;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public abstract class SkyAdditionsFeatures {
    public static final Feature<LocatableStructureFeatureConfiguration> LOCATABLE_STRUCTURE =
            new LocatableStructureFeature(LocatableStructureFeatureConfiguration.CODEC);
    public static final Feature<SpawnPlatformFeatureConfiguration> SPAWN_PLATFORM =
            new SpawnPlatformFeature(SpawnPlatformFeatureConfiguration.CODEC);
    public static final Feature<NoneFeatureConfiguration> GATEWAY_ISLAND =
            new EndGatewayIslandFeature(NoneFeatureConfiguration.CODEC);

    public static void registerAll() {
        Registry.register(
                BuiltInRegistries.FEATURE,
                new SkyAdditionsResourceLocation("locatable_structure"),
                LOCATABLE_STRUCTURE);
        Registry.register(
                BuiltInRegistries.FEATURE, new SkyAdditionsResourceLocation("spawn_platform"), SPAWN_PLATFORM);
        Registry.register(
                BuiltInRegistries.FEATURE, new SkyAdditionsResourceLocation("end_gateway_island"), GATEWAY_ISLAND);
    }
}
