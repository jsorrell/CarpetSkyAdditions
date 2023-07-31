package com.jsorrell.carpetskyadditions.advancements.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import java.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.AABB;

public class DesertPyramidExistsCondition implements LootItemCondition {
    static final List<Block> BLOCKS_TO_CHECK = List.of(
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
                    BlockState requiredState = requiredBlock.state();
                    BlockState currentState = level.getBlockState(requiredBlock.pos());
                    if (currentState != requiredState) {
                        // Use terracotta to determine location and rotation
                        if (!requiredState.is(BlockTags.TERRACOTTA)) {
                            // Help players within 10 blocks of bounding box debug builds
                            AABB buildersBox = AABB.of(template.getBoundingBox(placeSettings, structureOrigin)
                                    .inflatedBy(10));
                            List<ServerPlayer> playersToNotify = serverLevel.getPlayers(
                                    serverPlayer -> buildersBox.contains(serverPlayer.position()));
                            MutableComponent message;
                            if (currentState.getBlock() == requiredState.getBlock()) {
                                Map.Entry<Property<?>, Comparable<?>> incorrectProperty =
                                        requiredState.getValues().entrySet().stream()
                                                .filter(e -> currentState.getValue(e.getKey()) != e.getValue())
                                                .findAny()
                                                .orElseThrow();
                                message = Component.translatable(
                                        "message.desert_pyramid_incorrect_state",
                                        requiredBlock.pos().getX(),
                                        requiredBlock.pos().getY(),
                                        requiredBlock.pos().getZ(),
                                        incorrectProperty.getKey().getName(),
                                        incorrectProperty.getValue());
                            } else {
                                message = Component.translatable(
                                        "message.desert_pyramid_incorrect_block",
                                        requiredBlock.pos().getX(),
                                        requiredBlock.pos().getY(),
                                        requiredBlock.pos().getZ(),
                                        requiredState.getBlock().getName());
                            }
                            playersToNotify.forEach(
                                    player -> player.sendSystemMessage(message.withStyle(ChatFormatting.DARK_RED)));
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
