package com.jsorrell.carpetskyadditions.tags;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class SkyAdditionsBiomeTags {
    public static final TagKey<Biome> WANDERING_TRADER_SPAWNS_ON_CAMEL = create("wandering_trader_spawns_on_camel");

    private static TagKey<Biome> create(String name) {
        return TagKey.create(Registries.BIOME, new SkyAdditionsResourceLocation(name));
    }
}
