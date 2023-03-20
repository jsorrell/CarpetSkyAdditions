package com.jsorrell.carpetskyadditions.gen.feature;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class EndGatewayIslandFeature extends Feature<DefaultFeatureConfig> {
    public EndGatewayIslandFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        if (!Feature.END_ISLAND.generate(context)) {
            return false;
        }

        StructureWorldAccess world = context.getWorld();

        int x = context.getOrigin().getX();
        int y = context.getOrigin().getY();
        int z = context.getOrigin().getZ();

        // Try to generate in a 11x11 area around the center of the island.
        // 20 tries should be more than enough, even for small islands.
        final int r = 5;
        for (BlockPos pos : BlockPos.iterateRandomly(context.getRandom(), 20, x - r, y, z - r, x + r, y, z + r)) {
            // Force not generating on edge
            if (Direction.Type.HORIZONTAL.stream().noneMatch(dir -> world.isAir(pos.offset(dir)))
                    && Feature.CHORUS_PLANT.generate(new FeatureContext<>(
                            Optional.empty(),
                            world,
                            context.getGenerator(),
                            context.getRandom(),
                            pos.up(),
                            FeatureConfig.DEFAULT))) {
                return true;
            }
        }
        return false;
    }

    // Finds a place to spawn a gateway that won't overwrite chorus
    // Allows a gateway that pops off chorus flowers
    public static BlockPos findGatewayLocation(WorldView world, BlockPos origin) {
        return BlockPos.streamOutwards(origin, 7, 0, 7)
                .filter(pos -> world.getBlockState(pos).isOf(Blocks.END_STONE)
                        && Direction.stream()
                                .allMatch(direction -> world.isAir(pos.up(11).offset(direction)))
                        && Direction.stream()
                                .allMatch(direction -> world.isAir(pos.up(9).offset(direction))))
                .findFirst()
                .orElse(origin);
    }
}
