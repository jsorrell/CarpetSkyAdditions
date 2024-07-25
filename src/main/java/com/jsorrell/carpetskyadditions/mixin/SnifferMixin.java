package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sniffer.class)
public abstract class SnifferMixin extends Animal {
    // Desert wells are features (not structures) and don't have stored bounding boxes. They're not shown by minihud.
    // We use their loot tables by giving desert pyramids a chance to have desert well loot tables.
    @Unique
    private static final Map<Block, Map<ResourceKey<Structure>, Function<RandomSource, ResourceKey<LootTable>>>>
            LOOT_MAP = Map.of(
                    Blocks.SAND,
                    Map.of(
                            BuiltinStructures.DESERT_PYRAMID,
                            r -> r.nextFloat() < 0.2
                                    ? BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY
                                    : BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY,
                            BuiltinStructures.OCEAN_RUIN_WARM,
                            r -> BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY),
                    Blocks.GRAVEL,
                    Map.of(
                            BuiltinStructures.OCEAN_RUIN_COLD,
                            r -> BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY,
                            BuiltinStructures.TRAIL_RUINS,
                            r -> r.nextFloat() < 0.2
                                    ? BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE
                                    : BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON));

    protected SnifferMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract BlockPos getHeadBlock();

    @Shadow
    protected abstract Stream<GlobalPos> getExploredPositions();

    @Unique
    private Optional<Function<RandomSource, ResourceKey<LootTable>>> getLootTable(BlockPos diggedBlockPos) {
        BlockState diggedBlockState = level().getBlockState(diggedBlockPos);
        Map<ResourceKey<Structure>, Function<RandomSource, ResourceKey<LootTable>>> map =
                LOOT_MAP.get(diggedBlockState.getBlock());
        if (map == null) return Optional.empty();
        Registry<Structure> structureRegistry = level().registryAccess().registryOrThrow(Registries.STRUCTURE);
        return map.entrySet().stream()
                .map(e -> {
                    if (((ServerLevel) level())
                            .structureManager()
                            .getStructureWithPieceAt(diggedBlockPos, structureRegistry.get(e.getKey()))
                            .isValid()) {
                        return e.getValue();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                // In case of multiple structures, just choose any. Structures will rarely overlap.
                .findAny();
    }

    @Unique
    private boolean shouldDropIron(BlockState blockState) {
        return blockState.is(BlockTags.SAND)
                || blockState.is(Blocks.GRAVEL)
                || blockState.is(Blocks.SUSPICIOUS_SAND)
                || blockState.is(Blocks.SUSPICIOUS_GRAVEL);
    }

    @Inject(
            method = "dropSeed",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/server/ReloadableServerRegistries$Holder;getLootTable(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/level/storage/loot/LootTable;"),
            cancellable = true)
    private void dropIronAndSusify(CallbackInfo ci) {
        BlockPos diggedBlockPos = getHeadBlock().below();
        BlockState blockState = level().getBlockState(diggedBlockPos);
        // Return to digging regular seed from grass
        if (!(SkyAdditionsSettings.doSuspiciousSniffers && shouldDropIron(blockState))) return;

        if (SkyAdditionsSettings.ironFromSniffers) {
            ItemStack itemStack = new ItemStack(Items.IRON_NUGGET);
            ItemEntity itemEntity = new ItemEntity(
                    level(),
                    getHeadBlock().getX(),
                    getHeadBlock().getY(),
                    getHeadBlock().getZ(),
                    itemStack);
            itemEntity.setDefaultPickUpDelay();
            level().addFreshEntity(itemEntity);
            playSound(SoundEvents.SNIFFER_DROP_SEED, 1.0F, 1.0F);
        }

        // Try to do conversion
        Optional<Function<RandomSource, ResourceKey<LootTable>>> archLootTable = getLootTable(diggedBlockPos);
        if (SkyAdditionsSettings.doSuspiciousSniffers
                && archLootTable.isPresent()
                && level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
                && level().getRandom().nextFloat() < 0.1) {
            Block susBlock = level().getBlockState(diggedBlockPos).is(Blocks.SAND)
                    ? Blocks.SUSPICIOUS_SAND
                    : Blocks.SUSPICIOUS_GRAVEL;
            level().setBlockAndUpdate(diggedBlockPos, susBlock.defaultBlockState());
            ResourceKey<LootTable> lootTable = archLootTable.get().apply(level().getRandom());
            level().getBlockEntity(diggedBlockPos, BlockEntityType.BRUSHABLE_BLOCK)
                    .ifPresent(e -> e.setLootTable(lootTable, level().random.nextLong()));
        }
        ci.cancel();
    }

    @Inject(method = "canDig(Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    private void canSusify(BlockPos digPos, CallbackInfoReturnable<Boolean> cir) {
        if (!SkyAdditionsSettings.doSuspiciousSniffers) return;

        GlobalPos globalDigPos = GlobalPos.of(level().dimension(), digPos);
        if (getExploredPositions().noneMatch(globalDigPos::equals)) {
            BlockState blockState = level().getBlockState(getHeadBlock().below());
            if (shouldDropIron(blockState)) {
                if (Optional.ofNullable(getNavigation().createPath(digPos, 1))
                        .map(Path::canReach)
                        .orElse(false)) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
