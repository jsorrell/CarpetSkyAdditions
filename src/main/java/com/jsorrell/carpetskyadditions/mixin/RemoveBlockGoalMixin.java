package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RemoveBlockGoal.class)
public class RemoveBlockGoalMixin {
    @Shadow
    @Final
    private Mob removerMob;

    @Shadow
    @Final
    private Block blockToRemove;

    @Inject(
            method = "tick",
            at =
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z",
                            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void placeSnifferEgg(
            CallbackInfo ci, Level level, BlockPos drownedBlockPos, BlockPos eggPos, RandomSource randomSource) {
        if (SkyAdditionsSettings.sniffersFromDrowneds
                && blockToRemove == Blocks.TURTLE_EGG
                && removerMob instanceof Drowned drowned) {
            ItemStack offhand = drowned.getItemInHand(InteractionHand.OFF_HAND);
            if (offhand.is(Items.SNIFFER_EGG)) {
                level.setBlockAndUpdate(eggPos, Blocks.SNIFFER_EGG.defaultBlockState());
                offhand.shrink(1);
            }
        }
    }
}
