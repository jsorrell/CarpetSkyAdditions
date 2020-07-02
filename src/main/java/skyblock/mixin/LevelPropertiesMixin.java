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
    private GeneratorOptions field_25425;

    // Removes the annoying "This is experimental; Thar Be Dragons" message before starting any world with a skyblock chunk generator
    @Environment(EnvType.CLIENT)
    @Inject(method = "method_29588", at = @At("HEAD"), cancellable = true)
    private void markAsStable(CallbackInfoReturnable<Lifecycle> cir) {
        if (this.field_25425.getChunkGenerator() instanceof SkyblockChunkGenerator) {
            cir.setReturnValue(Lifecycle.stable());
        }
    }
}
