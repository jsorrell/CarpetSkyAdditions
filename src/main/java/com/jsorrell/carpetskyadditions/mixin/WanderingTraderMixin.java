package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.fakes.CamelInterface;
import com.jsorrell.carpetskyadditions.helpers.TraderCamelHelper;
import com.jsorrell.carpetskyadditions.helpers.WanderingTraderHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin extends AbstractVillager {
    @Shadow
    protected abstract void registerGoals();

    public WanderingTraderMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    @SuppressWarnings("ConstantConditions")
    private WanderingTrader asTrader() {
        if ((AbstractVillager) this instanceof WanderingTrader wanderingTrader) {
            return wanderingTrader;
        } else {
            throw new AssertionError("Not wandering trader");
        }
    }

    @Redirect(
            method = "updateTrades",
            at =
                    @At(
                            value = "FIELD",
                            opcode = Opcodes.GETSTATIC,
                            target =
                                    "Lnet/minecraft/world/entity/npc/VillagerTrades;WANDERING_TRADER_TRADES:Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;"))
    private Int2ObjectMap<VillagerTrades.ItemListing[]> getTrades() {
        return WanderingTraderHelper.getTrades();
    }

    @Override
    public double getMyRidingOffset() {
        return TraderCamelHelper.isMountedTrader(asTrader()) ? -0.45 : super.getMyRidingOffset();
    }

    @Override
    public void remove(RemovalReason reason) {
        Camel traderCamel = TraderCamelHelper.getTraderCamel(asTrader());
        if (traderCamel != null) {
            // Despawn trader camel when trader despawns
            if (reason == RemovalReason.DISCARDED) {
                traderCamel.discard();
            }
        }
        super.remove(reason);
    }

    @Override
    public boolean startRiding(Entity vehicle, boolean force) {
        boolean success = super.startRiding(vehicle, force);
        if (success) {
            Camel traderCamel = TraderCamelHelper.getTraderCamel(asTrader());
            if (traderCamel != null) {
                ((CamelInterface) traderCamel).carpetSkyAdditions$makeTraderCamel();
                reregisterGoalsForMountedTrader();
            }
        }
        return success;
    }

    @Override
    public void stopRiding() {
        Camel traderCamel = TraderCamelHelper.getTraderCamel(asTrader());
        super.stopRiding();
        if (traderCamel != null) {
            ((CamelInterface) traderCamel).carpetSkyAdditions$makeStandaloneCamel();
            reregisterGoalsForUnmountedTrader();
        }
    }

    // Motion speeds are a multiple of the goal speed and the entity speed.
    // The camel's base speed assumes goal speed much faster than what the trader's base speed assumes.
    // In order for the trader camel to move at a reasonable speed when mounted,
    // we need to boost the trader's goal speeds significantly.
    @Unique
    private void reregisterGoalsForMountedTrader() {
        goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
        double s = 8.0;
        goalSelector.removeAllGoals(g -> true);

        // Goals
        goalSelector.addGoal(
                0,
                new UseItemGoal<>(
                        this,
                        PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY),
                        SoundEvents.WANDERING_TRADER_DISAPPEARED,
                        wanderingTrader -> this.level().isNight() && !wanderingTrader.isInvisible()));
        goalSelector.addGoal(
                0,
                new UseItemGoal<>(
                        this,
                        new ItemStack(Items.MILK_BUCKET),
                        SoundEvents.WANDERING_TRADER_REAPPEARED,
                        wanderingTrader -> this.level().isDay() && wanderingTrader.isInvisible()));
        goalSelector.addGoal(1, new TraderCamelHelper.TradeWithPlayerWhileMountedGoal(this));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zombie.class, 8.0F, 0.5 * s, 0.5 * s));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Evoker.class, 12.0F, 0.5 * s, 0.5 * s));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vindicator.class, 8.0F, 0.5 * s, 0.5 * s));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vex.class, 8.0F, 0.5 * s, 0.5 * s));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Pillager.class, 15.0F, 0.5 * s, 0.5 * s));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Illusioner.class, 12.0F, 0.5 * s, 0.5 * s));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zoglin.class, 10.0F, 0.5 * s, 0.5 * s));
        goalSelector.addGoal(1, new PanicGoal(this, 0.5 * s));
        goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        goalSelector.addGoal(2, new TraderCamelHelper.MountedTraderWanderToPositionGoal(asTrader(), 2.0, 0.35 * s));
        goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35 * s));
        goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35 * s));
        goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        // Remove this to stop the trader staring at the camel
        // goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    @Unique
    private void reregisterGoalsForUnmountedTrader() {
        goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
        goalSelector.removeAllGoals(g -> true);
        registerGoals();
    }
}
