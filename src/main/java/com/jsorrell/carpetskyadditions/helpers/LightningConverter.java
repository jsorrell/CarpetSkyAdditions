package com.jsorrell.carpetskyadditions.helpers;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlowLichenBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LightningConverter {
    public static void strike(Level world, BlockPos pos) {
        BlockState rawHitBlock = world.getBlockState(pos);
        BlockPos hitBlockPos;
        BlockState hitBlock;
        if (rawHitBlock.is(Blocks.LIGHTNING_ROD)) {
            hitBlockPos =
                    pos.relative(rawHitBlock.getValue(LightningRodBlock.FACING).getOpposite());
            hitBlock = world.getBlockState(hitBlockPos);
        } else {
            hitBlockPos = pos;
            hitBlock = rawHitBlock;
        }

        alchemizeVinesToGlowLichen(world, hitBlockPos, hitBlock);
    }

    protected static void alchemizeVinesToGlowLichen(Level world, BlockPos hitBlockPos, BlockState hitBlock) {
        if (!(SkyAdditionsSettings.lightningElectrifiesVines && hitBlock.is(Blocks.GLOWSTONE))) return;

        for (Direction dir : Direction.values()) {
            BlockPos adjacentBlockPos = hitBlockPos.offset(dir.getNormal());
            BlockState adjacentBlock = world.getBlockState(adjacentBlockPos);
            Direction opDir = dir.getOpposite();
            if (adjacentBlock.is(Blocks.VINE) && adjacentBlock.getValue(VineBlock.getPropertyForFace(opDir))) {
                BlockState glowLichen =
                        Blocks.GLOW_LICHEN.defaultBlockState().setValue(GlowLichenBlock.getFaceProperty(opDir), true);
                world.setBlockAndUpdate(adjacentBlockPos, glowLichen);
            }
        }
    }
}
