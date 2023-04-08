package com.jsorrell.carpetskyadditions.config;

import com.jsorrell.carpetskyadditions.SkyAdditionsExtension;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = SkyAdditionsExtension.MOD_ID)
public class SkyAdditionsConfig implements ConfigData {
    public enum InitialTreeType {
        OAK,
        ACACIA,
        ;

        @Override
        public String toString() {
            switch (this) {
                case OAK -> {
                    return "Oak";
                }
                case ACACIA -> {
                    return "Acacia";
                }
                default -> {
                    return null;
                }
            }
        }
    }

    public boolean defaultToSkyBlockWorld = false;
    public boolean enableDatapackByDefault = false;
    public String initialTreeType = InitialTreeType.OAK.toString();
    public boolean autoEnableDefaultSettings = true;

    private InitialTreeType parseInitialTreeType() throws ValidationException {
        switch (initialTreeType.toLowerCase()) {
            case "oak" -> {
                return InitialTreeType.OAK;
            }
            case "acacia" -> {
                return InitialTreeType.ACACIA;
            }
            default -> throw new ValidationException("Couldn't parse initialTreeType: " + initialTreeType);
        }
    }

    public InitialTreeType getInitialTreeType() {
        try {
            return parseInitialTreeType();
        } catch (ValidationException e) {
            throw new AssertionError("Invalid tree type");
        }
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        parseInitialTreeType();
    }
}
