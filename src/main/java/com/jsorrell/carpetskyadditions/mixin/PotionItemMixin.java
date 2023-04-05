package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeepslateConversionHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(method = "useOn", at = @At("TAIL"), cancellable = true)
    private void convertStoneToDeepslate(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (SkyAdditionsSettings.doRenewableDeepslate) {
            Level world = context.getLevel();
            BlockPos blockPos = context.getClickedPos();
            Player playerEntity = context.getPlayer();
            ItemStack itemStack = context.getItemInHand();

            if (context.getClickedFace() != Direction.DOWN
                    && DeepslateConversionHelper.convertDeepslateWithBottle(world, blockPos, blockPos)) {
                world.playSound(null, blockPos, SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0f, 1.0f);
                Objects.requireNonNull(playerEntity)
                        .setItemInHand(
                                context.getHand(),
                                ItemUtils.createFilledResult(
                                        itemStack, playerEntity, new ItemStack(Items.GLASS_BOTTLE)));
                playerEntity.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
            }
        }
    }
}
