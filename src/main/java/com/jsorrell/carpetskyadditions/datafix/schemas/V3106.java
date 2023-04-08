package com.jsorrell.carpetskyadditions.datafix.schemas;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class V3106 extends NamespacedSchema {
    public V3106(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    @Override
    public void registerTypes(
            Schema schema,
            Map<String, Supplier<TypeTemplate>> entityTypes,
            Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        super.registerTypes(schema, entityTypes, blockEntityTypes);
        Supplier<TypeTemplate> noiseTemplate = () -> DSL.optionalFields(
                "biome_source",
                DSL.taggedChoiceLazy(
                        "type",
                        DSL.string(),
                        ImmutableMap.of(
                                "minecraft:fixed",
                                () -> DSL.fields("biome", References.BIOME.in(schema)),
                                "minecraft:multi_noise",
                                () -> DSL.or(
                                        DSL.fields("preset", namespacedString().template()),
                                        DSL.list(DSL.fields("biome", References.BIOME.in(schema)))),
                                "minecraft:checkerboard",
                                () -> DSL.fields("biomes", DSL.list(References.BIOME.in(schema))),
                                "minecraft:the_end",
                                DSL::remainder)),
                "settings",
                DSL.or(
                        DSL.constType(DSL.string()),
                        DSL.optionalFields(
                                "default_block",
                                References.BLOCK_NAME.in(schema),
                                "default_fluid",
                                References.BLOCK_NAME.in(schema))));

        // Add SkyBlock with same TypeTemplate as noise
        schema.registerType(
                false,
                References.WORLD_GEN_SETTINGS,
                () -> DSL.fields(
                        "dimensions",
                        DSL.compoundList(
                                DSL.constType(namespacedString()),
                                DSL.fields(
                                        "generator",
                                        DSL.taggedChoiceLazy(
                                                "type",
                                                DSL.string(),
                                                ImmutableMap.of(
                                                        "minecraft:debug",
                                                        DSL::remainder,
                                                        "minecraft:flat",
                                                        () -> DSL.optionalFields(
                                                                "settings",
                                                                DSL.optionalFields(
                                                                        "biome",
                                                                        References.BIOME.in(schema),
                                                                        "layers",
                                                                        DSL.list(
                                                                                DSL.optionalFields(
                                                                                        "block",
                                                                                        References.BLOCK_NAME.in(
                                                                                                schema))))),
                                                        "minecraft:noise",
                                                        noiseTemplate,
                                                        "carpetskyadditions:skyblock",
                                                        noiseTemplate))))));
    }
}
