package skyblock.mixin;

import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import skyblock.SkyBlockUtils;

import java.util.Properties;

@Mixin(GeneratorOptions.class)
public class GeneratorOptionsMixin {
    @Inject(method = "fromProperties", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void addSkyblockGeneratorOptionWhenLoadingProperties(Properties properties, CallbackInfoReturnable<GeneratorOptions> cir, String string, String string2, boolean generateStructures, String string4, String generatorSettingsValue, long seed) {
        if ("skyblock".equals(generatorSettingsValue)) {
            GeneratorOptions generatorOptions = new GeneratorOptions(seed, generateStructures, false, GeneratorOptions.method_28608(SkyBlockUtils.getSkyblockSimpleRegistry(seed), SkyBlockUtils.createOverworldGenerator(seed)));
            cir.setReturnValue(generatorOptions);
        }
    }
}
