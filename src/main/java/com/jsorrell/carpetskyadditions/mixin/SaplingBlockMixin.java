package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin extends BushBlock {

    public SaplingBlockMixin(BlockBehaviour.Properties settings) {
        super(settings);
    }

    private boolean saplingIsOnSand(BlockGetter world, BlockPos pos) {
        BlockState underBlock = world.getBlockState(pos.below());
        return underBlock.is(Blocks.SAND) || underBlock.is(Blocks.RED_SAND);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isPropagule() {
        return (BushBlock) this instanceof MangrovePropaguleBlock;
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        if (SkyAdditionsSettings.saplingsDieOnSand) {
            if (!this.isPropagule()) {
                return floor.is(Blocks.SAND) || floor.is(Blocks.RED_SAND) || super.mayPlaceOn(floor, world, pos);
            }
        }
        return super.mayPlaceOn(floor, world, pos);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void killIfOnSand(
            BlockState blockState, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (SkyAdditionsSettings.saplingsDieOnSand && saplingIsOnSand(world, pos)) {
            if (random.nextFloat() < 0.2) {
                world.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), Block.UPDATE_ALL);
            }
            ci.cancel();
        }
    }

    @Inject(method = "isValidBonemealTarget", at = @At("HEAD"), cancellable = true)
    private void stopBonemealingOnSand(
            LevelReader world, BlockPos pos, BlockState state, boolean isClient, CallbackInfoReturnable<Boolean> cir) {
        if (SkyAdditionsSettings.saplingsDieOnSand && saplingIsOnSand(world, pos)) {
            cir.setReturnValue(false);
        }
    }
}
