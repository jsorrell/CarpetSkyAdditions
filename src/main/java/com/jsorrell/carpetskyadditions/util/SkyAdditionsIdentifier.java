package com.jsorrell.carpetskyadditions.util;

import com.jsorrell.carpetskyadditions.SkyAdditionsExtension;
import net.minecraft.resources.ResourceLocation;

public class SkyAdditionsIdentifier extends ResourceLocation {
    public static final String NAMESPACE = SkyAdditionsExtension.MOD_ID;

    public SkyAdditionsIdentifier(String path) {
        super(NAMESPACE, path);
    }
}
