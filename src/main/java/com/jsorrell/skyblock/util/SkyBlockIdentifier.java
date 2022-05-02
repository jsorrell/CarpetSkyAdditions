package com.jsorrell.skyblock.util;

public class SkyBlockIdentifier extends net.minecraft.util.Identifier {
  public static final String NAMESPACE = "skyblock";

  public SkyBlockIdentifier(String id) {
    super(NAMESPACE, id);
  }
}
