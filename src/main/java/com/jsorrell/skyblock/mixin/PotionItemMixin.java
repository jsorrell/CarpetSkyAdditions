package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.SkyBlockSettings;
import com.jsorrell.skyblock.helpers.DeepslateConversionHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PotionItem.class)
public class PotionItemMixin {
  @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
  private void convertStoneToDeepslate(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
    if (SkyBlockSettings.doRenewableDeepslate) {
      World world = context.getWorld();
      BlockPos blockPos = context.getBlockPos();
      PlayerEntity playerEntity = context.getPlayer();
      ItemStack itemStack = context.getStack();
      BlockState blockState = world.getBlockState(blockPos);

      if (context.getSide() != Direction.DOWN && DeepslateConversionHelper.convertDeepslateAtPos(world, blockState, blockPos)) {
        world.playSound(null, blockPos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.PLAYERS, 1.0f, 1.0f);
        Objects.requireNonNull(playerEntity).setStackInHand(context.getHand(), ItemUsage.exchangeStack(itemStack, playerEntity, new ItemStack(Items.GLASS_BOTTLE)));
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
        cir.setReturnValue(ActionResult.success(world.isClient));
      }
    }
  }
}
