package archives.tater.xom;

import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import static net.minecraft.util.math.MathHelper.nextBetween;

public class PolycarbDripParticle extends BlockLeakParticle.ContinuousFalling {
    public PolycarbDripParticle(ClientWorld world, double x, double y, double z, Fluid fluid, ParticleEffect nextParticle) {
        super(world, x, y, z, fluid, nextParticle);
    }

    @Override
    protected void updateVelocity() {
        super.updateVelocity();
        if (onGround)
            world.playSound(x, y, z, SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_LAVA, SoundCategory.BLOCKS, nextBetween(random, 0.3F, 1.0F), 1F, false);
    }
}
