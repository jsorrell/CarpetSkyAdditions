package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.settings.SkyBlockSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxEntity.class)
public abstract class FoxEntityMixin extends Entity {
  private FoxEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Inject(
    method = "initEquipment",
    cancellable = true,
    at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F", ordinal = 1))
  private void addFoxHeldItem(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
    if (0 < SkyBlockSettings.foxesSpawnWithSweetBerriesChance) {
      float f = random.nextFloat();
      ItemStack equippedItem;
      if (f < SkyBlockSettings.foxesSpawnWithSweetBerriesChance) {
        equippedItem = new ItemStack(Items.SWEET_BERRIES);
        this.equipStack(EquipmentSlot.MAINHAND, equippedItem);
        ci.cancel();
      }
    }
  }
}
