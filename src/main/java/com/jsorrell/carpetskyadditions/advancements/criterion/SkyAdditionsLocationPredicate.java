package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.jsorrell.carpetskyadditions.helpers.CoralSpreader;
import com.jsorrell.carpetskyadditions.helpers.SmallDripleafSpreader;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsText;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;

public record SkyAdditionsLocationPredicate(
        Optional<Boolean> desertPyramidCheck,
        Optional<Boolean> coralConvertible,
        Optional<MinMaxBounds.Doubles> coralSuitability,
        Optional<Boolean> smallDripleafCanSpread) {
    public static final Codec<SkyAdditionsLocationPredicate> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                            Codec.BOOL
                                    .optionalFieldOf("is_desert_pyramid_blue_terracotta")
                                    .forGetter(SkyAdditionsLocationPredicate::desertPyramidCheck),
                            Codec.BOOL
                                    .optionalFieldOf("coral_convertible")
                                    .forGetter(SkyAdditionsLocationPredicate::coralConvertible),
                            MinMaxBounds.Doubles.CODEC
                                    .optionalFieldOf("coral_suitability")
                                    .forGetter(SkyAdditionsLocationPredicate::coralSuitability),
                            Codec.BOOL
                                    .optionalFieldOf("small_dripleaf_spreadable")
                                    .forGetter(SkyAdditionsLocationPredicate::smallDripleafCanSpread))
                    .apply(instance, SkyAdditionsLocationPredicate::new));

    private boolean doDesertPyramidCheck(ServerLevel level, BlockPos blueTerracottaPos, boolean sendDebugMessage) {
        StructureTemplate template = level.getServer()
                .getStructureManager()
                .get(new SkyAdditionsResourceLocation("desert_pyramid"))
                .orElseThrow();
        BlockPos centerOffset = new BlockPos(
                template.getSize().getX() / 2, 0, template.getSize().getZ() / 2);
        BlockPos structureOrigin = blueTerracottaPos.subtract(centerOffset);

        StructurePlaceSettings placeSettings = new StructurePlaceSettings().setRotationPivot(centerOffset);

        return Arrays.stream(Rotation.values()).anyMatch(r -> {
            placeSettings.setRotation(r);

            for (Block block : new Block[] {
                Blocks.BLUE_TERRACOTTA,
                Blocks.ORANGE_TERRACOTTA,
                Blocks.SANDSTONE,
                Blocks.CUT_SANDSTONE,
                Blocks.CHISELED_SANDSTONE,
                Blocks.SANDSTONE_STAIRS,
                Blocks.SANDSTONE_SLAB
            }) {
                List<StructureTemplate.StructureBlockInfo> requiredBlocks =
                        template.filterBlocks(structureOrigin, placeSettings, block);
                for (StructureTemplate.StructureBlockInfo requiredBlock : requiredBlocks) {
                    BlockState requiredState = requiredBlock.state();
                    BlockState currentState = level.getBlockState(requiredBlock.pos());
                    if (currentState != requiredState) {
                        // Use terracotta to determine location and rotation
                        if (sendDebugMessage && !requiredState.is(BlockTags.TERRACOTTA)) {
                            // Help players within 10 blocks of bounding box debug builds
                            AABB buildersBox = AABB.of(template.getBoundingBox(placeSettings, structureOrigin)
                                    .inflatedBy(10));
                            List<ServerPlayer> playersToNotify =
                                    level.getPlayers(serverPlayer -> buildersBox.contains(serverPlayer.position()));
                            MutableComponent message;
                            if (currentState.getBlock() == requiredState.getBlock()) {
                                Map.Entry<Property<?>, Comparable<?>> incorrectProperty =
                                        requiredState.getValues().entrySet().stream()
                                                .filter(e -> currentState.getValue(e.getKey()) != e.getValue())
                                                .findAny()
                                                .orElseThrow();
                                message = SkyAdditionsText.translatable(
                                        "message.desert_pyramid_incorrect_state",
                                        requiredBlock.pos().getX(),
                                        requiredBlock.pos().getY(),
                                        requiredBlock.pos().getZ(),
                                        incorrectProperty.getKey().getName(),
                                        incorrectProperty.getValue());
                            } else {
                                message = SkyAdditionsText.translatable(
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

    public boolean matches(ServerLevel level, double x, double y, double z) {
        BlockPos blockPos = BlockPos.containing(x, y, z);
        if (desertPyramidCheck.isPresent()) {
            if (doDesertPyramidCheck(level, blockPos, desertPyramidCheck.get()) != desertPyramidCheck.get()) {
                return false;
            }
        }

        if (coralConvertible.isPresent()) {
            if (CoralSpreader.isConvertible(level, blockPos) != coralConvertible.get()) {
                return false;
            }
        }

        if (coralSuitability.isPresent()) {
            if (!coralSuitability.get().matches(CoralSpreader.calculateCoralSuitability(level, blockPos))) {
                return false;
            }
        }

        if (smallDripleafCanSpread.isPresent()) {
            if (SmallDripleafSpreader.canSpreadFrom(level.getBlockState(blockPos), level, blockPos)
                    != smallDripleafCanSpread.get()) {
                return false;
            }
        }

        return true;
    }
}
