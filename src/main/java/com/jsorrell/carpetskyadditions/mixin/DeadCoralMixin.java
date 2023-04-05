package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeadCoralToSandHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseCoralFanBlock;
import net.minecraft.world.level.block.BaseCoralPlantBlock;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.CoralFanBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({BaseCoralPlantBlock.class, BaseCoralFanBlock.class})
public abstract class DeadCoralMixin extends BaseCoralPlantTypeBlock {
    public DeadCoralMixin(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (SkyAdditionsSettings.coralErosion) {
            world.scheduleTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(world.getRandom()));
        }
        super.onPlace(state, world, pos, oldState, notify);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isCoralFan() {
        return (BaseCoralPlantTypeBlock) this instanceof CoralFanBlock;
    }

    public BlockState getStateForNeighborUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor world,
            BlockPos pos,
            BlockPos neighborPos) {
        if (SkyAdditionsSettings.coralErosion && !this.isCoralFan()) {
            world.scheduleTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(world.getRandom()));
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (SkyAdditionsSettings.coralErosion) {
            if (DeadCoralToSandHelper.tryDropSand(state, world, pos, random)) {
                world.scheduleTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(random));
            }
        }
    }
}
