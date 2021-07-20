package com.jsorrell.skyblock.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;

import com.jsorrell.skyblock.SkyBlockSettings;
import com.jsorrell.skyblock.helpers.UsefulComposter;
import com.jsorrell.skyblock.helpers.UsefulComposter.FullComposterInventory;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {

  @Inject(
      method = "emptyFullComposter",
      locals = LocalCapture.CAPTURE_FAILSOFT,
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/entity/ItemEntity;setToDefaultPickupDelay()V"))
  private static void setItemDroppedFromComposter(
      BlockState arg0,
      World world,
      BlockPos pos,
      CallbackInfoReturnable<BlockState> cir,
      float f,
      double d,
      double e,
      double g,
      ItemEntity droppedItem) {
    if (SkyBlockSettings.doUsefulComposters) {
      if (!SkyBlockSettings.usefulCompostersNeedRedstone || world.isReceivingRedstonePower(pos)) {
        Biome biome = world.getBiome(pos);
        Item composterProduct = UsefulComposter.getComposterProduct(biome);
        droppedItem.setStack(new ItemStack(composterProduct));
      }
    }
  }

  @Inject(
      method = "getInventory",
      locals = LocalCapture.CAPTURE_FAILSOFT,
      cancellable = true,
      at = @At(value = "RETURN", ordinal = 0))
  private void setFullComposterInventory(
      BlockState state,
      WorldAccess world,
      BlockPos pos,
      CallbackInfoReturnable<SidedInventory> cir) {
    if (SkyBlockSettings.doUsefulComposters) {
      if (!SkyBlockSettings.usefulCompostersNeedRedstone
          || ((World) world).isReceivingRedstonePower(pos)) {
        SidedInventory inventory = new FullComposterInventory(state, world, pos);
        cir.setReturnValue(inventory);
      }
    }
  }
}
