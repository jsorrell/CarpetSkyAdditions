package skyblock;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trades {
    public static final Int2ObjectMap<TradeOffers.Factory[]> WANDERING_TRADER_SKYBLOCK_TRADES;

    private static final Constructor<?> processItemFactoryConstructor;
    private static final Constructor<?> sellItemFactoryConstructor;
    static {
        try {
            // These indices may change during updates, but so may the unmapped names. Best to just keep this up to date.
            processItemFactoryConstructor = TradeOffers.class.getDeclaredClasses()[0].getDeclaredConstructor(ItemConvertible.class, int.class, int.class, Item.class, int.class, int.class, int.class);
            processItemFactoryConstructor.setAccessible(true);

            sellItemFactoryConstructor = TradeOffers.class.getDeclaredClasses()[7].getDeclaredConstructor(Item.class, int.class, int.class, int.class, int.class);
            sellItemFactoryConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected static TradeOffers.Factory newProcessItemFactory(ItemConvertible item, int secondCount, int price, Item sellItem, int sellCount, int maxUses) {
        try {
            return (TradeOffers.Factory) processItemFactoryConstructor.newInstance(item, secondCount, price, sellItem, sellCount, maxUses, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static TradeOffers.Factory newSellItemFactory(Item item, int price, int count, int maxUses){
        try {
            return (TradeOffers.Factory) sellItemFactoryConstructor.newInstance(item, price, count, maxUses, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static {
        List<TradeOffers.Factory> tier1Trades = new ArrayList<>(Arrays.asList(TradeOffers.WANDERING_TRADER_TRADES.get(1)));
        List<TradeOffers.Factory> tier2Trades = new ArrayList<>(Arrays.asList(TradeOffers.WANDERING_TRADER_TRADES.get(2)));

        tier1Trades.addAll(Arrays.asList(
                // Duplicatable after one trade
                newSellItemFactory(Items.CHORUS_FLOWER, 5, 1, 6),
                newSellItemFactory(Items.NETHER_WART, 1, 1, 12),
                newSellItemFactory(Items.SWEET_BERRIES, 1, 1, 16),
                newSellItemFactory(Items.SUNFLOWER, 5, 1, 10),
                newSellItemFactory(Items.LILAC, 5, 1, 10),
                newSellItemFactory(Items.ROSE_BUSH, 5, 1, 10),
                newSellItemFactory(Items.PEONY, 5, 1, 10),
                newSellItemFactory(Items.GLOW_LICHEN, 5, 1, 10)
        ));

        tier2Trades.addAll(Arrays.asList(
                // Tier 2 Trades
                newProcessItemFactory(Items.BUCKET, 1, 16, Items.LAVA_BUCKET, 1, 1),
                newSellItemFactory(Items.JUKEBOX, 64, 1, 6),
                newSellItemFactory(Items.ENCHANTING_TABLE, 64, 1, 6),
                newSellItemFactory(Items.HEART_OF_THE_SEA, 64, 1, 6),
                newSellItemFactory(Items.WET_SPONGE, 16, 1, 6),
                newSellItemFactory(Items.MUSIC_DISC_PIGSTEP, 16, 1, 6),
                newSellItemFactory(Items.COBWEB, 3, 1, 9)
        ));

        WANDERING_TRADER_SKYBLOCK_TRADES = new Int2ObjectOpenHashMap<>(ImmutableMap.of(1, tier1Trades.toArray(new TradeOffers.Factory[0]), 2, tier2Trades.toArray(new TradeOffers.Factory[0])));
    }
}
