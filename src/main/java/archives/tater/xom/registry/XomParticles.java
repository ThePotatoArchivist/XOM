package archives.tater.xom.registry;

import archives.tater.xom.Xom;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;

import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class XomParticles {
    public static <T extends ParticleType<?>> T register(String path, T type) {
        return Registry.register(Registries.PARTICLE_TYPE, Xom.id(path), type);
    }

    public static SimpleParticleType registerSimple(String path) {
        return register(path, FabricParticleTypes.simple());
    }

    public static SimpleParticleType DRIPPING_POLYCARB = registerSimple("dripping_polycarb");
    public static SimpleParticleType FALLING_POLYCARB = registerSimple("falling_polycarb");
    public static SimpleParticleType LANDING_POLYCARB = registerSimple("landing_polycarb");

    public static void init() {

    }
}
