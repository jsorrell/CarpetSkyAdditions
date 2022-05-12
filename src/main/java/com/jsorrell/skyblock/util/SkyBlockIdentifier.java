package com.jsorrell.skyblock.util;

import com.jsorrell.skyblock.Build;

public class SkyBlockIdentifier extends net.minecraft.util.Identifier {
  public static final String NAMESPACE = Build.MODID;

  public SkyBlockIdentifier(String path) {
    super(NAMESPACE, path);
  }
}
