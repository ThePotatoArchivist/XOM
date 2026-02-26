package archives.tater.xom.registry;

import archives.tater.xom.Xom;
import archives.tater.xom.fluid.PolycarbFluid;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class XomFluids {

    private static <T extends Fluid> T register(String path, T fluid) {
        return Registry.register(Registries.FLUID, Xom.id(path), fluid);
    }

    public static final FlowableFluid LIQUID_POLYCARB = register("liquid_polycarb", new PolycarbFluid.Still());
    public static final FlowableFluid FLOWING_LIQUID_POLYCARB = register("flowing_liquid_polycarb", new PolycarbFluid.Flowing());

    public static void init() {
        FluidVariantAttributes.register(XomFluids.LIQUID_POLYCARB, new FluidVariantAttributeHandler() {
            @Override
            public Optional<SoundEvent> getFillSound(FluidVariant variant) {
                return Optional.of(SoundEvents.ITEM_BUCKET_FILL_LAVA);
            }

            @Override
            public Optional<SoundEvent> getEmptySound(FluidVariant variant) {
                return Optional.of(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
            }

            @Override
            public int getTemperature(FluidVariant variant) {
                return FluidConstants.LAVA_TEMPERATURE;
            }

            @Override
            public int getViscosity(FluidVariant variant, @Nullable World world) {
                return FluidConstants.LAVA_VISCOSITY;
            }
        });

    }
}
