package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.function.Predicate;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    @Shadow
    private BlockState blockState;

    public FallingBlockEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    private void compactEntityToDiamonds(Entity entity) {
        if (entity instanceof ItemEntity e
                && e.getItem().is(Items.COAL_BLOCK)
                && 64 <= e.getItem().getCount()) {
            int numCoalBlocks = e.getItem().getCount();
            int numDiamonds = numCoalBlocks / 64;
            int remainingCoalBlocks = numCoalBlocks % 64;
            ItemEntity diamondEntity =
                    new ItemEntity(e.level(), e.getX(), e.getY(), e.getZ(), new ItemStack(Items.DIAMOND, numDiamonds));
            diamondEntity.setDefaultPickUpDelay();
            e.level().addFreshEntity(diamondEntity);

            e.getItem().setCount(remainingCoalBlocks);
        }
    }

    @Inject(
            method = "causeFallDamage",
            at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"))
    private void compactCoalToDiamonds(
            float fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (SkyAdditionsSettings.renewableDiamonds) {
            if (blockState.is(BlockTags.ANVIL)) {
                Predicate<Entity> coalBlockPredicate = entity -> entity instanceof ItemEntity itemEntity
                        && itemEntity.getItem().is(Items.COAL_BLOCK);
                this.level()
                        .getEntities(this, this.getBoundingBox(), coalBlockPredicate)
                        .forEach(this::compactEntityToDiamonds);
            }
        }
    }
}
