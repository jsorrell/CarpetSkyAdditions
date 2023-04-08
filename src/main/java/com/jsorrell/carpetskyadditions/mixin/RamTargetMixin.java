package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.RamTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RamTarget.class)
public abstract class RamTargetMixin<E extends PathfinderMob> extends Behavior<E> {

    @Shadow
    @Final
    private Function<Goat, SoundEvent> getImpactSound;

    @Shadow
    protected abstract void finishRam(ServerLevel level, Goat goat);

    public RamTargetMixin(Map<MemoryModuleType<?>, MemoryStatus> memories) {
        super(memories);
    }

    @Inject(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/goat/Goat;J)V",
            locals = LocalCapture.CAPTURE_FAILSOFT,
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/entity/ai/Brain;getMemory(Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;)Ljava/util/Optional;",
                            ordinal = 0),
            cancellable = true)
    private void breakOpenNetherWart(ServerLevel level, Goat rammer, long gameTime, CallbackInfo ci) {
        if (SkyAdditionsSettings.rammingWart && level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            Optional<BlockPos> optionalWartPos = shouldBreakNetherWart(level, rammer);
            if (optionalWartPos.isPresent()) {
                BlockPos wartPos = optionalWartPos.get();
                level.playSound(null, rammer, getImpactSound.apply(rammer), SoundSource.HOSTILE, 1.0f, 1.0f);

                boolean blockRemoved = level.removeBlock(wartPos, false);
                if (blockRemoved) {
                    if (!level.isClientSide) {
                        Block.popResource(
                                level, wartPos, new ItemStack(Items.NETHER_WART, level.random.nextInt(2) + 1));
                    }
                    level.gameEvent(rammer, GameEvent.BLOCK_DESTROY, wartPos);
                    level.playSound(null, wartPos, SoundEvents.WART_BLOCK_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                finishRam(level, rammer);
                ci.cancel();
            }
        }
    }

    private Optional<BlockPos> shouldBreakNetherWart(ServerLevel level, Goat goat) {
        Vec3 movementVector = goat.getDeltaMovement().multiply(1, 0, 1).normalize();
        BlockPos hitPos = BlockPos.containing(goat.position().add(movementVector));
        return level.getBlockState(hitPos).is(Blocks.NETHER_WART_BLOCK) ? Optional.of(hitPos) : Optional.empty();
    }
}
