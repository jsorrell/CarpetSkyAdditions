package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.advancements.criterion.SkyAdditionsCriteriaTriggers;
import com.jsorrell.carpetskyadditions.helpers.GeodeGenerator;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {
    @Inject(method = "randomTick", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "HEAD"))
    private void tryCreateGeode(Level level, BlockPos pos, FluidState state, RandomSource random, CallbackInfo ci) {
        if (SkyAdditionsSettings.renewableBuddingAmethysts) {
            if (random.nextInt(GeodeGenerator.CONVERSION_RATE) == 0) {
                if (GeodeGenerator.checkGeodeFormation(level, pos)) {
                    level.setBlockAndUpdate(pos, Blocks.BUDDING_AMETHYST.defaultBlockState());
                    level.playSound(
                            null,
                            pos,
                            SoundEvents.LAVA_EXTINGUISH,
                            SoundSource.BLOCKS,
                            0.5f,
                            2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f);
                    level.playSound(
                            null,
                            pos,
                            SoundEvents.AMETHYST_BLOCK_PLACE,
                            SoundSource.BLOCKS,
                            1.0f,
                            0.5f + level.random.nextFloat() * 1.2f);

                    AABB criteriaTriggerBox = new AABB(pos).inflate(50, 20, 50);
                    level.getEntitiesOfClass(ServerPlayer.class, criteriaTriggerBox)
                            .forEach(SkyAdditionsCriteriaTriggers.GENERATE_GEODE::trigger);
                }
            }
        }
    }
}
