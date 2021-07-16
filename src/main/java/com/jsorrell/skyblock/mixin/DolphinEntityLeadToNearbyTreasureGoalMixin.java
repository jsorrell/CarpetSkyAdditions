package com.jsorrell.skyblock.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import com.jsorrell.skyblock.SkyBlockSettings;

@Mixin(targets = "net/minecraft/entity/passive/DolphinEntity$LeadToNearbyTreasureGoal")
abstract class DolphinEntityLeadToNearbyTreasureGoalMixin extends Goal {

  private static final float CHANCE_TO_FIND_HEART_OF_THE_SEA = 0.1f;

  @Shadow @Final private DolphinEntity dolphin;

  @Inject(
      method = "stop()V",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/entity/passive/DolphinEntity;setHasFish(Z)V",
              shift = At.Shift.AFTER))
  private void giveDolphinHeartOfTheSea(CallbackInfo ci) {
    if (SkyBlockSettings.enableSkyBlockFeatures && SkyBlockSettings.renewableHeartsOfTheSea) {
      Random random = new Random();
      if (random.nextFloat() < CHANCE_TO_FIND_HEART_OF_THE_SEA) {
        ItemStack heartOfTheSea = new ItemStack(Items.HEART_OF_THE_SEA);
        if (this.dolphin.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()
            && this.dolphin.canPickupItem(heartOfTheSea)) {
          this.dolphin.equipStack(EquipmentSlot.MAINHAND, heartOfTheSea);
        }
      }
    }
  }
}
