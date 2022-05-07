package com.jsorrell.skyblock.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkyBlockSetting {
  String value() default "";

  Class<? extends SettingFixer>[] fixer() default {};
}
