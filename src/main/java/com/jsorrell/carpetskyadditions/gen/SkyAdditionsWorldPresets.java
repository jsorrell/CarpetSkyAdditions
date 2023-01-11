package com.jsorrell.carpetskyadditions.gen;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.WorldPreset;

public class SkyAdditionsWorldPresets {
  public static final RegistryKey<WorldPreset> SKYBLOCK = RegistryKey.of(RegistryKeys.WORLD_PRESET, new SkyAdditionsIdentifier("skyblock"));
}
