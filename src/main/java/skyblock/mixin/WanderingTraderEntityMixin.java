package skyblock.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.village.TradeOffers;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import skyblock.SkyBlockSettings;
import skyblock.Trades;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin {
    @Redirect(method = "fillRecipes", at=@At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/village/TradeOffers;WANDERING_TRADER_TRADES:Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;"))
    private Int2ObjectMap<TradeOffers.Factory[]> getTrades() {
        if (SkyBlockSettings.wanderingTraderSkyblockTrades) {
            return Trades.WANDERING_TRADER_SKYBLOCK_TRADES;
        } else {
            return TradeOffers.WANDERING_TRADER_TRADES;
        }
    }
}
