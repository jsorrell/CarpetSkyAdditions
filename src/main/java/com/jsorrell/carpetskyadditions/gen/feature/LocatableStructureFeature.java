package com.jsorrell.carpetskyadditions.gen.feature;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class LocatableStructureFeature extends Feature<LocatableStructureFeatureConfig> {
  public LocatableStructureFeature(Codec<LocatableStructureFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public boolean generate(FeatureContext<LocatableStructureFeatureConfig> context) {
    StructureWorldAccess world = context.getWorld();
    MinecraftServer server = world.getServer();
    if (server == null) {
      return false;
    }
    LocatableStructureFeatureConfig config = context.getConfig();
    StructureTemplate structure = server.getStructureTemplateManager().getTemplate(config.structure()).orElse(null);
    if (structure == null) {
      SkyAdditionsSettings.LOG.warn("Missing structure " + config.structure());
      return false;
    }

    return structure.place(world, context.getOrigin().add(config.pos()), null, new StructurePlacementData(), context.getRandom(), Block.NOTIFY_LISTENERS);
  }
}
