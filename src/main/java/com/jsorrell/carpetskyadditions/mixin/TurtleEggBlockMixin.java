package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public class TurtleEggBlockMixin {
    // Zombies can normally destroy turtle eggs by attempting to destroy them
    // or just being on top of them too long.
    // But unintentional destroys happen even when the destry egg task is running.
    // We prevent them here to ensure drowneds always replace the turtle egg with the sniffer egg.
    @Inject(
            method = "destroyEgg",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/block/TurtleEggBlock;decreaseEggs(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"),
            cancellable = true)
    private void stopDestroyWhenDrowned(
            Level level, BlockState state, BlockPos pos, Entity entity, int chance, CallbackInfo ci) {
        if (SkyAdditionsSettings.sniffersFromDrowneds && entity instanceof Drowned drowned) {
            ItemStack offhand = drowned.getItemInHand(InteractionHand.OFF_HAND);
            if (offhand.is(Items.SNIFFER_EGG)) {
                ci.cancel();
            }
        }
    }
}
