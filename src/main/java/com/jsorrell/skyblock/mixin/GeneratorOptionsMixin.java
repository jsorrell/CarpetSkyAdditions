package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.gen.SkyBlockGenerationSettings;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Properties;

@Mixin(GeneratorOptions.class)
public class GeneratorOptionsMixin {

  @Inject(
      method = "fromProperties",
      at = @At("RETURN"),
      locals = LocalCapture.CAPTURE_FAILHARD,
      cancellable = true)
  private static void addSkyBlockGeneratorOptionWhenLoadingProperties(
      DynamicRegistryManager drm,
      Properties properties,
      CallbackInfoReturnable<GeneratorOptions> cir,
      String string,
      String string2,
      boolean generateStructures,
      String string4,
      String generatorSettingsName,
      long seed
  ) {
    if (SkyBlockGenerationSettings.NAME.equals(generatorSettingsName)) {
      GeneratorOptions x = new GeneratorOptions(
          seed,
          generateStructures,
          false,
          SkyBlockGenerationSettings.getSkyBlockDimensionOptionsRegistry(drm, seed)
      );
      cir.setReturnValue(x);
    }
  }
}
