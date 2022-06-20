package com.jsorrell.carpetskyadditions.helpers;

import com.jsorrell.carpetskyadditions.fakes.VexEntityInterface;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.math.BlockPos;

public class SkyAdditionsMinecartComparatorLogic implements MinecartComparatorLogic<MinecartEntity> {
  @Override
  public int getComparatorValue(MinecartEntity minecart, BlockState state, BlockPos pos) {
    if (SkyAdditionsSettings.allayableVexes && minecart.getFirstPassenger() instanceof VexEntity vex) {
      return ((VexEntityInterface) vex).getNextNote();
    }
    return 0;
  }
}
