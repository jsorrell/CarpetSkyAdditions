package com.jsorrell.skyblock.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.village.TradeOffers;

import com.jsorrell.skyblock.SkyBlockSettings;
import com.jsorrell.skyblock.helpers.WanderingTrader;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin {
  @Redirect(
      method = "fillRecipes",
      at =
          @At(
              value = "FIELD",
              opcode = Opcodes.GETSTATIC,
              target =
                  "Lnet/minecraft/village/TradeOffers;WANDERING_TRADER_TRADES:Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;"))
  private Int2ObjectMap<TradeOffers.Factory[]> getTrades() {
    if (SkyBlockSettings.enableSkyBlockFeatures && SkyBlockSettings.wanderingTraderSkyBlockTrades) {
      return WanderingTrader.WANDERING_TRADER_SKYBLOCK_TRADES;
    } else {
      return TradeOffers.WANDERING_TRADER_TRADES;
    }
  }
}
