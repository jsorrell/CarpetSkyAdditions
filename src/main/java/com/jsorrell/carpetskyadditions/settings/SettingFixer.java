package com.jsorrell.carpetskyadditions.settings;

import javax.annotation.Nullable;

public abstract class SettingFixer {
  @Nullable
  public String[] alternateNames() {
    return null;
  }

  @Nullable
  public abstract FieldPair fix(FieldPair currentVal);
}
