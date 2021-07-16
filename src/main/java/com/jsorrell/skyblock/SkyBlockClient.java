package com.jsorrell.skyblock;

import net.fabricmc.api.ClientModInitializer;

import com.jsorrell.skyblock.gen.SkyBlockGeneratorTypes;
import com.jsorrell.skyblock.mixin.GeneratorTypeAccessor;

public class SkyBlockClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    GeneratorTypeAccessor.getValues().add(SkyBlockGeneratorTypes.SKYBLOCK);
  }
}
