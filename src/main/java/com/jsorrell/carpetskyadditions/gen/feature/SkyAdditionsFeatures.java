package com.jsorrell.carpetskyadditions.gen.feature;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public abstract class SkyAdditionsFeatures {
    public static final Feature<LocatableStructureFeatureConfig> LOCATABLE_STRUCTURE =
            new LocatableStructureFeature(LocatableStructureFeatureConfig.CODEC);
    public static final Feature<SpawnPlatformFeatureConfig> SPAWN_PLATFORM =
            new SpawnPlatformFeature(SpawnPlatformFeatureConfig.CODEC);
    public static final Feature<NoneFeatureConfiguration> GATEWAY_ISLAND =
            new EndGatewayIslandFeature(NoneFeatureConfiguration.CODEC);

    public static void registerAll() {
        Registry.register(
                BuiltInRegistries.FEATURE, new SkyAdditionsIdentifier("locatable_structure"), LOCATABLE_STRUCTURE);
        Registry.register(BuiltInRegistries.FEATURE, new SkyAdditionsIdentifier("spawn_platform"), SPAWN_PLATFORM);
        Registry.register(BuiltInRegistries.FEATURE, new SkyAdditionsIdentifier("end_gateway_island"), GATEWAY_ISLAND);
    }
}
