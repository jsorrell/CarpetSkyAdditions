package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.SkyAdditionsDataComponents;
import com.jsorrell.carpetskyadditions.helpers.SkyAdditionsEnchantmentHelper;
import java.util.List;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(
            method = "getAvailableEnchantmentResults",
            at =
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void forceAllowSwiftSneak(
            FeatureFlagSet featureFlagSet,
            int modifiedEnchantingLevel,
            ItemStack stack,
            boolean allowTreasure,
            CallbackInfoReturnable<List<EnchantmentInstance>> cir,
            List<EnchantmentInstance> enchantmentList) {
        if (Boolean.TRUE.equals(stack.get(SkyAdditionsDataComponents.SWIFT_SNEAK_ENCHANTABLE_COMPONENT))) {
            if (Enchantments.SWIFT_SNEAK.canEnchant(stack) || stack.is(Items.BOOK)) {
                for (int level = 3; 1 <= level; --level) {
                    if (SkyAdditionsEnchantmentHelper.getSwiftSneakMinCost(level) <= modifiedEnchantingLevel
                            && modifiedEnchantingLevel <= SkyAdditionsEnchantmentHelper.getSwiftSneakMaxCost(level)) {
                        enchantmentList.add(new EnchantmentInstance(Enchantments.SWIFT_SNEAK, level));
                        break;
                    }
                }
            }
        }
    }
}
