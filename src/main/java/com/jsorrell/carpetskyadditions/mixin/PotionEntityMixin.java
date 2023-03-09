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
import net.minecraft.util.math.Direction;
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

  @Inject(method = "onCollision",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/PotionUtil;getPotionEffects(Lnet/minecraft/item/ItemStack;)Ljava/util/List;"),
    locals = LocalCapture.CAPTURE_FAILSOFT)
  private void convertToDeepslateWithSplashPotion(HitResult hitResult, CallbackInfo ci, ItemStack itemStack, Potion potion) {
    if (SkyAdditionsSettings.renewableDeepslateFromSplash) {
      if (potion == Potions.THICK) {
        BlockPos.stream(this.getBoundingBox().expand(4.0, 2.0, 4.0)).forEach(mutable -> {
            if (world.getBlockState(mutable).isOf(Blocks.STONE)) {
                world.setBlockState(mutable, Blocks.DEEPSLATE.getDefaultState());
            }
        });
      }
    }
  }
}
