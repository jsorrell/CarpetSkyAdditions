package com.jsorrell.skyblock.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;

public class WanderingTrader {
  public static final Int2ObjectMap<TradeOffers.Factory[]> WANDERING_TRADER_SKYBLOCK_TRADES;

  static {
    List<TradeOffers.Factory> tier1Trades =
        new ArrayList<>(Arrays.asList(TradeOffers.WANDERING_TRADER_TRADES.get(1)));
    List<TradeOffers.Factory> tier2Trades =
        new ArrayList<>(Arrays.asList(TradeOffers.WANDERING_TRADER_TRADES.get(2)));

    tier1Trades.addAll(
        Arrays.asList(
            // Tall Flowers
            new TradeOffers.SellItemFactory(Items.SUNFLOWER, 1, 1, 12, 1),
            new TradeOffers.SellItemFactory(Items.LILAC, 1, 1, 12, 1),
            new TradeOffers.SellItemFactory(Items.ROSE_BUSH, 1, 1, 12, 1),
            new TradeOffers.SellItemFactory(Items.PEONY, 1, 1, 12, 1)));

    tier2Trades.addAll(
        Arrays.asList(
            // Tier 2 Trades
            new TradeOffers.ProcessItemFactory(Items.BUCKET, 1, 16, Items.LAVA_BUCKET, 1, 1, 1)));

    WANDERING_TRADER_SKYBLOCK_TRADES =
        new Int2ObjectOpenHashMap<>(
            ImmutableMap.of(
                1,
                tier1Trades.toArray(new TradeOffers.Factory[0]),
                2,
                tier2Trades.toArray(new TradeOffers.Factory[0])));
  }
}
