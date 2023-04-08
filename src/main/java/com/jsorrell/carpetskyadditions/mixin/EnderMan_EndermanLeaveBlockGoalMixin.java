package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EnderMan.EndermanLeaveBlockGoal.class)
public abstract class EnderMan_EndermanLeaveBlockGoalMixin {
    @Shadow
    @Final
    private EnderMan enderman;

    @Inject(
            method = "tick",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/block/Block;updateFromNeighbourShapes(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                            shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void inject(
            CallbackInfo ci,
            RandomSource random,
            Level world,
            int x,
            int y,
            int z,
            BlockPos placePosBottom,
            BlockState placeStateBottom,
            BlockPos belowPlacePos,
            BlockState belowPosState,
            BlockState heldBlockState) {
        Block heldBlock = heldBlockState.getBlock();
        if (heldBlock instanceof DoublePlantBlock || heldBlock instanceof DoorBlock) {
            BlockPos placePosTop = placePosBottom.above();
            BlockState placeStateTop = world.getBlockState(placePosTop);
            if (placePosTop.getY() < world.getMaxBuildHeight()
                    && placeStateBottom.isAir()
                    && placeStateTop.isAir()
                    && !belowPosState.isAir()
                    && !belowPosState.is(Blocks.BEDROCK)
                    && belowPosState.isCollisionShapeFullBlock(world, belowPlacePos)
                    && heldBlockState.canSurvive(world, placePosBottom)
                    && world.getEntities(enderman, new AABB(x, y, z, x + 1.0, y + 2.0, z + 1.0))
                            .isEmpty()) {

                if (heldBlock instanceof DoorBlock) {
                    boolean powered = world.hasNeighborSignal(placePosBottom) || world.hasNeighborSignal(placePosTop);
                    // FIXME what about facing and hinge?
                    heldBlockState = heldBlockState
                            .setValue(BlockStateProperties.POWERED, powered)
                            .setValue(BlockStateProperties.OPEN, powered);
                }
                world.setBlockAndUpdate(placePosBottom, heldBlockState);
                heldBlock.setPlacedBy(world, placePosBottom, heldBlockState, enderman, ItemStack.EMPTY);
                world.gameEvent(GameEvent.BLOCK_PLACE, placePosBottom, GameEvent.Context.of(enderman, heldBlockState));
                enderman.setCarriedBlock(null);
            }
            ci.cancel();
        }
    }
}
