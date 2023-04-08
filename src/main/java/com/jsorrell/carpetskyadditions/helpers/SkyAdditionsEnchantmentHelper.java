package com.jsorrell.carpetskyadditions.helpers;

public class SkyAdditionsEnchantmentHelper {
    public static final double MAX_WARDEN_DISTANCE_FOR_SWIFT_SNEAK = 8.0;
    public static final String SWIFT_SNEAK_ENCHANTABLE_TAG = "CarpetSkySwiftSneakEnchantable";

    public static int getSwiftSneakMinCost(int level) {
        return 14 + level * 7;
    }

    public static int getSwiftSneakMaxCost(int level) {
        return getSwiftSneakMinCost(level) + 50;
    }
}
