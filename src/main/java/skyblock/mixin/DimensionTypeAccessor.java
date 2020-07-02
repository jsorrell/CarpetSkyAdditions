package skyblock.mixin;

import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DimensionType.class)
public interface DimensionTypeAccessor {
    @Accessor("THE_NETHER")
    static DimensionType getTheNether() {
        throw new NotImplementedException("Failed to apply mixin DimensionTypeAccessor");
    }

    @Accessor("THE_END")
    static DimensionType getTheEnd() {
        throw new NotImplementedException("Failed to apply mixin DimensionTypeAccessor");
    }
}
