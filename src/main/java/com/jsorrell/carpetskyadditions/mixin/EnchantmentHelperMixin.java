package com.jsorrell.carpetskyadditions.mixin;

import static com.jsorrell.carpetskyadditions.helpers.SkyAdditionsEnchantmentHelper.SWIFT_SNEAK_ENCHANTABLE_TAG;

import com.jsorrell.carpetskyadditions.helpers.SkyAdditionsEnchantmentHelper;
import java.util.List;
import net.minecraft.world.item.Item;
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
            int modifiedEnchantingLevel,
            ItemStack stack,
            boolean allowTreasure,
            CallbackInfoReturnable<List<EnchantmentInstance>> cir,
            List<EnchantmentInstance> enchantmentList,
            Item item) {
        if (stack.hasTag() && stack.getTag().contains(SWIFT_SNEAK_ENCHANTABLE_TAG)) {
            if (Enchantments.SWIFT_SNEAK.category.canEnchant(stack.getItem()) || stack.is(Items.BOOK)) {
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
