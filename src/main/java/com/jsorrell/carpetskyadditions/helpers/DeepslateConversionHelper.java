package com.jsorrell.carpetskyadditions.helpers;

import java.util.Optional;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class DeepslateConversionHelper {
    public static final Potion CONVERSION_POTION = Potions.THICK;

    public static Optional<BlockState> canConvert(BlockState from) {
        if (from.isOf(Blocks.STONE)) {
            return Optional.of(Blocks.DEEPSLATE.getDefaultState());
        }
        return Optional.empty();
    }

    protected static double chanceFromDurationMultiplier(double mult) {
        return MathHelper.clamp(2.0 * mult, 0, 1);
    }

    public static double getSplashConversionChance(double distance) {
        // vanilla calculation -- don't change
        double mult = MathHelper.clamp(1.0 - distance / 4.0, 0, 1);

        return chanceFromDurationMultiplier(mult);
    }

    public static void convertDeepslateAtSplash(World world, Vec3d hitPos) {
        BlockPos.stream(Box.of(hitPos, 8.25, 4.25, 8.25)).forEach(pos -> {
            BlockState state = world.getBlockState(pos);
            Optional<BlockState> optionalConvertedState = canConvert(state);
            if (optionalConvertedState.isPresent()) {
                double distance = Math.sqrt(pos.toCenterPos().squaredDistanceTo(hitPos));
                if (world.random.nextDouble() < getSplashConversionChance(distance)) {
                    world.setBlockState(pos, optionalConvertedState.get());
                }
            }
        });
    }

    public static void convertDeepslateInCloud(World world, Box box) {
        BlockPos.stream(box).forEach(pos -> {
            BlockState state = world.getBlockState(pos);
            Optional<BlockState> optionalConvertedState = canConvert(state);
            optionalConvertedState.ifPresent(blockState -> world.setBlockState(pos, blockState));
        });
    }

    public static boolean convertDeepslateWithBottle(World world, BlockPos blockPos, BlockPos eventPos) {
        BlockState state = world.getBlockState(blockPos);
        Optional<BlockState> optionalConvertedState = canConvert(state);
        if (optionalConvertedState.isPresent()) {
            if (!world.isClient) {
                ServerWorld serverWorld = (ServerWorld) world;
                for (int i = 0; i < 5; ++i) {
                    serverWorld.spawnParticles(
                            ParticleTypes.SPLASH,
                            (double) eventPos.getX() + world.random.nextDouble(),
                            eventPos.getY() + 1,
                            (double) eventPos.getZ() + world.random.nextDouble(),
                            1,
                            0.0,
                            0.0,
                            0.0,
                            1.0);
                }
            }
            world.playSound(null, eventPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, eventPos);
            world.setBlockState(blockPos, optionalConvertedState.get());
            return true;
        }
        return false;
    }
}
