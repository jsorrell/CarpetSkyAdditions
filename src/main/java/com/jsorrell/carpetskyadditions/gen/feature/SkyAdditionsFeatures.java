package com.jsorrell.carpetskyadditions.gen.feature;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public abstract class SkyAdditionsFeatures {
    public static final Feature<LocatableStructureFeatureConfiguration> LOCATABLE_STRUCTURE = register(
            "locatable_structure", new LocatableStructureFeature(LocatableStructureFeatureConfiguration.CODEC));
    public static final Feature<SpawnPlatformFeatureConfiguration> SPAWN_PLATFORM =
            register("spawn_platform", new SpawnPlatformFeature(SpawnPlatformFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> GATEWAY_ISLAND =
            register("end_gateway_island", new EndGatewayIslandFeature(NoneFeatureConfiguration.CODEC));

    private static <T extends Feature<?>> T register(String name, T feature) {
        return Registry.register(BuiltInRegistries.FEATURE, new SkyAdditionsResourceLocation(name), feature);
    }

    public static void bootstrap() {}
}
