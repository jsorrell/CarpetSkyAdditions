package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeepslateConversionHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.Objects;
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

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void convertStoneToDeepslate(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (SkyAdditionsSettings.doRenewableDeepslate) {
            World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();
            PlayerEntity playerEntity = context.getPlayer();
            ItemStack itemStack = context.getStack();

            if (context.getSide() != Direction.DOWN
                    && DeepslateConversionHelper.convertDeepslateWithBottle(world, blockPos, blockPos)) {
                world.playSound(null, blockPos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.PLAYERS, 1.0f, 1.0f);
                Objects.requireNonNull(playerEntity)
                        .setStackInHand(
                                context.getHand(),
                                ItemUsage.exchangeStack(itemStack, playerEntity, new ItemStack(Items.GLASS_BOTTLE)));
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
                cir.setReturnValue(ActionResult.success(world.isClient));
            }
        }
    }
}
