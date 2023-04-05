package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DolphinFindHeartGoal;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Dolphin.class)
public abstract class DolphinEntityMixin extends WaterAnimal {
    protected DolphinEntityMixin(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @SuppressWarnings("ConstantConditions")
    private Dolphin asDolphin() {
        if ((WaterAnimal) this instanceof Dolphin dolphin) {
            return dolphin;
        } else {
            throw new AssertionError("Not dolphin");
        }
    }

    @ModifyArg(
            method = "registerGoals",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",
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
