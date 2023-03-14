package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.RamImpactTask;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Mixin(RamImpactTask.class)
public abstract class RamImpactTaskMixin<E extends PathAwareEntity> extends MultiTickTask<E> {

  @Shadow
  @Final
  private Function<GoatEntity, SoundEvent> impactSoundFactory;

  @Shadow
  protected abstract void finishRam(ServerWorld world, GoatEntity goat);

  public RamImpactTaskMixin(Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState) {
    super(requiredMemoryState);
  }

  @Inject(
    method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/GoatEntity;J)V",
    locals = LocalCapture.CAPTURE_FAILSOFT,
    at =
    @At(
      value = "INVOKE",
      target = "Lnet/minecraft/entity/ai/brain/Brain;getOptionalRegisteredMemory(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Ljava/util/Optional;",
      ordinal = 0),
    cancellable = true)
  private void breakOpenNetherWart(ServerWorld world, GoatEntity rammer, long time, CallbackInfo ci) {
    if (SkyAdditionsSettings.rammingWart && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
      Optional<BlockPos> optionalWartPos = shouldBreakNetherWart(world, rammer);
      if (optionalWartPos.isPresent()) {
        BlockPos wartPos = optionalWartPos.get();
        world.playSoundFromEntity(null, rammer, this.impactSoundFactory.apply(rammer), SoundCategory.HOSTILE, 1.0f, 1.0f);

        boolean blockRemoved = world.removeBlock(wartPos, false);
        if (blockRemoved) {
          if (!world.isClient()) {
            Block.dropStack(world, wartPos, new ItemStack(Items.NETHER_WART, world.random.nextInt(2) + 1));
          }
          world.emitGameEvent(rammer, GameEvent.BLOCK_DESTROY, wartPos);
          world.playSound(null, wartPos, SoundEvents.BLOCK_WART_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
        this.finishRam(world, rammer);
        ci.cancel();
      }
    }
  }

  private Optional<BlockPos> shouldBreakNetherWart(ServerWorld world, GoatEntity goat) {
    Vec3d movementVector = goat.getVelocity().multiply(1.0, 0.0, 1.0).normalize();
    BlockPos hitPos = BlockPos.ofFloored(goat.getPos().add(movementVector));
    return world.getBlockState(hitPos).isOf(Blocks.NETHER_WART_BLOCK) ? Optional.of(hitPos) : Optional.empty();
  }
}
