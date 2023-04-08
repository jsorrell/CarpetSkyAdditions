package com.jsorrell.carpetskyadditions.gen.feature;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EndGatewayIslandFeature extends Feature<NoneFeatureConfiguration> {
    public EndGatewayIslandFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!Feature.END_ISLAND.place(context)) {
            return false;
        }

        WorldGenLevel level = context.level();

        int x = context.origin().getX();
        int y = context.origin().getY();
        int z = context.origin().getZ();

        // Try to generate in a 11x11 area around the center of the island.
        // 20 tries should be more than enough, even for small islands.
        final int r = 5;
        for (BlockPos pos : BlockPos.randomBetweenClosed(context.random(), 20, x - r, y, z - r, x + r, y, z + r)) {
            // Force not generating on edge
            if (Direction.Plane.HORIZONTAL.stream().noneMatch(dir -> level.isEmptyBlock(pos.relative(dir)))
                    && Feature.CHORUS_PLANT.place(new FeaturePlaceContext<>(
                            Optional.empty(),
                            level,
                            context.chunkGenerator(),
                            context.random(),
                            pos.above(),
                            FeatureConfiguration.NONE))) {
                return true;
            }
        }
        return false;
    }

    // Finds a place to spawn a gateway that won't overwrite chorus
    // Allows a gateway that pops off chorus flowers
    public static BlockPos findGatewayLocation(LevelReader level, BlockPos origin) {
        return BlockPos.withinManhattanStream(origin, 7, 0, 7)
                .filter(pos -> level.getBlockState(pos).is(Blocks.END_STONE)
                        && Direction.stream()
                                .allMatch(direction ->
                                        level.isEmptyBlock(pos.above(11).relative(direction)))
                        && Direction.stream()
                                .allMatch(direction ->
                                        level.isEmptyBlock(pos.above(9).relative(direction))))
                .findFirst()
                .orElse(origin);
    }
}
