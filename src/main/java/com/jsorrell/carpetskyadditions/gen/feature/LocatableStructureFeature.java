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

public class LocatableStructureFeature extends Feature<LocatableStructureFeatureConfiguration> {
    public LocatableStructureFeature(Codec<LocatableStructureFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LocatableStructureFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        MinecraftServer server = level.getServer();
        if (server == null) {
            return false;
        }
        LocatableStructureFeatureConfiguration config = context.config();
        StructureTemplate structure =
                server.getStructureManager().get(config.structure()).orElse(null);
        if (structure == null) {
            SkyAdditionsSettings.LOG.warn("Missing structure " + config.structure());
            return false;
        }

        return structure.placeInWorld(
                level,
                context.origin().offset(config.pos()),
                null,
                new StructurePlaceSettings(),
                context.random(),
                Block.UPDATE_CLIENTS);
    }
}
