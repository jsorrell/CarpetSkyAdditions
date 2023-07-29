package com.jsorrell.carpetskyadditions.advancements.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class DesertPyramidExistsCondition implements LootItemCondition {
    static List<Block> BLOCKS_TO_CHECK = List.of(
            Blocks.BLUE_TERRACOTTA,
            Blocks.ORANGE_TERRACOTTA,
            Blocks.SANDSTONE,
            Blocks.CUT_SANDSTONE,
            Blocks.CHISELED_SANDSTONE,
            Blocks.SANDSTONE_STAIRS,
            Blocks.SANDSTONE_SLAB);
    final LootContext.EntityTarget entityTarget;

    DesertPyramidExistsCondition(LootContext.EntityTarget entityTarget) {
        this.entityTarget = entityTarget;
    }

    @Override
    public LootItemConditionType getType() {
        return SkyAdditionsLootItemConditions.DESERT_PYRAMID_EXISTS;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(this.entityTarget.getParam());
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.getParamOrNull(this.entityTarget.getParam());
        if (entity == null) return false;
        Level level = entity.level();
        if (!(level instanceof ServerLevel serverLevel)) return false;

        BlockPos blueTerracottaPos = entity.blockPosition().below();
        StructureTemplate template = serverLevel
                .getServer()
                .getStructureManager()
                .get(new SkyAdditionsResourceLocation("desert_pyramid"))
                .orElseThrow();
        BlockPos centerOffset = new BlockPos(
                template.getSize().getX() / 2, 0, template.getSize().getZ() / 2);
        BlockPos structureOrigin = blueTerracottaPos.subtract(centerOffset);

        StructurePlaceSettings placeSettings = new StructurePlaceSettings().setRotationPivot(centerOffset);

        return Arrays.stream(Rotation.values()).anyMatch(r -> {
            placeSettings.setRotation(r);

            for (Block block : BLOCKS_TO_CHECK) {
                List<StructureTemplate.StructureBlockInfo> requiredBlocks =
                        template.filterBlocks(structureOrigin, placeSettings, block);
                for (StructureTemplate.StructureBlockInfo requiredBlock : requiredBlocks) {
                    if (requiredBlock.state() != level.getBlockState(requiredBlock.pos())) {
                        // Use terracotta to determine location and rotation
                        if (!requiredBlock.state().is(BlockTags.TERRACOTTA)) {
                            // Help people debug builds
                            SkyAdditionsSettings.LOG.warn(
                                    "Incorrect Block at " + requiredBlock.pos() + " in Desert Pyramid");
                        }
                        return false;
                    }
                }
            }
            return true;
        });
    }

    public static class Serializer
            implements net.minecraft.world.level.storage.loot.Serializer<DesertPyramidExistsCondition> {
        public void serialize(
                JsonObject jsonObject,
                DesertPyramidExistsCondition desertPyramidExistsCondition,
                JsonSerializationContext jsonSerializationContext) {
            jsonObject.add("entity", jsonSerializationContext.serialize(desertPyramidExistsCondition.entityTarget));
        }

        public DesertPyramidExistsCondition deserialize(
                JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return new DesertPyramidExistsCondition(GsonHelper.getAsObject(
                    jsonObject, "entity", jsonDeserializationContext, LootContext.EntityTarget.class));
        }
    }
}
