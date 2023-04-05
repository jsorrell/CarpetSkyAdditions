package com.jsorrell.carpetskyadditions.gen;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

public class SkyAdditionsWorldPresets {
    public static final ResourceKey<WorldPreset> SKYBLOCK = preset("skyblock");

    private static ResourceKey<WorldPreset> preset(String path) {
        return ResourceKey.create(Registries.WORLD_PRESET, new SkyAdditionsIdentifier(path));
    }
}
