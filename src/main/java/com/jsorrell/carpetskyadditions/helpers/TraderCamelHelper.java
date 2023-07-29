package com.jsorrell.carpetskyadditions.helpers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jsorrell.carpetskyadditions.mixin.WanderingTraderAccessor;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.tags.SkyAdditionsBiomeTags;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.camel.CamelAi;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TraderCamelHelper {
    public static boolean tradersRideCamelsAt(Level level, BlockPos pos) {
        return SkyAdditionsSettings.traderCamels
                && level.getBiome(pos).is(SkyAdditionsBiomeTags.WANDERING_TRADER_SPAWNS_ON_CAMEL);
    }

    public static boolean isMountedTrader(WanderingTrader trader) {
        return getTraderCamel(trader) != null;
    }

    public static Camel getTraderCamel(WanderingTrader trader) {
        if (trader.getControlledVehicle() instanceof Camel camel && SkyAdditionsSettings.traderCamels) {
            return camel;
        }
        return null;
    }

    public static boolean isTraderCamel(Camel camel) {
        return camel.getControllingPassenger() instanceof WanderingTrader && SkyAdditionsSettings.traderCamels;
    }

    public static class MountedTraderWanderToPositionGoal extends Goal {
        final WanderingTrader trader;
        final double stopDistance;
        final double speedModifier;

        public MountedTraderWanderToPositionGoal(WanderingTrader trader, double stopDistance, double speedModifier) {
            this.trader = trader;
            this.stopDistance = stopDistance;
            this.speedModifier = speedModifier;
            setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public void stop() {
            trader.setWanderTarget(null);
            trader.getNavigation().stop();
        }

        @Override
        public boolean canUse() {
            BlockPos target = ((WanderingTraderAccessor) trader).getWanderTarget();
            return target != null && isTooFarAway(target, stopDistance);
        }

        @Override
        public void tick() {
            BlockPos target = ((WanderingTraderAccessor) trader).getWanderTarget();
            if (target != null && trader.getNavigation().isDone()) {
                if (isTooFarAway(target, 10.0)) {
                    Vec3 directionTowardTarget = Vec3.atLowerCornerOf(target)
                            .subtract(trader.position())
                            .normalize();
                    Vec3 partialTarget = directionTowardTarget.scale(10.0).add(trader.position());
                    trader.getNavigation().moveTo(partialTarget.x, partialTarget.y, partialTarget.z, speedModifier);
                } else {
                    trader.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), speedModifier);
                }
            }
        }

        private boolean isTooFarAway(BlockPos pos, double distance) {
            return !pos.closerToCenterThan(trader.position(), distance);
        }
    }

    public static class TradeWithPlayerWhileMountedGoal extends TradeWithPlayerGoal {
        protected AbstractVillager villager;

        public TradeWithPlayerWhileMountedGoal(AbstractVillager villager) {
            super(villager);
            this.villager = villager;
        }

        @Override
        public boolean canUse() {
            if (!villager.isAlive()) {
                return false;
            } else if (villager.isInWater()) {
                return false;
            }

            Entity vehicle = villager.getVehicle();
            if (vehicle == null) {
                if (!villager.onGround()) return false;
            } else {
                if (!vehicle.onGround()) return false;
            }

            if (villager.hurtMarked) {
                return false;
            } else {
                Player player = villager.getTradingPlayer();
                if (player == null) {
                    return false;
                } else if (villager.distanceToSqr(player) > 16.0) {
                    return false;
                } else {
                    return player.containerMenu != null;
                }
            }
        }
    }

    public static class TraderCamelAI {
        public static Brain<?> makeBrain(Brain<Camel> brain) {
            initCoreActivity(brain);
            brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
            brain.useDefaultActivity();
            return brain;
        }

        private static void initCoreActivity(Brain<Camel> brain) {
            brain.addActivity(
                    Activity.CORE,
                    0,
                    ImmutableList.of(
                            new Swim(0.8F),
                            new CamelAi.CamelPanic(4.0F),
                            new LookAtTargetSink(45, 90),
                            new MoveToTargetSink()));
        }
    }
}
