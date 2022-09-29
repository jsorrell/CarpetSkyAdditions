package com.jsorrell.carpetskyadditions.gen;

import com.google.common.base.Suppliers;
import com.jsorrell.carpetskyadditions.mixin.JigsawStructureAccessor;
import com.jsorrell.carpetskyadditions.mixin.SinglePoolElementAccessor;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.RandomSeed;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.util.PlacedFeatureIndexer;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.StrongholdStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SkyBlockChunkGenerator extends NoiseChunkGenerator {
  private final Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry;
  private final Supplier<List<PlacedFeatureIndexer.IndexedFeatures>> indexedFeaturesListSupplier;

  public static final Codec<SkyBlockChunkGenerator> CODEC =
    RecordCodecBuilder.create(
      instance ->
        NoiseChunkGenerator.createStructureSetRegistryGetter(instance).and(
            instance
              .group(
                RegistryOps.createRegistryCodec(Registry.NOISE_KEY).forGetter(generator -> generator.noiseRegistry),
                (BiomeSource.CODEC.fieldOf("biome_source")).forGetter(ChunkGenerator::getBiomeSource),
                (ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings")).forGetter(SkyBlockChunkGenerator::getSettings)))
          .apply(instance, instance.stable(SkyBlockChunkGenerator::new)));

  public SkyBlockChunkGenerator(Registry<StructureSet> structureRegistry, Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
                                BiomeSource biomeSource, RegistryEntry<ChunkGeneratorSettings> settings) {
    super(structureRegistry, noiseRegistry, biomeSource, settings);
    // Duplicate noiseRegistry and indexedFeaturesListSupplier from super b/c it has private access
    this.noiseRegistry = noiseRegistry;
    this.indexedFeaturesListSupplier = Suppliers.memoize(() -> PlacedFeatureIndexer.collectIndexedFeatures(List.copyOf(biomeSource.getBiomes()), biomeEntry -> biomeEntry.value().getGenerationSettings().getFeatures(), true));
  }

  public RegistryEntry<ChunkGeneratorSettings> getSettings() {
    return this.settings;
  }

  @Override
  protected Codec<? extends ChunkGenerator> getCodec() {
    return CODEC;
  }

  @Override
  public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
  }

  @Override
  public CompletableFuture<Chunk> populateNoise(
    Executor executor, Blender blender, NoiseConfig noiseConfig, StructureAccessor accessor, Chunk chunk) {
    return CompletableFuture.completedFuture(chunk);
  }

  @Override
  public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess access, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carver) {
  }

  // Duplicated from super b/c it has private access
  private static BlockBox getBlockBoxForChunk(Chunk chunk) {
    ChunkPos chunkPos = chunk.getPos();
    int startX = chunkPos.getStartX();
    int startZ = chunkPos.getStartZ();
    HeightLimitView heightLimitView = chunk.getHeightLimitView();
    int bottomY = heightLimitView.getBottomY() + 1;
    int topY = heightLimitView.getTopY() - 1;
    return new BlockBox(startX, bottomY, startZ, startX + 15, topY, startZ + 15);
  }

  @Override
  public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
    ChunkPos chunkPos = chunk.getPos();
    ChunkSectionPos chunkSectionPos = ChunkSectionPos.from(chunkPos, world.getBottomSectionCoord());
    BlockPos minChunkPos = chunkSectionPos.getMinPos();

    Registry<Structure> structureRegistry = world.getRegistryManager().get(Registry.STRUCTURE_KEY);
    Map<Integer, List<Structure>> structuresByStep = structureRegistry.stream().collect(Collectors.groupingBy(structureType -> structureType.getFeatureGenerationStep().ordinal()));
    List<PlacedFeatureIndexer.IndexedFeatures> indexedFeatures = this.indexedFeaturesListSupplier.get();

    ChunkRandom chunkRandom = new ChunkRandom(new Xoroshiro128PlusPlusRandom(RandomSeed.getSeed()));
    long populationSeed = chunkRandom.setPopulationSeed(world.getSeed(), minChunkPos.getX(), minChunkPos.getZ());

    // Get all surrounding biomes for biome-based structures
    ObjectArraySet<Biome> biomeSet = new ObjectArraySet<>();
    ChunkPos.stream(chunkSectionPos.toChunkPos(), 1).forEach(curChunkPos -> {
      Chunk curChunk = world.getChunk(curChunkPos.x, curChunkPos.z);
      for (ChunkSection chunkSection : curChunk.getSectionArray()) {
        chunkSection.getBiomeContainer().forEachValue(registryEntry -> biomeSet.add(registryEntry.value()));
      }
    });
    biomeSet.retainAll(this.biomeSource.getBiomes().stream().map(RegistryEntry::value).collect(Collectors.toSet()));

    int numIndexedFeatures = indexedFeatures.size();
    try {
      Registry<PlacedFeature> placedFeatures = world.getRegistryManager().get(Registry.PLACED_FEATURE_KEY);
      int numSteps = Math.max(GenerationStep.Feature.values().length, numIndexedFeatures);
      for (int genStep = 0; genStep < numSteps; ++genStep) {
        int m = 0;
        if (structureAccessor.shouldGenerateStructures()) {
          List<Structure> structuresForStep = structuresByStep.getOrDefault(genStep, Collections.emptyList());
          for (Structure structure : structuresForStep) {
            chunkRandom.setDecoratorSeed(populationSeed, m, genStep);
            Supplier<String> featureNameSupplier = () -> structureRegistry.getKey(structure).map(Object::toString).orElseGet(structure::toString);
            try {
              // Stronghold
              if (structure instanceof StrongholdStructure && (SkyAdditionsSettings.generateEndPortals || SkyAdditionsSettings.generateSilverfishSpawners)) {
                world.setCurrentlyGeneratingStructureName(featureNameSupplier);
                structureAccessor.getStructureStarts(chunkSectionPos, structure).forEach(structureStart -> {
                  for (StructurePiece piece : structureStart.getChildren()) {
                    if (piece.intersectsChunk(chunkPos, 0) && piece.getType() == StructurePieceType.STRONGHOLD_PORTAL_ROOM) {
                      BlockBox chunkBox = getBlockBoxForChunk(chunk);
                      if (SkyAdditionsSettings.generateEndPortals) {
                        new SkyBlockStructures.EndPortalStructure(piece).generate(world, chunkBox, chunkRandom);
                      }

                      if (SkyAdditionsSettings.generateSilverfishSpawners) {
                        new SkyBlockStructures.SilverfishSpawnerStructure(piece).generate(world, chunkBox, chunkRandom);
                      }
                    }
                  }
                });
              } else if (structure instanceof JigsawStructure) {
                RegistryEntry<StructurePool> startPool = ((JigsawStructureAccessor) structure).getStartPool();
                // Bastion Remnants
                if (SkyAdditionsSettings.generateMagmaCubeSpawners && startPool.matchesId(new Identifier("bastion/starts"))) {
                  world.setCurrentlyGeneratingStructureName(featureNameSupplier);
                  structureAccessor.getStructureStarts(chunkSectionPos, structure).forEach(structureStart -> {
                    for (StructurePiece piece : structureStart.getChildren()) {
                      if (piece.intersectsChunk(chunkPos, 0) && piece instanceof PoolStructurePiece poolPiece) {
                        if (poolPiece.getPoolElement() instanceof SinglePoolElement singlePoolElement) {
                          Identifier pieceId = ((SinglePoolElementAccessor) singlePoolElement).getLocation().left().orElseThrow(AssertionError::new);
                          if (pieceId.equals(new Identifier("bastion/treasure/bases/lava_basin"))) {
                            new SkyBlockStructures.MagmaCubeSpawner(piece).generate(world, getBlockBoxForChunk(chunk), chunkRandom);
                          }
                        }
                      }
                    }
                  });
                  // Ancient Cities
                } else if (SkyAdditionsSettings.generateAncientCityPortals && startPool.matchesId(new Identifier("ancient_city/city_center"))) {
                  world.setCurrentlyGeneratingStructureName(featureNameSupplier);
                  structureAccessor.getStructureStarts(chunkSectionPos, structure).forEach(structureStart -> {
                    for (StructurePiece piece : structureStart.getChildren()) {
                      if (piece.intersectsChunk(chunkPos, 0) && piece instanceof PoolStructurePiece poolPiece) {
                        if (poolPiece.getPoolElement() instanceof SinglePoolElement singlePoolElement) {
                          Identifier pieceId = ((SinglePoolElementAccessor) singlePoolElement).getLocation().left().orElseThrow(AssertionError::new);
                          if (pieceId.getNamespace().equals("minecraft") && pieceId.getPath().startsWith("ancient_city/city_center/city_center")) {
                            new SkyBlockStructures.AncientCityPortalStructure(piece).generate(world, getBlockBoxForChunk(chunk), chunkRandom);
                          }
                        }
                      }
                    }
                  });
                }
              }
            } catch (Exception e) {
              CrashReport crashReport = CrashReport.create(e, "Feature placement");
              crashReport.addElement("Feature").add("Description", featureNameSupplier::get);
              throw new CrashException(crashReport);
            }
            ++m;
          }
        }
        if (genStep >= numIndexedFeatures) continue;
        IntArraySet intSet = new IntArraySet();
        for (Biome biome : biomeSet) {
          List<RegistryEntryList<PlacedFeature>> biomeFeatureStepList = biome.getGenerationSettings().getFeatures();
          if (genStep >= biomeFeatureStepList.size()) continue;
          RegistryEntryList<PlacedFeature> biomeFeaturesForStep = biomeFeatureStepList.get(genStep);
          PlacedFeatureIndexer.IndexedFeatures indexedFeature = indexedFeatures.get(genStep);
          biomeFeaturesForStep.stream().map(RegistryEntry::value).forEach(placedFeature -> intSet.add(indexedFeature.indexMapping().applyAsInt(placedFeature)));
        }
        int n = intSet.size();
        int[] is = intSet.toIntArray();
        Arrays.sort(is);
        PlacedFeatureIndexer.IndexedFeatures indexedFeature = indexedFeatures.get(genStep);
        for (int o = 0; o < n; ++o) {
          int p = is[o];
          PlacedFeature placedFeature = indexedFeature.features().get(p);
          Supplier<String> placedFeatureNameSupplier = () -> placedFeatures.getKey(placedFeature).map(Object::toString).orElseGet(placedFeature::toString);
          chunkRandom.setDecoratorSeed(populationSeed, p, genStep);
          try {
            // Random End Gateways
            if (SkyAdditionsSettings.generateRandomEndGateways && placedFeature.feature().matchesId(new Identifier("end_gateway_return"))) {
              world.setCurrentlyGeneratingStructureName(placedFeatureNameSupplier);
              placedFeature.generate(world, this, chunkRandom, minChunkPos);
            }
          } catch (Exception e) {
            CrashReport crashReport = CrashReport.create(e, "Feature placement");
            crashReport.addElement("Feature").add("Description", placedFeatureNameSupplier::get);
            throw new CrashException(crashReport);
          }
        }
      }
      world.setCurrentlyGeneratingStructureName(null);
    } catch (Exception e) {
      CrashReport crashReport = CrashReport.create(e, "Biome decoration");
      crashReport.addElement("Generation").add("CenterX", chunkPos.x).add("CenterZ", chunkPos.z).add("Seed", populationSeed);
      throw new CrashException(crashReport);
    }
  }

  @Override
  public void populateEntities(ChunkRegion region) {
  }

  public NoiseConfig getNoiseConfig(StructureWorldAccess world) {
    return NoiseConfig.create(this.settings.value(), world.getRegistryManager().get(Registry.NOISE_KEY), world.getSeed());
  }

  public int getHeightOnGround(int x, int z, Heightmap.Type heightmap, StructureWorldAccess world) {
    return super.getHeight(x, z, heightmap, world, getNoiseConfig(world));
  }

  public int getHeightInGround(int x, int z, Heightmap.Type heightmap, StructureWorldAccess world) {
    return super.getHeight(x, z, heightmap, world, getNoiseConfig(world)) - 1;
  }

  static {
    Registry.register(Registry.CHUNK_GENERATOR, new SkyAdditionsIdentifier("skyblock"), SkyBlockChunkGenerator.CODEC);
  }
}
