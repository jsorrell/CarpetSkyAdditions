package com.jsorrell.carpetskyadditions.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

public abstract class SkyAdditionsText {
    protected static Language language = Language.getInstance();

    public static MutableText translatable(String key) {
        return Text.translatableWithFallback(key, language.get(key));
    }

    public static MutableText translatable(String key, Object... args) {
        return Text.translatableWithFallback(key, language.get(key), args);
    }
}
