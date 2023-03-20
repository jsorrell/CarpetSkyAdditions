package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeadCoralToSandHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DeadCoralWallFanBlock.class)
public class DeadCoralWallFanBlockMixin extends DeadCoralFanBlock {
    public DeadCoralWallFanBlockMixin(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isCoralWallFan() {
        return (DeadCoralFanBlock) this instanceof CoralWallFanBlock;
    }

    @Inject(method = "getStateForNeighborUpdate", at = @At(value = "HEAD"))
    private void scheduleTickOnBlockUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos,
            CallbackInfoReturnable<BlockState> cir) {
        if (SkyAdditionsSettings.coralErosion && !this.isCoralWallFan()) {
            world.scheduleBlockTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(world.getRandom()));
        }
    }
}
