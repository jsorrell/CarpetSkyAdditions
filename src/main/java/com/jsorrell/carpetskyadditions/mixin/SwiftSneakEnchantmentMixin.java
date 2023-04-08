package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.SwiftSneakEnchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwiftSneakEnchantment.class)
public class SwiftSneakEnchantmentMixin extends Enchantment {
    protected SwiftSneakEnchantmentMixin(Rarity weight, EnchantmentCategory type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMinCost(int level) {
        return SkyAdditionsSettings.renewableSwiftSneak ? 14 + level * 7 : super.getMinCost(level);
    }
}
