package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.helpers.AncientCityEnchantingTableHelper;
import com.jsorrell.skyblock.settings.SkyBlockSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.structure.StructureTypeKeys;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {
  @Shadow
  @Final
  private ScreenHandlerContext context;

  @Redirect(method = "generateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;generateEnchantments(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;IZ)Ljava/util/List;"))
  private List<EnchantmentLevelEntry> addSwiftSneak(Random random, ItemStack stack, int level, boolean treasureAllowed) {
    if (SkyBlockSettings.swiftSneakFromEnchantingTable) {
      boolean inAncientCity = this.context.get((world, pos) -> {
        assert world instanceof ServerWorld;
        return ((ServerWorld) world).getStructureAccessor().getStructureContaining(pos, StructureTypeKeys.ANCIENT_CITY).hasChildren();
      }).orElseThrow();
      if (inAncientCity) {
        return AncientCityEnchantingTableHelper.generateEnchantments(random, stack, level, treasureAllowed);
      }
    }
    return EnchantmentHelper.generateEnchantments(random, stack, level, treasureAllowed);
  }
}
