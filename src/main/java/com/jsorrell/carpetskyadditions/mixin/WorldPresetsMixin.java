package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.gen.SkyAdditionsWorldPresets;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldPresets.class)
public class WorldPresetsMixin {
  @Redirect(method = "createDefaultOptions(Lnet/minecraft/util/registry/DynamicRegistryManager;JZZ)Lnet/minecraft/world/gen/GeneratorOptions;", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/world/gen/WorldPresets;DEFAULT:Lnet/minecraft/util/registry/RegistryKey;"))
  private static RegistryKey<WorldPreset> setDefaultWorldPreset() {
    return SkyAdditionsWorldPresets.getInitiallySelectedPreset();
  }
}
