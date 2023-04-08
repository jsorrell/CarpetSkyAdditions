package com.jsorrell.carpetskyadditions.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpawnPlatformFeature extends Feature<SpawnPlatformFeatureConfiguration> {
    public SpawnPlatformFeature(Codec<SpawnPlatformFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SpawnPlatformFeatureConfiguration> context) {
        SpawnPlatformFeatureConfiguration config = context.config();
        // Always absolute with Y
        BlockPos origin = config.spawnRelative() ? context.origin().atY(0) : BlockPos.ZERO;

        return SkyAdditionsFeatures.LOCATABLE_STRUCTURE.place(
                config.platformConfig(), context.level(), context.chunkGenerator(), context.random(), origin);
    }
}
