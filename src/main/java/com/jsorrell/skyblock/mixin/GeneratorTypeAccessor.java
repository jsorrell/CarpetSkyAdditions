package com.jsorrell.skyblock.mixin;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.world.GeneratorType;

@Environment(EnvType.CLIENT)
@Mixin(GeneratorType.class)
public interface GeneratorTypeAccessor {
  @Accessor("VALUES")
  static List<GeneratorType> getValues() {
    throw new AssertionError();
  }
}
