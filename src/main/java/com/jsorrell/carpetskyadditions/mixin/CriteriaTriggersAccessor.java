package com.jsorrell.carpetskyadditions.mixin;

import com.google.common.collect.BiMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CriteriaTriggers.class)
public interface CriteriaTriggersAccessor {
    @Accessor("CRITERIA")
    static BiMap<ResourceLocation, CriterionTrigger<?>> getCriteria() {
        throw new AssertionError();
    }
}
