package com.jsorrell.carpetskyadditions;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import com.mojang.serialization.Codec;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class SkyAdditionsDataComponents {
    public static final DataComponentType<Boolean> SWIFT_SNEAK_ENCHANTABLE_COMPONENT =
            register("swift_sneak_enchantable", builder -> builder.persistent(Codec.BOOL));

    public static void bootstrap() {}

    private static <T> DataComponentType<T> register(
            String string, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                new SkyAdditionsResourceLocation(string),
                unaryOperator.apply(DataComponentType.builder()).build());
    }
}
