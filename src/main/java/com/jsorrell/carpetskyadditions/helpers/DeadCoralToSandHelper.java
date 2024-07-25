package com.jsorrell.carpetskyadditions.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public class DeadCoralToSandHelper {
    protected static final double BREAK_CHANCE = 0.03;

    public static int getSandDropDelay(RandomSource random) {
        // 16-32s per sand, 24 on average
        // This comes out to about the same as 1 villager in a max rate iron farm, which is also infinitely auto.
        return 320 + random.nextInt(320);
    }

    public static boolean tryDropSand(BlockState state, Level level, BlockPos pos, RandomSource random) {
        FluidState fluidState = level.getFluidState(pos);
        if (!fluidState.is(Fluids.WATER)) {
            return false;
        }

        Vec3 waterVelocity = fluidState.getFlow(level, pos);
        if (waterVelocity.equals(Vec3.ZERO)) {
            return false;
        }

        if (!level.isClientSide) {
            Vec3 sandVelocity = waterVelocity.scale(0.1);
            Item sandItem =
                    state.getBlock().getLootTable().location().getPath().contains("fire") ? Items.RED_SAND : Items.SAND;
            ItemEntity itemEntity = new ItemEntity(
                    level,
                    pos.getX() + 0.5,
                    pos.getY(),
                    pos.getZ() + 0.5,
                    new ItemStack(sandItem),
                    sandVelocity.x(),
                    sandVelocity.y(),
                    sandVelocity.z());
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }

        if (random.nextFloat() < BREAK_CHANCE) {
            level.removeBlock(pos, false);
            level.playSound(
                    null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SAND_BREAK, SoundSource.BLOCKS, 0.5f, 1f);
            return false;
        }

        return true;
    }
}
