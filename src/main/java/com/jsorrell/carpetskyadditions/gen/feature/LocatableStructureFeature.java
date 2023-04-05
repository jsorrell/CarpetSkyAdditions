package com.jsorrell.carpetskyadditions.gen.feature;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.mojang.serialization.Codec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class LocatableStructureFeature extends Feature<LocatableStructureFeatureConfig> {
    public LocatableStructureFeature(Codec<LocatableStructureFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LocatableStructureFeatureConfig> context) {
        WorldGenLevel world = context.level();
        MinecraftServer server = world.getServer();
        if (server == null) {
            return false;
        }
        LocatableStructureFeatureConfig config = context.config();
        StructureTemplate structure =
                server.getStructureManager().get(config.structure()).orElse(null);
        if (structure == null) {
            SkyAdditionsSettings.LOG.warn("Missing structure " + config.structure());
            return false;
        }

        return structure.placeInWorld(
                world,
                context.origin().offset(config.pos()),
                null,
                new StructurePlaceSettings(),
                context.random(),
                Block.UPDATE_CLIENTS);
    }
}
