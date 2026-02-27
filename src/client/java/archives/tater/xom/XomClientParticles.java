package archives.tater.xom;

import archives.tater.xom.registry.XomFluids;
import archives.tater.xom.registry.XomParticles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;

import org.jetbrains.annotations.Nullable;

public class XomClientParticles {
    private static ParticleFactoryRegistry.PendingParticleFactory<SimpleParticleType> blockLeak(BlockLeakParticleFactory factory, float red, float green, float blue, Fluid fluid) {
        return setSprite((parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            var particle = factory.createParticle(world, x, y, z, fluid);
            if (particle == null) return null;
            particle.setColor(red, green, blue);
            return particle;
        });
    }

    private static ParticleFactoryRegistry.PendingParticleFactory<SimpleParticleType> chainBlockLeak(ChainBlockLeakParticleFactory factory, float red, float green, float blue, Fluid fluid, ParticleEffect nextParticle) {
        return setSprite((parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            var particle = factory.createParticle(world, x, y, z, fluid, nextParticle);
            if (particle == null) return null;
            particle.setColor(red, green, blue);
            return particle;
        });
    }

    private static <T extends ParticleEffect> ParticleFactoryRegistry.PendingParticleFactory<T> setSprite(ParticleFactory<T> factory) {
        return provider ->
                (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
                        var particle = factory.createParticle(parameters, world, x, y, z, velocityX, velocityY, velocityZ);
                        if (particle instanceof SpriteBillboardParticle spriteBillboardParticle)
                            spriteBillboardParticle.setSprite(provider);
                        return particle;
                };
    }

    @FunctionalInterface
    private interface BlockLeakParticleFactory {
        @Nullable BlockLeakParticle createParticle(ClientWorld world, double x, double y, double z, Fluid fluid);
    }

    @FunctionalInterface
    private interface ChainBlockLeakParticleFactory {
        @Nullable BlockLeakParticle createParticle(ClientWorld world, double x, double y, double z, Fluid fluid, ParticleEffect nextParticle);
    }

    public static void init() {
        ParticleFactoryRegistry.getInstance().register(XomParticles.DRIPPING_POLYCARB, chainBlockLeak(BlockLeakParticle.Dripping::new, 0.97f, 0.58f, 0.13f, XomFluids.LIQUID_POLYCARB, XomParticles.FALLING_POLYCARB));
        ParticleFactoryRegistry.getInstance().register(XomParticles.FALLING_POLYCARB, chainBlockLeak(PolycarbDripParticle::new,        0.97f, 0.58f, 0.13f, XomFluids.LIQUID_POLYCARB, XomParticles.LANDING_POLYCARB));
        ParticleFactoryRegistry.getInstance().register(XomParticles.LANDING_POLYCARB, blockLeak(BlockLeakParticle.Landing::new,        0.97f, 0.58f, 0.13f, XomFluids.LIQUID_POLYCARB));
    }
}
