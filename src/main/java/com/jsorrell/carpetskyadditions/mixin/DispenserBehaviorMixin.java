package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeepslateConversionHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/block/dispenser/DispenserBehavior$20")
public class DispenserBehaviorMixin {
    @Inject(method = "dispenseSilently", at = @At(value = "HEAD"), cancellable = true)
    private void addDeepslateConversionBehavior(
            BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (SkyAdditionsSettings.doRenewableDeepslate) {
            if (PotionUtil.getPotion(stack) == DeepslateConversionHelper.CONVERSION_POTION) {
                ServerWorld serverWorld = pointer.getWorld();
                BlockPos dispenserPos = pointer.getPos();
                Direction dispenserFacing = pointer.getBlockState().get(DispenserBlock.FACING);
                BlockPos targetPos = dispenserPos.offset(dispenserFacing);
                if (DeepslateConversionHelper.convertDeepslateWithBottle(serverWorld, targetPos, dispenserPos)) {
                    cir.setReturnValue(new ItemStack(Items.GLASS_BOTTLE));
                }
            }
        }
    }
}
