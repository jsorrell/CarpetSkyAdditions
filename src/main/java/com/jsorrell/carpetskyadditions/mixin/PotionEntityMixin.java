package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "onCollision",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/potion/PotionUtil;getPotionEffects(Lnet/minecraft/item/ItemStack;)Ljava/util/List;"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void convertToDeepslateWithSplashPotion(
            HitResult hitResult, CallbackInfo ci, ItemStack itemStack, Potion potion) {
        if (SkyAdditionsSettings.renewableDeepslateFromSplash) {
            if (potion == Potions.THICK) {
                Vec3d hitPos = hitResult.getType() == HitResult.Type.BLOCK ? hitResult.getPos() : this.getPos();
                BlockPos.stream(Box.of(hitPos, 8.25, 4.25, 8.25)).forEach(pos -> {
                    if (world.getBlockState(pos).isOf(Blocks.STONE)) {
                        double distance = Math.sqrt(pos.toCenterPos().squaredDistanceTo(hitPos));
                        // Probability of conversion is based upon the strength of an applied potion effect,
                        // but it guarantees conversion within 3 euclidean blocks of the collision point
                        if (world.random.nextDouble() < 2.5 - distance / 2) {
                            world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState());
                        }
                    }
                });
            }
        }
    }
}
