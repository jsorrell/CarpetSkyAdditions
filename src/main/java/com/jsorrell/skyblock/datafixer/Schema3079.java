package com.jsorrell.skyblock.datafixer;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;

import java.util.Map;
import java.util.function.Supplier;

public class Schema3079 extends IdentifierNormalizingSchema {
  public Schema3079(int versionKey, Schema parent) {
    super(versionKey, parent);
  }

  @Override
  public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
    super.registerTypes(schema, entityTypes, blockEntityTypes);
    schema.registerType(false, TypeReferences.WORLD_GEN_SETTINGS, () -> {
      return DSL.fields("dimensions", DSL.compoundList(DSL.constType(getIdentifierType()), DSL.fields("generator", DSL.taggedChoiceLazy("type", DSL.string(), ImmutableMap.of("minecraft:debug", DSL::remainder, "minecraft:flat", () -> {
        return DSL.optionalFields("settings", DSL.optionalFields("biome", TypeReferences.BIOME.in(schema), "layers", DSL.list(DSL.optionalFields("block", TypeReferences.BLOCK_NAME.in(schema)))));
      }, "minecraft:noise", () -> {
        return DSL.optionalFields("biome_source", DSL.taggedChoiceLazy("type", DSL.string(), ImmutableMap.of("minecraft:fixed", () -> {
          return DSL.fields("biome", TypeReferences.BIOME.in(schema));
        }, "minecraft:multi_noise", () -> {
          return DSL.or(DSL.fields("preset", getIdentifierType().template()), DSL.list(DSL.fields("biome", TypeReferences.BIOME.in(schema))));
        }, "minecraft:checkerboard", () -> {
          return DSL.fields("biomes", DSL.list(TypeReferences.BIOME.in(schema)));
        }, "minecraft:the_end", DSL::remainder)), "settings", DSL.or(DSL.constType(DSL.string()), DSL.optionalFields("default_block", TypeReferences.BLOCK_NAME.in(schema), "default_fluid", TypeReferences.BLOCK_NAME.in(schema))));
      }, "skyblock:skyblock", () -> { // Replace minecraft:skyblock with skyblock:skyblock
        return DSL.optionalFields("biome_source", DSL.taggedChoiceLazy("type", DSL.string(), ImmutableMap.of("minecraft:fixed", () -> {
          return DSL.fields("biome", TypeReferences.BIOME.in(schema));
        }, "minecraft:multi_noise", () -> {
          return DSL.or(DSL.fields("preset", getIdentifierType().template()), DSL.list(DSL.fields("biome", TypeReferences.BIOME.in(schema))));
        }, "minecraft:checkerboard", () -> {
          return DSL.fields("biomes", DSL.list(TypeReferences.BIOME.in(schema)));
        }, "minecraft:the_end", DSL::remainder)), "settings", DSL.or(DSL.constType(DSL.string()), DSL.optionalFields("default_block", TypeReferences.BLOCK_NAME.in(schema), "default_fluid", TypeReferences.BLOCK_NAME.in(schema))));
      })))));
    });
  }
}
