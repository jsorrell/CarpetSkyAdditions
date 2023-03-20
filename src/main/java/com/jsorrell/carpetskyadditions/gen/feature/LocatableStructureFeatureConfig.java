package com.jsorrell.carpetskyadditions.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.FeatureConfig;

public record LocatableStructureFeatureConfig(Identifier structure, BlockPos pos) implements FeatureConfig {
    public static final Codec<LocatableStructureFeatureConfig> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                            Identifier.CODEC.fieldOf("structure").forGetter(config -> config.structure),
                            BlockPos.CODEC.fieldOf("pos").forGetter(config -> config.pos))
                    .apply(instance, LocatableStructureFeatureConfig::new));
}
