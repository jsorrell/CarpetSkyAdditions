package com.jsorrell.carpetskyadditions.helpers;

import com.google.common.collect.ImmutableMap;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;

public class WanderingTrader {
    public static Int2ObjectMap<VillagerTrades.ItemListing[]> getTrades() {
        List<VillagerTrades.ItemListing> tier1Trades =
                new ArrayList<>(Arrays.asList(VillagerTrades.WANDERING_TRADER_TRADES.get(1)));
        List<VillagerTrades.ItemListing> tier2Trades =
                new ArrayList<>(Arrays.asList(VillagerTrades.WANDERING_TRADER_TRADES.get(2)));

        if (SkyAdditionsSettings.tallFlowersFromWanderingTrader) {
            Collections.addAll(
                    tier1Trades,
                    new VillagerTrades.ItemsForEmeralds(Items.SUNFLOWER, 1, 1, 12, 1),
                    new VillagerTrades.ItemsForEmeralds(Items.LILAC, 1, 1, 12, 1),
                    new VillagerTrades.ItemsForEmeralds(Items.ROSE_BUSH, 1, 1, 12, 1),
                    new VillagerTrades.ItemsForEmeralds(Items.PEONY, 1, 1, 12, 1));
        }

        if (SkyAdditionsSettings.lavaFromWanderingTrader) {
            tier2Trades.add(
                    new VillagerTrades.ItemsAndEmeraldsToItems(Items.BUCKET, 1, 16, Items.LAVA_BUCKET, 1, 1, 1));
        }

        return new Int2ObjectOpenHashMap<>(ImmutableMap.of(
                1,
                tier1Trades.toArray(new VillagerTrades.ItemListing[0]),
                2,
                tier2Trades.toArray(new VillagerTrades.ItemListing[0])));
    }
}
