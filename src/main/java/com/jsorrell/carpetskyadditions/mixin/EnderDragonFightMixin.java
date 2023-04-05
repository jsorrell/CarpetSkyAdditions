package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.gen.SkyBlockChunkGenerator;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndDragonFight.class)
public class EnderDragonFightMixin {
    @Shadow
    private boolean previouslyKilled;

    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    private @Nullable BlockPos portalLocation;

    @Inject(method = "spawnExitPortal", at = @At(value = "HEAD"))
    private void setExitPortalLocation(boolean previouslyKilled, CallbackInfo ci) {
        if (this.level.getChunkSource().getGenerator() instanceof SkyBlockChunkGenerator chunkGenerator) {
            if (portalLocation == null) {
                int y = chunkGenerator.getHeightInGround(
                        EndPodiumFeature.END_PODIUM_LOCATION.getX(),
                        EndPodiumFeature.END_PODIUM_LOCATION.getZ(),
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        level);
                portalLocation = EndPodiumFeature.END_PODIUM_LOCATION.atY(y);
            }
        }
    }

    @Inject(
            method = "setDragonKilled",
            at =
                    @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/world/level/dimension/end/EndDragonFight;previouslyKilled:Z",
                            opcode = Opcodes.PUTFIELD))
    private void spawnShulkerOnDragonReKill(EnderDragon dragon, CallbackInfo ci) {
        if (SkyAdditionsSettings.shulkerSpawnsOnDragonKill) {
            // On top of bedrock pillar
            BlockPos shulkerPosition = this.portalLocation.offset(0, 4, 0);
            if (this.previouslyKilled
                    && this.level.getBlockState(shulkerPosition).isAir()) {
                Shulker shulker =
                        EntityType.SHULKER.create(level, null, null, shulkerPosition, MobSpawnType.EVENT, true, false);
                if (shulker != null && level.noCollision(shulker)) {
                    level.addFreshEntity(shulker);
                }
            }
        }
    }
}
