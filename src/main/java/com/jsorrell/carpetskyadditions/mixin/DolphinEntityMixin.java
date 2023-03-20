package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DolphinFindHeartGoal;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DolphinEntity.class)
public abstract class DolphinEntityMixin extends WaterCreatureEntity {
    protected DolphinEntityMixin(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    @SuppressWarnings("ConstantConditions")
    private DolphinEntity asDolphin() {
        if ((WaterCreatureEntity) this instanceof DolphinEntity dolphin) {
            return dolphin;
        } else {
            throw new AssertionError("Not dolphin");
        }
    }

    @ModifyArg(
            method = "initGoals",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
                            ordinal = 2),
            index = 1)
    private Goal replaceTreasureGoal(Goal findTreasureGoal) {
        if (SkyAdditionsSettings.renewableHeartsOfTheSea) {
            return new DolphinFindHeartGoal(this.asDolphin());
        } else {
            return findTreasureGoal;
        }
    }
}
