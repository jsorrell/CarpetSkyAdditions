package com.jsorrell.carpetskyadditions.gen;

import com.google.common.base.Suppliers;
import com.jsorrell.carpetskyadditions.mixin.JigsawStructureAccessor;
import com.jsorrell.carpetskyadditions.mixin.SinglePoolElementAccessor;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;

public class SkyBlockChunkGenerator extends NoiseBasedChunkGenerator {
    private final Supplier<List<FeatureSorter.StepFeatureData>> indexedFeaturesListSupplier;

    public static final Codec<SkyBlockChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(SkyBlockChunkGenerator::getBiomeSource),
                    NoiseGeneratorSettings.CODEC
                            .fieldOf("settings")
                            .forGetter(SkyBlockChunkGenerator::generatorSettings))
            .apply(instance, instance.stable(SkyBlockChunkGenerator::new)));

    public SkyBlockChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, settings);
        // Duplicate indexedFeaturesListSupplier from super b/c it has private access
        this.indexedFeaturesListSupplier = Suppliers.memoize(() -> FeatureSorter.buildFeaturesPerStep(
                List.copyOf(biomeSource.possibleBiomes()),
                biomeEntry -> biomeEntry.value().getGenerationSettings().features(),
                true));
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void buildSurface(
            WorldGenRegion region, StructureManager structures, RandomState noiseConfig, ChunkAccess chunk) {}

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(
            Executor executor, Blender blender, RandomState noiseConfig, StructureManager accessor, ChunkAccess chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public void applyCarvers(
            WorldGenRegion chunkRegion,
            long seed,
            RandomState noiseConfig,
            BiomeManager access,
            StructureManager structureAccessor,
            ChunkAccess chunk,
            GenerationStep.Carving carver) {}

    // Duplicated from super b/c it has private access
    private static BoundingBox getBlockBoxForChunk(ChunkAccess chunk) {
        ChunkPos chunkPos = chunk.getPos();
        int startX = chunkPos.getMinBlockX();
        int startZ = chunkPos.getMinBlockZ();
        LevelHeightAccessor heightLimitView = chunk.getHeightAccessorForGeneration();
        int bottomY = heightLimitView.getMinBuildHeight() + 1;
        int topY = heightLimitView.getMaxBuildHeight() - 1;
        return new BoundingBox(startX, bottomY, startZ, startX + 15, topY, startZ + 15);
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel world, ChunkAccess chunk, StructureManager structureAccessor) {
        ChunkPos chunkPos = chunk.getPos();
        SectionPos chunkSectionPos = SectionPos.of(chunkPos, world.getMinSection());
        BlockPos minChunkPos = chunkSectionPos.origin();

        Registry<Structure> structureRegistry = world.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Map<Integer, List<Structure>> structuresByStep = structureRegistry.stream()
                .collect(Collectors.groupingBy(
                        structureType -> structureType.step().ordinal()));
        List<FeatureSorter.StepFeatureData> indexedFeatures = this.indexedFeaturesListSupplier.get();

        WorldgenRandom chunkRandom = new WorldgenRandom(new XoroshiroRandomSource(RandomSupport.generateUniqueSeed()));
        long populationSeed = chunkRandom.setDecorationSeed(world.getSeed(), minChunkPos.getX(), minChunkPos.getZ());

        // Get all surrounding biomes for biome-based structures
        ObjectArraySet<Biome> biomeSet = new ObjectArraySet<>();
        ChunkPos.rangeClosed(chunkSectionPos.chunk(), 1).forEach(curChunkPos -> {
            ChunkAccess curChunk = world.getChunk(curChunkPos.x, curChunkPos.z);
            for (LevelChunkSection chunkSection : curChunk.getSections()) {
                chunkSection.getBiomes().getAll(registryEntry -> biomeSet.add(registryEntry.value()));
            }
        });
        biomeSet.retainAll(
                this.biomeSource.possibleBiomes().stream().map(Holder::value).collect(Collectors.toSet()));

        int numIndexedFeatures = indexedFeatures.size();
        try {
            Registry<PlacedFeature> placedFeatures = world.registryAccess().registryOrThrow(Registries.PLACED_FEATURE);
            int numSteps = Math.max(GenerationStep.Decoration.values().length, numIndexedFeatures);
            for (int genStep = 0; genStep < numSteps; ++genStep) {
                int m = 0;
                if (structureAccessor.shouldGenerateStructures()) {
                    List<Structure> structuresForStep = structuresByStep.getOrDefault(genStep, Collections.emptyList());
                    for (Structure structure : structuresForStep) {
                        chunkRandom.setFeatureSeed(populationSeed, m, genStep);
                        Supplier<String> featureNameSupplier = () -> structureRegistry
                                .getResourceKey(structure)
                                .map(Object::toString)
                                .orElseGet(structure::toString);
                        try {
                            // Stronghold
                            if (structure instanceof StrongholdStructure
                                    && (SkyAdditionsSettings.generateEndPortals
                                            || SkyAdditionsSettings.generateSilverfishSpawners)) {
                                world.setCurrentlyGenerating(featureNameSupplier);
                                structureAccessor
                                        .startsForStructure(chunkSectionPos, structure)
                                        .forEach(structureStart -> {
                                            for (StructurePiece piece : structureStart.getPieces()) {
                                                if (piece.isCloseToChunk(chunkPos, 0)
                                                        && piece.getType()
                                                                == StructurePieceType.STRONGHOLD_PORTAL_ROOM) {
                                                    BoundingBox chunkBox = getBlockBoxForChunk(chunk);
                                                    if (SkyAdditionsSettings.generateEndPortals) {
                                                        new SkyBlockStructures.EndPortalStructure(piece)
                                                                .generate(world, chunkBox, chunkRandom);
                                                    }

                                                    if (SkyAdditionsSettings.generateSilverfishSpawners) {
                                                        new SkyBlockStructures.SilverfishSpawnerStructure(piece)
                                                                .generate(world, chunkBox, chunkRandom);
                                                    }
                                                }
                                            }
                                        });
                            } else if (structure instanceof JigsawStructure) {
                                Holder<StructureTemplatePool> startPool =
                                        ((JigsawStructureAccessor) structure).getStartPool();
                                // Bastion Remnants
                                if (SkyAdditionsSettings.generateMagmaCubeSpawners
                                        && startPool.is(new ResourceLocation("bastion/starts"))) {
                                    world.setCurrentlyGenerating(featureNameSupplier);
                                    structureAccessor
                                            .startsForStructure(chunkSectionPos, structure)
                                            .forEach(structureStart -> {
                                                for (StructurePiece piece : structureStart.getPieces()) {
                                                    if (piece.isCloseToChunk(chunkPos, 0)
                                                            && piece instanceof PoolElementStructurePiece poolPiece) {
                                                        if (poolPiece.getElement()
                                                                instanceof SinglePoolElement singlePoolElement) {
                                                            ResourceLocation pieceId = ((SinglePoolElementAccessor)
                                                                            singlePoolElement)
                                                                    .getTemplate()
                                                                    .left()
                                                                    .orElseThrow(AssertionError::new);
                                                            if (pieceId.equals(new ResourceLocation(
                                                                    "bastion/treasure/bases/lava_basin"))) {
                                                                new SkyBlockStructures.MagmaCubeSpawner(piece)
                                                                        .generate(
                                                                                world,
                                                                                getBlockBoxForChunk(chunk),
                                                                                chunkRandom);
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                    // Ancient Cities
                                } else if (SkyAdditionsSettings.generateAncientCityPortals
                                        && startPool.is(new ResourceLocation("ancient_city/city_center"))) {
                                    world.setCurrentlyGenerating(featureNameSupplier);
                                    structureAccessor
                                            .startsForStructure(chunkSectionPos, structure)
                                            .forEach(structureStart -> {
                                                for (StructurePiece piece : structureStart.getPieces()) {
                                                    if (piece.isCloseToChunk(chunkPos, 0)
                                                            && piece instanceof PoolElementStructurePiece poolPiece) {
                                                        if (poolPiece.getElement()
                                                                instanceof SinglePoolElement singlePoolElement) {
                                                            ResourceLocation pieceId = ((SinglePoolElementAccessor)
                                                                            singlePoolElement)
                                                                    .getTemplate()
                                                                    .left()
                                                                    .orElseThrow(AssertionError::new);
                                                            if (pieceId.getNamespace()
                                                                            .equals("minecraft")
                                                                    && pieceId.getPath()
                                                                            .startsWith(
                                                                                    "ancient_city/city_center/city_center")) {
                                                                new SkyBlockStructures.AncientCityPortalStructure(piece)
                                                                        .generate(
                                                                                world,
                                                                                getBlockBoxForChunk(chunk),
                                                                                chunkRandom);
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        } catch (Exception e) {
                            CrashReport crashReport = CrashReport.forThrowable(e, "Feature placement");
                            crashReport.addCategory("Feature").setDetail("Description", featureNameSupplier::get);
                            throw new ReportedException(crashReport);
                        }
                        ++m;
                    }
                }
                if (genStep >= numIndexedFeatures) continue;
                IntArraySet intSet = new IntArraySet();
                for (Biome biome : biomeSet) {
                    List<HolderSet<PlacedFeature>> biomeFeatureStepList =
                            biome.getGenerationSettings().features();
                    if (genStep >= biomeFeatureStepList.size()) continue;
                    HolderSet<PlacedFeature> biomeFeaturesForStep = biomeFeatureStepList.get(genStep);
                    FeatureSorter.StepFeatureData indexedFeature = indexedFeatures.get(genStep);
                    biomeFeaturesForStep.stream()
                            .map(Holder::value)
                            .forEach(placedFeature ->
                                    intSet.add(indexedFeature.indexMapping().applyAsInt(placedFeature)));
                }
                int n = intSet.size();
                int[] is = intSet.toIntArray();
                Arrays.sort(is);
                FeatureSorter.StepFeatureData indexedFeature = indexedFeatures.get(genStep);
                for (int o = 0; o < n; ++o) {
                    int p = is[o];
                    PlacedFeature placedFeature = indexedFeature.features().get(p);
                    Supplier<String> placedFeatureNameSupplier = () -> placedFeatures
                            .getResourceKey(placedFeature)
                            .map(Object::toString)
                            .orElseGet(placedFeature::toString);
                    chunkRandom.setFeatureSeed(populationSeed, p, genStep);
                    try {
                        // Random End Gateways
                        if (SkyAdditionsSettings.generateRandomEndGateways
                                && placedFeature.feature().is(new ResourceLocation("end_gateway_return"))) {
                            world.setCurrentlyGenerating(placedFeatureNameSupplier);
                            placedFeature.placeWithBiomeCheck(world, this, chunkRandom, minChunkPos);
                        }
                    } catch (Exception e) {
                        CrashReport crashReport = CrashReport.forThrowable(e, "Feature placement");
                        crashReport.addCategory("Feature").setDetail("Description", placedFeatureNameSupplier::get);
                        throw new ReportedException(crashReport);
                    }
                }
            }
            world.setCurrentlyGenerating(null);
        } catch (Exception e) {
            CrashReport crashReport = CrashReport.forThrowable(e, "Biome decoration");
            crashReport
                    .addCategory("Generation")
                    .setDetail("CenterX", chunkPos.x)
                    .setDetail("CenterZ", chunkPos.z)
                    .setDetail("Seed", populationSeed);
            throw new ReportedException(crashReport);
        }
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {}

    public RandomState getNoiseConfig(WorldGenLevel world) {
        return RandomState.create(
                this.generatorSettings().value(),
                world.registryAccess().registryOrThrow(Registries.NOISE).asLookup(),
                world.getSeed());
    }

    public int getHeightOnGround(int x, int z, Heightmap.Types heightmap, WorldGenLevel world) {
        return super.getBaseHeight(x, z, heightmap, world, getNoiseConfig(world));
    }

    public int getHeightInGround(int x, int z, Heightmap.Types heightmap, WorldGenLevel world) {
        return super.getBaseHeight(x, z, heightmap, world, getNoiseConfig(world)) - 1;
    }
}
