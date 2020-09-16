package skyblock.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import skyblock.SkyBlockUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(GeneratorType.class)
public abstract class GeneratorTypeMixin {
    @Shadow
    @Final
    private Text translationKey;

    @Shadow
    @Final
    protected static List<GeneratorType> VALUES;

    public boolean isSkyblock() {
        return ((TranslatableText)this.translationKey).getKey().equals("generator.skyblock.skyblock");
    }

    @Inject(method = "<clinit>", at=@At("TAIL"))
    @SuppressWarnings("unused")
    private static void addSkyblockGenerator(CallbackInfo ci) {
        // Return an instance of GeneratorType.DEFAULT which acts differently when it's called skyblock
        Class<?> clazz = GeneratorType.DEFAULT.getClass();
        GeneratorType skyblockGeneratorType;
        try {
            Constructor<?> ctor = clazz.getDeclaredConstructor(String.class);
            skyblockGeneratorType = (GeneratorType)ctor.newInstance("skyblock.skyblock");
        } catch (NoSuchMethodException|IllegalAccessException|InstantiationException|InvocationTargetException e){
            throw new RuntimeException(e);
        }

        VALUES.add(skyblockGeneratorType);
    }

    @Inject(method = "createDefaultOptions", at = @At("TAIL"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    @SuppressWarnings("unused")
    private void setToSkyblockChunkGenerator(DynamicRegistryManager.Impl impl, long seed, boolean generateStructures, boolean bonusChest, CallbackInfoReturnable<GeneratorOptions> cir, Registry<Biome> biomeRegistry, Registry<DimensionType> dimensionTypeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry) {
        if (this.isSkyblock()) {
            cir.setReturnValue(new GeneratorOptions(seed, generateStructures, bonusChest, GeneratorOptions.method_28608(dimensionTypeRegistry, SkyBlockUtils.getSkyblockSimpleRegistry(dimensionTypeRegistry, biomeRegistry, settingsRegistry, seed), SkyBlockUtils.createOverworldGenerator(biomeRegistry, settingsRegistry, seed))));
        }
    }
}
