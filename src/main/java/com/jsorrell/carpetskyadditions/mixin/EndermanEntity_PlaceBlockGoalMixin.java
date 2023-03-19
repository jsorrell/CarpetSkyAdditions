package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.block.*;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EndermanEntity.PlaceBlockGoal.class)
public abstract class EndermanEntity_PlaceBlockGoalMixin {
    @Shadow
    @Final
    private EndermanEntity enderman;

    @Inject(
            method = "tick",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/block/Block;postProcessState(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;",
                            shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void inject(
            CallbackInfo ci,
            Random random,
            World world,
            int x,
            int y,
            int z,
            BlockPos placePosBottom,
            BlockState placeStateBottom,
            BlockPos belowPlacePos,
            BlockState belowPosState,
            BlockState heldBlockState) {
        Block heldBlock = heldBlockState.getBlock();
        if (heldBlock instanceof TallPlantBlock || heldBlock instanceof DoorBlock) {
            BlockPos placePosTop = placePosBottom.up();
            BlockState placeStateTop = world.getBlockState(placePosTop);
            if (placePosTop.getY() < world.getTopY()
                    && placeStateBottom.isAir()
                    && placeStateTop.isAir()
                    && !belowPosState.isAir()
                    && !belowPosState.isOf(Blocks.BEDROCK)
                    && belowPosState.isFullCube(world, belowPlacePos)
                    && heldBlockState.canPlaceAt(world, placePosBottom)
                    && world.getOtherEntities(this.enderman, new Box(x, y, z, x + 1.0, y + 2.0, z + 1.0))
                            .isEmpty()) {

                if (heldBlock instanceof DoorBlock) {
                    boolean powered = world.isReceivingRedstonePower(placePosBottom)
                            || world.isReceivingRedstonePower(placePosTop);
                    // FIXME what about facing and hinge?
                    heldBlockState =
                            heldBlockState.with(Properties.POWERED, powered).with(Properties.OPEN, powered);
                }
                world.setBlockState(placePosBottom, heldBlockState);
                heldBlock.onPlaced(world, placePosBottom, heldBlockState, this.enderman, ItemStack.EMPTY);
                world.emitGameEvent(
                        GameEvent.BLOCK_PLACE, placePosBottom, GameEvent.Emitter.of(this.enderman, heldBlockState));
                this.enderman.setCarriedBlock(null);
            }
            ci.cancel();
        }
    }
}
