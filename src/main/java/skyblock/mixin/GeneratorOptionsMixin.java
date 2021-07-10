package skyblock.mixin;

import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import skyblock.SkyBlockUtils;

import java.util.Properties;

@Mixin(GeneratorOptions.class)
@SuppressWarnings("unused")
public class GeneratorOptionsMixin {
    @Inject(method = "fromProperties", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    @SuppressWarnings("unused")
    private static void addSkyblockGeneratorOptionWhenLoadingProperties(DynamicRegistryManager drm, Properties properties, CallbackInfoReturnable<GeneratorOptions> cir, String string, String string2, boolean generateStructures, String string4, String generatorSettingsValue, long seed, Registry<DimensionType> dimensionTypeRegistry, Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry) {
        if ("skyblock".equals(generatorSettingsValue)) {
            SimpleRegistry<DimensionOptions> dimensionOptions = SkyBlockUtils.getSkyblockSimpleRegistry(dimensionTypeRegistry, biomeRegistry, settingsRegistry, seed);
            SimpleRegistry<DimensionOptions> dimensionOptionsWithOverworld = GeneratorOptions.getRegistryWithReplacedOverworldGenerator(dimensionTypeRegistry, dimensionOptions, SkyBlockUtils.createOverworldGenerator(biomeRegistry, settingsRegistry, seed));
            GeneratorOptions generatorOptions = new GeneratorOptions(seed, generateStructures, false, dimensionOptionsWithOverworld);
            cir.setReturnValue(generatorOptions);
        }
    }
}
