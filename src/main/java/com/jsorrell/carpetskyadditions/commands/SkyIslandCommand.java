package com.jsorrell.carpetskyadditions.commands;

import carpet.utils.CommandHelper;
import com.jsorrell.carpetskyadditions.gen.feature.SkyAdditionsConfiguredFeatures;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsText;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
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

import java.util.ArrayList;
import java.util.Optional;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SkyIslandCommand {
  private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(SkyAdditionsText.translatable("commands.skyisland.new.failed"));
  private static final SimpleCommandExceptionType ISLAND_NOT_CREATED = new SimpleCommandExceptionType(SkyAdditionsText.translatable("commands.skyisland.not_created"));

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    LiteralArgumentBuilder<ServerCommandSource> command = literal("skyisland")
      .requires(source -> CommandHelper.canUseCommand(source, SkyAdditionsSettings.skyspawnCommand))
      .then(literal("new")
        .executes(c -> newIsland(c.getSource())))
      .then(literal("join")
        .then(argument("num", IntegerArgumentType.integer(1, SkyIslandPositionContainer.getNumIslands()))
          .executes(c -> joinIsland(c.getSource(), c.getSource().getPlayerOrThrow(), IntegerArgumentType.getInteger(c, "num")))
          .then(argument("player", EntityArgumentType.player())
            .executes(c -> joinIsland(c.getSource(), EntityArgumentType.getPlayer(c, "player"), IntegerArgumentType.getInteger(c, "num"))
        ))))
      .then(literal("locate")
        .then(argument("num", IntegerArgumentType.integer(1, SkyIslandPositionContainer.getNumIslands()))
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

    MutableText text = Texts.bracketed(SkyAdditionsText.translatable("commands.skyisland.locate.coordinates", x, z)).styled(style -> style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + x + " ~ " + z)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("chat.coordinates.tooltip"))));
    source.sendFeedback(SkyAdditionsText.translatable("commands.skyisland.locate.success", islandNum, text), false);

    BlockPos sourcePos = BlockPos.ofFloored(source.getPosition());
    int xOff = sourcePos.getX() - x;
    int zOff = sourcePos.getZ() - z;
    return MathHelper.floor(MathHelper.sqrt(xOff * xOff + zOff * zOff));
  }

  private static int newIsland(ServerCommandSource source) throws CommandSyntaxException {
    Optional<Integer> islandNum = getNewIslandPos(source.getWorld());
    if (islandNum.isEmpty()) {
      source.sendFeedback(SkyAdditionsText.translatable("commands.skyisland.new.no_valid_positions"), true);
      return 0;
    }
    ChunkPos chunkPos = SkyIslandPositionContainer.getChunk(islandNum.get());
    int x = chunkPos.getCenterX();
    int z = chunkPos.getCenterZ();

    // Load the target area
    source.getWorld().getChunkManager().addTicket(ChunkTicketType.UNKNOWN, chunkPos, 1, chunkPos);
    RegistryEntry.Reference<ConfiguredFeature<?, ?>> spawnPlatformFeature = source.getServer().getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE).getEntry(SkyAdditionsConfiguredFeatures.SPAWN_PLATFORM).orElseThrow();
    ChunkRandom random = new ChunkRandom(new CheckedRandom(0));
    random.setCarverSeed(source.getWorld().getSeed(), chunkPos.x, chunkPos.z);

    if (!spawnPlatformFeature.value().generate(source.getWorld(), source.getWorld().getChunkManager().getChunkGenerator(), random, new BlockPos(x, 0, z))) {
      throw FAILED_EXCEPTION.create();
    }

    source.sendFeedback(SkyAdditionsText.translatable("commands.skyisland.new.success", islandNum, x, z), true);
    return 1;
  }

  private static int joinIsland(ServerCommandSource source, ServerPlayerEntity player, int islandNum) throws CommandSyntaxException {
    ChunkPos chunkPos = SkyIslandPositionContainer.getChunk(islandNum);
    int x = chunkPos.getCenterX();
    int z = chunkPos.getCenterZ();
    joinIsland(source, player, x, z);
    return 1;
  }

  private static Optional<Integer> getNewIslandPos(ServerWorld world) {
    for (int i = 1; i <= SkyIslandPositionContainer.getNumIslands(); i++) {
      ChunkPos chunkPos = SkyIslandPositionContainer.getChunk(i);
      if (world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY).getStatus() == ChunkStatus.EMPTY) {
        return Optional.of(i);
      }
    }
    return Optional.empty();
  }

  private static void joinIsland(ServerCommandSource source, ServerPlayerEntity player, int x, int z) throws CommandSyntaxException {
    BlockPos pos = new BlockPos(x, 0, z);
    ChunkPos chunkPos = new ChunkPos(pos);
    Chunk chunk = source.getWorld().getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY);
    int y;
    if (chunk.getStatus() != ChunkStatus.FULL || (y = chunk.sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, x, z) + 1) <= chunk.getBottomY()) {
      throw ISLAND_NOT_CREATED.create();
    }
    player.teleport(x + 0.5, y, z + 0.5);
    if (!player.isFallFlying()) {
      player.setVelocity(player.getVelocity().multiply(1.0, 0.0, 1.0));
      player.setOnGround(true);
    }
    player.setSpawnPoint(player.world.getRegistryKey(), new BlockPos(x, y, z), 0f, true, false);
  }

  static abstract class SkyIslandPositionContainer {
    private static final ArrayList<ChunkPos> ISLAND_CHUNKS = new ArrayList<>();
    private static final int[] ORDERING = {21,52,57,23,15,32,18,4,56,22,19,27,48,25,13,26,2,64,3,43,39,46,45,35,33,51,30,24,1,7,47,55,20,11,17,44,60,29,28,14,50,31,8,16,9,58,5,37,38,49,42,61,40,59,34,41,63,62,6,54,12,10,53,36};

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
        islands.add(new ChunkPos((int)x, (int)z));
      }

      return islands;
    }
  }
}
