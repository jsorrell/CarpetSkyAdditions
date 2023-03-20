package com.jsorrell.carpetskyadditions.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface SkyAdditionsSetting {
    String value() default "";

    Class<? extends SettingFixer>[] fixer() default {};
}
