package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.WanderingTrader;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.world.entity.npc.WanderingTrader.class)
public abstract class WanderingTraderEntityMixin {
    @Redirect(
            method = "updateTrades",
            at =
                    @At(
                            value = "FIELD",
                            opcode = Opcodes.GETSTATIC,
                            target =
                                    "Lnet/minecraft/world/entity/npc/VillagerTrades;WANDERING_TRADER_TRADES:Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;"))
    private Int2ObjectMap<VillagerTrades.ItemListing[]> getTrades() {
        return WanderingTrader.getTrades();
    }
}
