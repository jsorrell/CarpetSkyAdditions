package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
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

    private boolean saplingIsOnSand(BlockGetter level, BlockPos pos) {
        BlockState underBlock = level.getBlockState(pos.below());
        return underBlock.is(BlockTags.SAND);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isPropagule() {
        return (BushBlock) this instanceof MangrovePropaguleBlock;
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter level, BlockPos pos) {
        if (SkyAdditionsSettings.saplingsDieOnSand && !isPropagule() && floor.is(BlockTags.SAND)) return true;
        return super.mayPlaceOn(floor, level, pos);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void killIfOnSand(
            BlockState blockState, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (SkyAdditionsSettings.saplingsDieOnSand && saplingIsOnSand(level, pos)) {
            if (random.nextFloat() < 0.2) {
                level.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), Block.UPDATE_ALL);
            }
            ci.cancel();
        }
    }

    @Inject(method = "isValidBonemealTarget", at = @At("HEAD"), cancellable = true)
    private void stopBonemealingOnSand(
            LevelReader level, BlockPos pos, BlockState state, boolean isClient, CallbackInfoReturnable<Boolean> cir) {
        if (SkyAdditionsSettings.saplingsDieOnSand && saplingIsOnSand(level, pos)) {
            cir.setReturnValue(false);
        }
    }
}
