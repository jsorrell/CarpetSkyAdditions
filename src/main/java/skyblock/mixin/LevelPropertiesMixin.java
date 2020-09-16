package skyblock.mixin;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import skyblock.SkyblockChunkGenerator;

@Mixin(LevelProperties.class)
public abstract class LevelPropertiesMixin {
    @Shadow
    @Final
    private GeneratorOptions generatorOptions;

    // Removes the annoying "This is experimental; Thar Be Dragons" message before starting any world with a skyblock chunk generator
    @Environment(EnvType.CLIENT)
    @Inject(method = "getLifecycle", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("unused")
    private void markAsStable(CallbackInfoReturnable<Lifecycle> cir) {
        if (this.generatorOptions.getChunkGenerator() instanceof SkyblockChunkGenerator) {
            cir.setReturnValue(Lifecycle.stable());
        }
    }
}
