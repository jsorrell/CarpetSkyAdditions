package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.SwiftSneakEnchantment;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwiftSneakEnchantment.class)
public class SwiftSneakEnchantmentMixin extends Enchantment {
  protected SwiftSneakEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
    super(weight, type, slotTypes);
  }

  @Override
  public int getMinPower(int level) {
    return 14 + level * 7;
  }
}
