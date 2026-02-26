package archives.tater.xom.datagen;

import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantSettings.Rotation;
import net.minecraft.util.Identifier;

public class BlockStateVariantBuilder extends BlockStateVariant {

    public BlockStateVariantBuilder model(Identifier model) {
        put(VariantSettings.MODEL, model);
        return this;
    }

    public BlockStateVariantBuilder uvLock(boolean uvLock) {
        put(VariantSettings.UVLOCK, uvLock);
        return this;
    }

    public BlockStateVariantBuilder weight(int weight) {
        put(VariantSettings.WEIGHT, weight);
        return this;
    }

    public BlockStateVariantBuilder x(Rotation x) {
        put(VariantSettings.X, x);
        return this;
    }

    public BlockStateVariantBuilder y(Rotation y) {
        put(VariantSettings.Y, y);
        return this;
    }

    public static BlockStateVariantBuilder blockStateVariant() {
        return new BlockStateVariantBuilder();
    }
}
