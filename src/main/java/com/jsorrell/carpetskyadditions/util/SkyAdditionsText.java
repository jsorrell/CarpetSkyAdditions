package com.jsorrell.carpetskyadditions.util;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public abstract class SkyAdditionsText {
    protected static Language language = Language.getInstance();

    public static MutableComponent translatable(String key) {
        return Component.translatableWithFallback(key, language.getOrDefault(key));
    }

    public static MutableComponent translatable(String key, Object... args) {
        return Component.translatableWithFallback(key, language.getOrDefault(key), args);
    }
}
