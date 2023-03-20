package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.criterion.SkyAdditionsCriteria;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpiderEntity.class)
public abstract class SpiderEntityMixin extends HostileEntity {

    protected SpiderEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @SuppressWarnings("ConstantConditions")
    private SpiderEntity asSpider() {
        if ((HostileEntity) this instanceof SpiderEntity spider) {
            return spider;
        } else {
            throw new AssertionError("Not spider");
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (SkyAdditionsSettings.poisonousPotatoesConvertSpiders) {
            ItemStack handStack = player.getStackInHand(hand);
            if (handStack.isOf(Items.POISONOUS_POTATO) && this.getType() == EntityType.SPIDER) {
                if (!player.getAbilities().creativeMode) {
                    handStack.decrement(1);
                }

                CaveSpiderEntity caveSpider = this.convertTo(EntityType.CAVE_SPIDER, true);
                if (caveSpider == null) {
                    return ActionResult.SUCCESS;
                }

                // Copy status effects
                this.getActiveStatusEffects().values().forEach(caveSpider::addStatusEffect);
                // Add particles
                caveSpider.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));

                if (player instanceof ServerPlayerEntity serverPlayer) {
                    SkyAdditionsCriteria.CONVERT_SPIDER.trigger(serverPlayer, this.asSpider(), caveSpider);
                }
                this.playSound(
                        SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE,
                        1.0f + this.random.nextFloat(),
                        this.random.nextFloat() * 0.7f + 0.3f);
                return ActionResult.SUCCESS;
            }
        }
        return super.interactMob(player, hand);
    }
}
