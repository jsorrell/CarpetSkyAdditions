package com.jsorrell.carpetskyadditions.gen.feature;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public abstract class SkyAdditionsFeatures {
    public static final Feature<LocatableStructureFeatureConfig> LOCATABLE_STRUCTURE =
            new LocatableStructureFeature(LocatableStructureFeatureConfig.CODEC);
    public static final Feature<SpawnPlatformFeatureConfig> SPAWN_PLATFORM =
            new SpawnPlatformFeature(SpawnPlatformFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> GATEWAY_ISLAND =
            new EndGatewayIslandFeature(DefaultFeatureConfig.CODEC);

    public static void registerAll() {
        Registry.register(Registries.FEATURE, new SkyAdditionsIdentifier("locatable_structure"), LOCATABLE_STRUCTURE);
        Registry.register(Registries.FEATURE, new SkyAdditionsIdentifier("spawn_platform"), SPAWN_PLATFORM);
        Registry.register(Registries.FEATURE, new SkyAdditionsIdentifier("end_gateway_island"), GATEWAY_ISLAND);
    }
}
