package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import com.jsorrell.carpetskyadditions.gen.SkyAdditionsWorldPresets;
import com.mojang.datafixers.util.Pair;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(value = EnvType.CLIENT)
@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
  @Redirect(method = "create(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;)V", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/world/gen/WorldPresets;DEFAULT:Lnet/minecraft/util/registry/RegistryKey;"))
  private static RegistryKey<WorldPreset> setDefaultSelectedWorldPreset() {
    SkyAdditionsConfig config = AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
    return config.defaultToSkyBlockWorld ? SkyAdditionsWorldPresets.SKYBLOCK : WorldPresets.DEFAULT;
  }

  @Redirect(method = "method_41854", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Pair;of(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;"))
  private static Pair<GeneratorOptions, DynamicRegistryManager.Immutable> setDefaultGeneratorOptions(Object first, Object second) {
    if (!(first instanceof GeneratorOptions defaultGeneratorOptions && second instanceof DynamicRegistryManager.Immutable drm))
      throw new AssertionError("Unknown redirection -- check mixin");

    GeneratorOptions generatorOptions = defaultGeneratorOptions;
    SkyAdditionsConfig config = AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
    if (config.defaultToSkyBlockWorld) {
      generatorOptions = drm.get(Registry.WORLD_PRESET_KEY).entryOf(SkyAdditionsWorldPresets.SKYBLOCK).value().createGeneratorOptions(Random.create().nextLong(), true, false);
    }
    return Pair.of(generatorOptions, drm);
  }
}
