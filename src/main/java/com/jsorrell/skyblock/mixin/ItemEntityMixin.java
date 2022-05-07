package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.settings.SkyBlockSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

  public ItemEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Shadow(prefix = "shadow$")
  public abstract ItemStack shadow$getStack();

  @Shadow(prefix = "shadow$")
  public abstract void shadow$setStack(ItemStack stack);

  private boolean canCompactToDiamonds(ItemStack stack) {
    return Items.COAL_BLOCK.equals(stack.getItem()) && stack.getCount() == stack.getMaxCount();
  }

  @Inject(
      method = "damage",
      locals = LocalCapture.CAPTURE_FAILSOFT,
      cancellable = true,
      at = @At(value = "HEAD"))
  private void compactCoalToDiamonds(
      DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
    if (SkyBlockSettings.renewableDiamonds) {
      if (source == DamageSource.ANVIL) {
        ItemStack stack = this.shadow$getStack();
        if (canCompactToDiamonds(stack)) {
          this.shadow$setStack(new ItemStack(Items.DIAMOND));
          cir.setReturnValue(true);
        }
      }
    }
  }
}
