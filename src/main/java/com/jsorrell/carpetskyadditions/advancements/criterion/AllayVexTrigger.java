package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
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
                        ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
                                .forGetter(TriggerInstance::player),
                        ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "vex")
                                .forGetter(TriggerInstance::vex),
                        ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "allay")
                                .forGetter(TriggerInstance::allay))
                .apply(instance, TriggerInstance::new));

        public boolean matches(LootContext vexContext, LootContext allayContext) {
            return (vex.isEmpty() || vex.get().matches(vexContext))
                    && (allay.isEmpty() || allay.get().matches(allayContext));
        }
    }
}
