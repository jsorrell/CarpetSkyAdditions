package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record SkyAdditionsEntityPredicate(
        Optional<SkyAdditionsLocationPredicate> location, Optional<SkyAdditionsLocationPredicate> steppingOnLocation) {
    public static final Codec<SkyAdditionsEntityPredicate> CODEC =
            ExtraCodecs.recursive(codec -> RecordCodecBuilder.create(instance -> instance.group(
                            ExtraCodecs.strictOptionalField(SkyAdditionsLocationPredicate.CODEC, "location")
                                    .forGetter(SkyAdditionsEntityPredicate::location),
                            ExtraCodecs.strictOptionalField(SkyAdditionsLocationPredicate.CODEC, "stepping_on")
                                    .forGetter(SkyAdditionsEntityPredicate::steppingOnLocation))
                    .apply(instance, SkyAdditionsEntityPredicate::new)));

    public boolean matches(ServerLevel level, Vec3 position, Entity entity) {
        if (entity == null) return false;

        if (location.isPresent() && !location.get().matches(level, entity.getX(), entity.getY(), entity.getZ()))
            return false;

        if (steppingOnLocation.isPresent()) {
            Vec3 stepPos = Vec3.atCenterOf(entity.getOnPos());
            if (!steppingOnLocation.get().matches(level, stepPos.x(), stepPos.y(), stepPos.z())) {
                return false;
            }
        }
        return true;
    }
}
