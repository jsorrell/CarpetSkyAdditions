package com.jsorrell.carpetskyadditions.util;

import com.jsorrell.carpetskyadditions.SkyAdditionsExtension;
import net.minecraft.resources.ResourceLocation;

public class SkyAdditionsResourceLocation extends ResourceLocation {
    public static final String NAMESPACE = SkyAdditionsExtension.MOD_ID;

    public SkyAdditionsResourceLocation(String path) {
        super(NAMESPACE, path);
    }
}
