package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeadCoralToSandHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseCoralFanBlock;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.CoralWallFanBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCoralWallFanBlock.class)
public class DeadCoralWallFanBlockMixin extends BaseCoralFanBlock {
    public DeadCoralWallFanBlockMixin(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isCoralWallFan() {
        return (BaseCoralFanBlock) this instanceof CoralWallFanBlock;
    }

    @Inject(method = "updateShape", at = @At(value = "HEAD"))
    private void scheduleTickOnBlockUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor world,
            BlockPos pos,
            BlockPos neighborPos,
            CallbackInfoReturnable<BlockState> cir) {
        if (SkyAdditionsSettings.coralErosion && !this.isCoralWallFan()) {
            world.scheduleTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(world.getRandom()));
        }
    }
}
