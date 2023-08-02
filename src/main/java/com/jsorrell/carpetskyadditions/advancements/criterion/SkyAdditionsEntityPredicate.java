package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SkyAdditionsEntityPredicate {
    public static final SkyAdditionsEntityPredicate ANY =
            new SkyAdditionsEntityPredicate(SkyAdditionsLocationPredicate.ANY, SkyAdditionsLocationPredicate.ANY);
    private final SkyAdditionsLocationPredicate location;
    private final SkyAdditionsLocationPredicate steppingOnLocation;

    private SkyAdditionsEntityPredicate(
            SkyAdditionsLocationPredicate location, SkyAdditionsLocationPredicate steppingOnLocation) {
        this.location = location;
        this.steppingOnLocation = steppingOnLocation;
    }

    public boolean matches(ServerLevel level, Vec3 position, Entity entity) {
        if (this == ANY) return true;
        if (entity == null) return false;

        if (!location.matches(level, entity.getX(), entity.getY(), entity.getZ())) return false;

        if (steppingOnLocation != SkyAdditionsLocationPredicate.ANY) {
            Vec3 stepPos = Vec3.atCenterOf(entity.getOnPos());
            if (!steppingOnLocation.matches(level, stepPos.x(), stepPos.y(), stepPos.z())) {
                return false;
            }
        }
        return true;
    }

    public static SkyAdditionsEntityPredicate fromJson(JsonElement json) {
        if (json == null || json.isJsonNull()) return ANY;

        JsonObject jsonObject = GsonHelper.convertToJsonObject(json, "entity");
        SkyAdditionsLocationPredicate locationPredicate =
                SkyAdditionsLocationPredicate.fromJson(jsonObject.get("location"));
        SkyAdditionsLocationPredicate locationPredicate2 =
                SkyAdditionsLocationPredicate.fromJson(jsonObject.get("stepping_on"));
        return new SkyAdditionsEntityPredicate(locationPredicate, locationPredicate2);
    }

    public JsonElement serializeToJson() {
        if (this == ANY) return JsonNull.INSTANCE;

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("location", location.serializeToJson());
        jsonObject.add("stepping_on", steppingOnLocation.serializeToJson());
        return jsonObject;
    }
}
