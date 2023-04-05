package com.jsorrell.carpetskyadditions.helpers;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DeepslateConversionHelper {
    public static final Potion CONVERSION_POTION = Potions.THICK;

    public static Optional<BlockState> canConvert(BlockState from) {
        if (from.is(Blocks.STONE)) {
            return Optional.of(Blocks.DEEPSLATE.defaultBlockState());
        }
        return Optional.empty();
    }

    protected static double chanceFromDurationMultiplier(double mult) {
        return Mth.clamp(2.0 * mult, 0, 1);
    }

    public static double getSplashConversionChance(double distance) {
        // vanilla calculation -- don't change
        double mult = Mth.clamp(1.0 - distance / 4.0, 0, 1);

        return chanceFromDurationMultiplier(mult);
    }

    public static void convertDeepslateAtSplash(Level world, Vec3 hitPos) {
        BlockPos.betweenClosedStream(AABB.ofSize(hitPos, 8.25, 4.25, 8.25)).forEach(pos -> {
            BlockState state = world.getBlockState(pos);
            Optional<BlockState> optionalConvertedState = canConvert(state);
            if (optionalConvertedState.isPresent()) {
                double distance = Math.sqrt(pos.getCenter().distanceToSqr(hitPos));
                if (world.random.nextDouble() < getSplashConversionChance(distance)) {
                    world.setBlockAndUpdate(pos, optionalConvertedState.get());
                }
            }
        });
    }

    public static void convertDeepslateInCloud(Level world, AABB box) {
        BlockPos.betweenClosedStream(box).forEach(pos -> {
            BlockState state = world.getBlockState(pos);
            Optional<BlockState> optionalConvertedState = canConvert(state);
            optionalConvertedState.ifPresent(blockState -> world.setBlockAndUpdate(pos, blockState));
        });
    }

    public static boolean convertDeepslateWithBottle(Level world, BlockPos blockPos, BlockPos eventPos) {
        BlockState state = world.getBlockState(blockPos);
        Optional<BlockState> optionalConvertedState = canConvert(state);
        if (optionalConvertedState.isPresent()) {
            if (!world.isClientSide) {
                ServerLevel serverWorld = (ServerLevel) world;
                for (int i = 0; i < 5; ++i) {
                    serverWorld.sendParticles(
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
            world.playSound(null, eventPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            world.gameEvent(null, GameEvent.FLUID_PLACE, eventPos);
            world.setBlockAndUpdate(blockPos, optionalConvertedState.get());
            return true;
        }
        return false;
    }
}
