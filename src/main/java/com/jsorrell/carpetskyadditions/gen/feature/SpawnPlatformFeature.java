package com.jsorrell.carpetskyadditions.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SpawnPlatformFeature extends Feature<SpawnPlatformFeatureConfig> {
    public SpawnPlatformFeature(Codec<SpawnPlatformFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<SpawnPlatformFeatureConfig> context) {
        SpawnPlatformFeatureConfig config = context.getConfig();
        // Always absolute with Y
        BlockPos origin = config.spawn_relative() ? context.getOrigin().withY(0) : BlockPos.ORIGIN;

        return SkyAdditionsFeatures.LOCATABLE_STRUCTURE.generateIfValid(
                config.platformConfig(), context.getWorld(), context.getGenerator(), context.getRandom(), origin);
    }
}
