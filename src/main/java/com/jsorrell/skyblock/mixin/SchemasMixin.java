package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.datafixer.Schema2551;
import com.jsorrell.skyblock.datafixer.Schema2832;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.datafixer.Schemas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BiFunction;

@Mixin(Schemas.class)
public abstract class SchemasMixin {
  @ModifyArg(
    method = "build",
    at = @At(value = "INVOKE",
      target = "Lcom/mojang/datafixers/DataFixerBuilder;addSchema(ILjava/util/function/BiFunction;)Lcom/mojang/datafixers/schemas/Schema;",
      ordinal = 112),
    index = 1
  )
  private static BiFunction<Integer, Schema, Schema> replaceSchema2551(BiFunction<Integer, Schema, Schema> factory) {
    return Schema2551::new;
  }

  @ModifyArg(
    method = "build",
    at = @At(value = "INVOKE",
      target = "Lcom/mojang/datafixers/DataFixerBuilder;addSchema(ILjava/util/function/BiFunction;)Lcom/mojang/datafixers/schemas/Schema;",
      ordinal = 136),
    index = 1
  )
  private static BiFunction<Integer, Schema, Schema> replaceSchema2832(BiFunction<Integer, Schema, Schema> factory) {
    return Schema2832::new;
  }
}
