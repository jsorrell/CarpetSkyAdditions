package com.jsorrell.carpetskyadditions.util;

import com.jsorrell.carpetskyadditions.Build;

public class SkyAdditionsIdentifier extends net.minecraft.util.Identifier {
  public static final String NAMESPACE = Build.MODID;

  public SkyAdditionsIdentifier(String path) {
    super(NAMESPACE, path);
  }
}
