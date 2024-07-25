package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.storage.loot.LootContext;

public class AllayVexTrigger extends SimpleCriterionTrigger<AllayVexTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Vex vex, Allay allay) {
        LootContext vexLootContext = EntityPredicate.createContext(player, vex);
        LootContext allayLootContext = EntityPredicate.createContext(player, allay);
        trigger(player, triggerInstance -> triggerInstance.matches(vexLootContext, allayLootContext));
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<ContextAwarePredicate> vex,
            Optional<ContextAwarePredicate> allay)
            implements SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC
                                .optionalFieldOf("player")
                                .forGetter(TriggerInstance::player),
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("vex").forGetter(TriggerInstance::vex),
                        EntityPredicate.ADVANCEMENT_CODEC
                                .optionalFieldOf("allay")
                                .forGetter(TriggerInstance::allay))
                .apply(instance, TriggerInstance::new));

        public boolean matches(LootContext vexContext, LootContext allayContext) {
            return (vex.isEmpty() || vex.get().matches(vexContext))
                    && (allay.isEmpty() || allay.get().matches(allayContext));
        }
    }
}
