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
public abstract class BaseCoralMixin extends BaseCoralPlantTypeBlock {
    public BaseCoralMixin(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean notify) {
        if (SkyAdditionsSettings.coralErosion) {
            level.scheduleTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(level.getRandom()));
        }
        super.onPlace(state, level, pos, oldState, notify);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isCoralFan() {
        return (BaseCoralPlantTypeBlock) this instanceof CoralFanBlock;
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos) {
        if (SkyAdditionsSettings.coralErosion && !isCoralFan()) {
            level.scheduleTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(level.getRandom()));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (SkyAdditionsSettings.coralErosion) {
            if (DeadCoralToSandHelper.tryDropSand(state, level, pos, random)) {
                level.scheduleTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(random));
            }
        }
    }
}
