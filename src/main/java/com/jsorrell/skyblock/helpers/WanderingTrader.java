package com.jsorrell.skyblock.helpers;

import com.google.common.collect.ImmutableMap;
import com.jsorrell.skyblock.settings.SkyBlockSettings;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WanderingTrader {
  public static Int2ObjectMap<TradeOffers.Factory[]> getTrades() {
    List<TradeOffers.Factory> tier1Trades =
      new ArrayList<>(Arrays.asList(TradeOffers.WANDERING_TRADER_TRADES.get(1)));
    List<TradeOffers.Factory> tier2Trades =
      new ArrayList<>(Arrays.asList(TradeOffers.WANDERING_TRADER_TRADES.get(2)));

    if (SkyBlockSettings.wanderingTraderSkyBlockTrades) {
      tier1Trades.addAll(
        Arrays.asList(
          // Tall Flowers
          new TradeOffers.SellItemFactory(Items.SUNFLOWER, 1, 1, 12, 1),
          new TradeOffers.SellItemFactory(Items.LILAC, 1, 1, 12, 1),
          new TradeOffers.SellItemFactory(Items.ROSE_BUSH, 1, 1, 12, 1),
          new TradeOffers.SellItemFactory(Items.PEONY, 1, 1, 12, 1)));
    }

    if (SkyBlockSettings.lavaFromWanderingTrader) {
      tier2Trades.add(new TradeOffers.ProcessItemFactory(Items.BUCKET, 1, 16, Items.LAVA_BUCKET, 1, 1, 1));
    }

    return new Int2ObjectOpenHashMap<>(
      ImmutableMap.of(
        1,
        tier1Trades.toArray(new TradeOffers.Factory[0]),
        2,
        tier2Trades.toArray(new TradeOffers.Factory[0])));
  }
}
