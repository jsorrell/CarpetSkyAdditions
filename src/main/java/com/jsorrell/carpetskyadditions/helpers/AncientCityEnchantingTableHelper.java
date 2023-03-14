package com.jsorrell.carpetskyadditions.helpers;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Weighting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class AncientCityEnchantingTableHelper {
  // Copied verbatim from EnchantmentHelper
  // TODO find a cleaner way to do this
  public static List<EnchantmentLevelEntry> generateEnchantments(Random random, ItemStack stack, int level, boolean treasureAllowed) {
    ArrayList<EnchantmentLevelEntry> list = Lists.newArrayList();
    Item item = stack.getItem();
    int i = item.getEnchantability();
    if (i <= 0) {
      return list;
    }
    level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
    float f = (random.nextFloat() + random.nextFloat() - 1.0f) * 0.15f;
    List<EnchantmentLevelEntry> list2 = AncientCityEnchantingTableHelper.getPossibleEntries(level = MathHelper.clamp(Math.round((float) level + (float) level * f), 1, Integer.MAX_VALUE), stack, treasureAllowed);
    if (!list2.isEmpty()) {
      Weighting.getRandom(random, list2).ifPresent(list::add);
      while (random.nextInt(50) <= level) {
        if (!list.isEmpty()) {
          EnchantmentHelper.removeConflicts(list2, Util.getLast(list));
        }
        if (list2.isEmpty()) break;
        Weighting.getRandom(random, list2).ifPresent(list::add);
        level /= 2;
      }
    }
    return list;
  }

  public static List<EnchantmentLevelEntry> getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed) {
    ArrayList<EnchantmentLevelEntry> enchantmentList = Lists.newArrayList();
    Item item = stack.getItem();
    boolean isBook = stack.isOf(Items.BOOK);
    block0:
    for (Enchantment enchantment : Registries.ENCHANTMENT) {
      // Force allow Swift Sneak
      if (!enchantment.equals(Enchantments.SWIFT_SNEAK)) {
        if (enchantment.isTreasure() && !treasureAllowed || !enchantment.isAvailableForRandomSelection()) continue;
      }

      if (!enchantment.target.isAcceptableItem(item) && !isBook) continue;

      for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
        if (power < enchantment.getMinPower(i) || power > enchantment.getMaxPower(i)) continue;
        enchantmentList.add(new EnchantmentLevelEntry(enchantment, i));
        continue block0;
      }
    }
    return enchantmentList;
  }
}
