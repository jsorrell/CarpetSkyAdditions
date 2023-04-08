package com.jsorrell.carpetskyadditions.helpers;

import com.jsorrell.carpetskyadditions.fakes.VexInterface;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.block.state.BlockState;

public class SkyAdditionsMinecartComparatorLogic implements MinecartComparatorLogic<Minecart> {
    @Override
    public int getComparatorValue(Minecart minecart, BlockState state, BlockPos pos) {
        if (SkyAdditionsSettings.allayableVexes && minecart.getFirstPassenger() instanceof Vex vex) {
            return ((VexInterface) vex).getAllayer().getNextNote();
        }
        return 0;
    }
}
