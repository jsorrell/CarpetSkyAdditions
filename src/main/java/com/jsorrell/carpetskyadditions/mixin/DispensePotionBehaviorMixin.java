package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeepslateConversionHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/core/dispenser/DispenseItemBehavior$27")
public class DispensePotionBehaviorMixin {
    @Inject(method = "execute", at = @At(value = "HEAD"), cancellable = true)
    private void addDeepslateConversionBehavior(
            BlockSource pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (SkyAdditionsSettings.doRenewableDeepslate) {
            if (PotionUtils.getPotion(stack) == DeepslateConversionHelper.CONVERSION_POTION) {
                ServerLevel level = pointer.level();
                BlockPos dispenserPos = pointer.pos();
                Direction dispenserFacing = pointer.state().getValue(DispenserBlock.FACING);
                BlockPos targetPos = dispenserPos.relative(dispenserFacing);
                if (DeepslateConversionHelper.convertDeepslateWithBottle(level, targetPos, dispenserPos)) {
                    cir.setReturnValue(new ItemStack(Items.GLASS_BOTTLE));
                }
            }
        }
    }
}
