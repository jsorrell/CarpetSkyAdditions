package com.jsorrell.carpetskyadditions.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SpawnPlatformFeatureConfig(LocatableStructureFeatureConfig platformConfig, boolean spawn_relative)
        implements FeatureConfiguration {
    public static final Codec<SpawnPlatformFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    LocatableStructureFeatureConfig.CODEC
                            .fieldOf("platform")
                            .forGetter(config -> config.platformConfig),
                    Codec.BOOL.fieldOf("spawn_relative").forGetter(config -> config.spawn_relative))
            .apply(instance, SpawnPlatformFeatureConfig::new));
}
