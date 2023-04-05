package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.criterion.SkyAdditionsCriteria;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Spider.class)
public abstract class SpiderEntityMixin extends Monster {

    protected SpiderEntityMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @SuppressWarnings("ConstantConditions")
    private Spider asSpider() {
        if ((Monster) this instanceof Spider spider) {
            return spider;
        } else {
            throw new AssertionError("Not spider");
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (SkyAdditionsSettings.poisonousPotatoesConvertSpiders) {
            ItemStack handStack = player.getItemInHand(hand);
            if (handStack.is(Items.POISONOUS_POTATO) && this.getType() == EntityType.SPIDER) {
                if (!player.getAbilities().instabuild) {
                    handStack.shrink(1);
                }

                CaveSpider caveSpider = this.convertTo(EntityType.CAVE_SPIDER, true);
                if (caveSpider == null) {
                    return InteractionResult.SUCCESS;
                }

                // Copy status effects
                this.getActiveEffects().forEach(caveSpider::addEffect);
                // Add particles
                caveSpider.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));

                if (player instanceof ServerPlayer serverPlayer) {
                    SkyAdditionsCriteria.CONVERT_SPIDER.trigger(serverPlayer, this.asSpider(), caveSpider);
                }
                this.playSound(
                        SoundEvents.ZOMBIE_VILLAGER_CURE,
                        1.0f + this.random.nextFloat(),
                        this.random.nextFloat() * 0.7f + 0.3f);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }
}
