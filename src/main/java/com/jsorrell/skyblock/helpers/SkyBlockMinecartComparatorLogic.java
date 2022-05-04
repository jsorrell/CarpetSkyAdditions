package com.jsorrell.skyblock.helpers;

import com.jsorrell.skyblock.SkyBlockSettings;
import com.jsorrell.skyblock.fakes.VexEntityInterface;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.math.BlockPos;

public class SkyBlockMinecartComparatorLogic implements MinecartComparatorLogic<MinecartEntity> {
  @Override
  public int getComparatorValue(MinecartEntity minecart, BlockState state, BlockPos pos) {
    if (SkyBlockSettings.renewableAllays && minecart.getFirstPassenger() instanceof VexEntity vex) {
      return ((VexEntityInterface) vex).getNextNote();
    }
    return 0;
  }
}
