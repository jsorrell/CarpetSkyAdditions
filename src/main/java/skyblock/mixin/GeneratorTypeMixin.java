package skyblock.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
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

    @Shadow
    protected abstract ChunkGenerator method_29076(long l);

    public boolean isSkyblock() {
        return ((TranslatableText)this.translationKey).getKey().equals("generator.skyblock.skyblock");
    }

    @Inject(method = "<clinit>", at=@At("TAIL"))
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

    @Inject(method = "method_29077", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void setToSkyblockChunkGenerator(RegistryTracker.Modifiable modifiable, long seed, boolean generateStructures, boolean bonusChest, CallbackInfoReturnable<GeneratorOptions> cir) {
        if (this.isSkyblock()) {
            cir.setReturnValue(new GeneratorOptions(seed, generateStructures, bonusChest, GeneratorOptions.method_28608(SkyBlockUtils.getSkyblockSimpleRegistry(seed), SkyBlockUtils.createOverworldGenerator(seed))));
        }
    }
}
