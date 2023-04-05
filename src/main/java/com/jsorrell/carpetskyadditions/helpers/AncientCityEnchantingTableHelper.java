package com.jsorrell.carpetskyadditions.helpers;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.filterCompatibleEnchantments;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;

public class AncientCityEnchantingTableHelper {
    // Copied verbatim from EnchantmentHelper
    // TODO find a cleaner way to do this
    // spotless:off
    public static List<EnchantmentInstance> selectEnchantment(RandomSource random, ItemStack itemStack, int level, boolean allowTreasure) {
        List<EnchantmentInstance> list = Lists.<EnchantmentInstance>newArrayList();
        Item item = itemStack.getItem();
        int i = item.getEnchantmentValue();
        if (i <= 0) {
            return list;
        } else {
            level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
            float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
            level = Mth.clamp(Math.round((float)level + (float)level * f), 1, Integer.MAX_VALUE);
            List<EnchantmentInstance> list2 = getAvailableEnchantmentResults(level, itemStack, allowTreasure);
            if (!list2.isEmpty()) {
                WeightedRandom.getRandomItem(random, list2).ifPresent(list::add);

                while(random.nextInt(50) <= level) {
                    if (!list.isEmpty()) {
                        filterCompatibleEnchantments(list2, Util.lastOf(list));
                    }

                    if (list2.isEmpty()) {
                        break;
                    }

                    WeightedRandom.getRandomItem(random, list2).ifPresent(list::add);
                    level /= 2;
                }
            }

            return list;
        }
    }
    // spotless:on

    public static List<EnchantmentInstance> getAvailableEnchantmentResults(
            int power, ItemStack stack, boolean treasureAllowed) {
        ArrayList<EnchantmentInstance> enchantmentList = Lists.newArrayList();
        Item item = stack.getItem();
        boolean isBook = stack.is(Items.BOOK);
        block0:
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            // Force allow Swift Sneak
            if (!enchantment.equals(Enchantments.SWIFT_SNEAK)) {
                if (enchantment.isTreasureOnly() && !treasureAllowed || !enchantment.isDiscoverable()) continue;
            }

            if (!enchantment.category.canEnchant(item) && !isBook) continue;

            for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                if (power < enchantment.getMinCost(i) || power > enchantment.getMaxCost(i)) continue;
                enchantmentList.add(new EnchantmentInstance(enchantment, i));
                continue block0;
            }
        }
        return enchantmentList;
    }
}
