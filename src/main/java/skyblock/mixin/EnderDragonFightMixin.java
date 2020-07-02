package skyblock.mixin;

import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skyblock.SkyblockChunkGenerator;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    private static final int exitPortalHeight = 60;

    @Shadow
    private BlockPos exitPortalLocation;

    @Shadow
    @Final
    private ServerWorld world;

    @Inject(method = "generateEndPortal", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/gen/feature/ConfiguredFeature;generate(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z",
            shift = At.Shift.BEFORE))
    private void adjustExitPortalLocation(boolean open, CallbackInfo ci) {
        if (this.world.getChunkManager().getChunkGenerator() instanceof SkyblockChunkGenerator) {
            this.exitPortalLocation = new BlockPos(this.exitPortalLocation.getX(), exitPortalHeight, this.exitPortalLocation.getZ());
        }
    }
}
