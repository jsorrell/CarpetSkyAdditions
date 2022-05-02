package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.datafixer.Schema3079;
import com.jsorrell.skyblock.datafixer.SkyBlockGeneratorNameFix;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.datafixer.Schemas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Schemas.class)
public abstract class SchemasMixin {
  @Inject(
    method = "build",
    at = @At(value = "INVOKE",
      target = "Lcom/mojang/datafixers/DataFixerBuilder;addFixer(Lcom/mojang/datafixers/DataFix;)V",
      ordinal = 225)
  )
  private static void addSkyBlockGeneratorNameFix(DataFixerBuilder builder, CallbackInfo ci) {
    Schema schema3079 = builder.addSchema(3079, 1, Schema3079::new);
    builder.addFixer(new SkyBlockGeneratorNameFix(schema3079));
  }
}
