package com.jsorrell.carpetskyadditions.commands;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import carpet.utils.CommandHelper;
import com.jsorrell.carpetskyadditions.gen.feature.SkyAdditionsConfiguredFeatures;
import com.jsorrell.carpetskyadditions.gen.feature.SkyAdditionsFeatures;
import com.jsorrell.carpetskyadditions.gen.feature.SpawnPlatformFeatureConfig;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class SkyIslandCommand {
    private static final SimpleCommandExceptionType FAILED_EXCEPTION =
            new SimpleCommandExceptionType(Text.translatable("commands.skyisland.new.failed"));
    private static final SimpleCommandExceptionType ISLAND_NOT_CREATED =
            new SimpleCommandExceptionType(Text.translatable("commands.skyisland.not_created"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        int maxIslandNum = SkyIslandPositionContainer.getNumIslands();

        LiteralArgumentBuilder<ServerCommandSource> command = literal("skyisland")
                .requires(source -> CommandHelper.canUseCommand(source, SkyAdditionsSettings.commandSkyIsland))
                .then(literal("new").executes(c -> newIsland(c.getSource())))
                .then(literal("join")
                        .then(argument("num", IntegerArgumentType.integer(1, maxIslandNum))
                                .executes(c -> joinIsland(
                                        c.getSource(),
                                        c.getSource().getPlayerOrThrow(),
                                        IntegerArgumentType.getInteger(c, "num")))
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(c -> joinIsland(
                                                c.getSource(),
                                                EntityArgumentType.getPlayer(c, "player"),
                                                IntegerArgumentType.getInteger(c, "num"))))))
                .then(literal("locate")
                        .then(argument("num", IntegerArgumentType.integer(1, maxIslandNum))
                                .executes(c -> locateIsland(c.getSource(), IntegerArgumentType.getInteger(c, "num")))));

        dispatcher.register(command);
    }

    private static int locateIsland(ServerCommandSource source, int islandNum) throws CommandSyntaxException {
        ChunkPos chunkPos = SkyIslandPositionContainer.getChunk(islandNum);
        int x = chunkPos.getCenterX();
        int z = chunkPos.getCenterZ();
        Chunk chunk = source.getWorld().getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY);
        if (chunk.getStatus() != ChunkStatus.FULL) {
            throw ISLAND_NOT_CREATED.create();
        }

        MutableText text = Texts.bracketed(Text.translatable("commands.skyisland.locate.coordinates", x, z))
                .styled(style -> style.withColor(Formatting.GREEN)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + x + " ~ " + z))
                        .withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT, Text.translatable("chat.coordinates.tooltip"))));
        source.sendFeedback(Text.translatable("commands.skyisland.locate.success", islandNum, text), false);

        BlockPos sourcePos = new BlockPos(source.getPosition());
        int xOff = sourcePos.getX() - x;
        int zOff = sourcePos.getZ() - z;
        return MathHelper.floor(MathHelper.sqrt(xOff * xOff + zOff * zOff));
    }

    // First look for a sky_island configured feature
    // Then try taking the spawn platform's config and using the platformConfig value if it is of type "spawn_platform"
    // Otherwise, just generate the spawn_platform configured feature
    private static ConfiguredFeature<?, ?> getIslandFeature(Registry<ConfiguredFeature<?, ?>> cfr) {
        ConfiguredFeature<?, ?> skyIslandCF = cfr.get(SkyAdditionsConfiguredFeatures.SKY_ISLAND);
        if (skyIslandCF != null) {
            return skyIslandCF;
        }

        ConfiguredFeature<?, ?> spawnPlatformCF = cfr.getOrThrow(SkyAdditionsConfiguredFeatures.SPAWN_PLATFORM);
        // try to get the platform out
        if (spawnPlatformCF.feature().equals(SkyAdditionsFeatures.SPAWN_PLATFORM)) {
            SpawnPlatformFeatureConfig config = (SpawnPlatformFeatureConfig) spawnPlatformCF.config();
            return new ConfiguredFeature<>(SkyAdditionsFeatures.LOCATABLE_STRUCTURE, config.platformConfig());
        } else {
            return spawnPlatformCF;
        }
    }

    private static int newIsland(ServerCommandSource source) throws CommandSyntaxException {
        int max = SkyIslandPositionContainer.getNumIslands();
        Optional<ImmutablePair<Integer, ChunkPos>> islandOpt = IntStream.range(1, max)
                .mapToObj(i -> ImmutablePair.of(i, SkyIslandPositionContainer.getChunk(i)))
                .filter(i -> {
                    Chunk chunk = source.getWorld().getChunk(i.right.x, i.right.z, ChunkStatus.EMPTY);
                    return chunk.getStatus() == ChunkStatus.EMPTY;
                })
                .findFirst();
        if (islandOpt.isEmpty()) {
            source.sendFeedback(Text.translatable("commands.skyisland.new.no_valid_positions"), true);
            return 0;
        }
        ImmutablePair<Integer, ChunkPos> island = islandOpt.get();
        ChunkPos chunkPos = island.right;
        int x = chunkPos.getCenterX();
        int z = chunkPos.getCenterZ();

        // Load the target area
        source.getWorld().getChunkManager().addTicket(ChunkTicketType.UNKNOWN, chunkPos, 2, chunkPos);
        Registry<ConfiguredFeature<?, ?>> configuredFeatureRegistry =
                source.getServer().getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE);
        ConfiguredFeature<?, ?> skyIslandFeature = getIslandFeature(configuredFeatureRegistry);
        ChunkRandom random = new ChunkRandom(new CheckedRandom(0));
        random.setCarverSeed(source.getWorld().getSeed(), chunkPos.x, chunkPos.z);

        if (!skyIslandFeature.generate(
                source.getWorld(),
                source.getWorld().getChunkManager().getChunkGenerator(),
                random,
                new BlockPos(x, 0, z))) {
            throw FAILED_EXCEPTION.create();
        }

        Text feedback = Text.translatable("commands.skyisland.new.success", island.getLeft(), x, z);
        source.sendFeedback(feedback, true);
        return island.getLeft();
    }

    private static int joinIsland(ServerCommandSource source, ServerPlayerEntity player, int islandNum)
            throws CommandSyntaxException {
        ChunkPos chunkPos = SkyIslandPositionContainer.getChunk(islandNum);
        int x = chunkPos.getCenterX();
        int z = chunkPos.getCenterZ();
        joinIsland(source, player, x, z);
        return 1;
    }

    private static void joinIsland(ServerCommandSource source, ServerPlayerEntity player, int x, int z)
            throws CommandSyntaxException {
        BlockPos pos = new BlockPos(x, 0, z);
        ChunkPos chunkPos = new ChunkPos(pos);
        Chunk chunk = source.getWorld().getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY);
        int y;
        Supplier<Integer> spawnHeight = () -> chunk.sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, x, z) + 1;
        if (chunk.getStatus() != ChunkStatus.FULL || (y = spawnHeight.get()) <= chunk.getBottomY()) {
            throw ISLAND_NOT_CREATED.create();
        }
        player.teleport(x + 0.5, y, z + 0.5);
        if (!player.isFallFlying()) {
            player.setVelocity(player.getVelocity().multiply(1.0, 0.0, 1.0));
            player.setOnGround(true);
        }
        player.setSpawnPoint(player.world.getRegistryKey(), new BlockPos(x, y, z), 0f, true, false);
    }

    public abstract static class SkyIslandPositionContainer {
        private static final ArrayList<ChunkPos> ISLAND_CHUNKS = new ArrayList<>();
        // Ordered to prioritize maximum distance from origin + previous islands
        private static final int[] ORDERING = {
            46, 59, 41, 54, 50, 63, 24, 13, 8, 16, 57, 48, 61, 39, 52, 19, 11, 31, 36, 20, 33, 44, 27, 22, 29, 1, 4, 3,
            6, 2, 5, 38, 30, 34, 26, 35, 25, 21, 28, 37, 23, 32, 51, 64, 43, 56, 40, 53, 49, 62, 45, 58, 47, 60, 42, 55,
            10, 7, 17, 12, 15, 14, 9, 18
        };

        static {
            ISLAND_CHUNKS.addAll(getIslandsInRing(384, 6, 0.25));
            ISLAND_CHUNKS.addAll(getIslandsInRing(768, 13, 0.5));
            ISLAND_CHUNKS.addAll(getIslandsInRing(1152, 19, 0.75));
            ISLAND_CHUNKS.addAll(getIslandsInRing(1536, 26, 1.));
        }

        public static int getNumIslands() {
            return ORDERING.length;
        }

        // 1 indexed
        public static ChunkPos getChunk(int i) {
            return ISLAND_CHUNKS.get(ORDERING[i - 1]);
        }

        private static ArrayList<ChunkPos> getIslandsInRing(int radius, int num, double offetAngle) {
            ArrayList<ChunkPos> islands = new ArrayList<>();

            for (int i = 0; i < num; i++) {
                double angle = offetAngle + i * (2 * Math.PI) / num;
                double x = Math.sin(angle) * radius;
                double z = Math.cos(angle) * radius;
                islands.add(new ChunkPos((int) x, (int) z));
            }
            return islands;
        }
    }
}
