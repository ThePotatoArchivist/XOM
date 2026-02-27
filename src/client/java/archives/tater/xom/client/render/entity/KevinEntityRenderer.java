package archives.tater.xom.client.render.entity;

import archives.tater.xom.Xom;
import archives.tater.xom.XomClient;
import archives.tater.xom.client.model.entity.KevinEntityModel;
import archives.tater.xom.entity.KevinEntity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import org.joml.Quaternionf;

import static net.minecraft.util.math.MathHelper.TAU;

public class KevinEntityRenderer extends EntityRenderer<KevinEntity> {
    public static final Identifier TEXTURE = Xom.id("textures/item/kevin.png");
    private final KevinEntityModel<KevinEntity> model;

    public static final float SHAKE_DISTANCE = 0.5f / 16;
    public static final float ROTATION_RATE = TAU / (6 * 20);

    public KevinEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        model = new KevinEntityModel<>(ctx.getPart(XomClient.KEVIN));
    }

    @Override
    public void render(KevinEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        var scale = entity.getScale() / 2;
        matrices.scale(scale, scale, scale);

        matrices.translate(0, 0.5, 0);
        matrices.scale(1, -1, 1);

        if (entity.isImploding()) {
            var implodeTicks = entity.getImplodeTicks(tickDelta);

            var random = entity.getRandom();
            var shakeDistance = SHAKE_DISTANCE * implodeTicks / KevinEntity.IMPLODE_TICKS;
            matrices.translate(
                    2 * shakeDistance * random.nextFloat() - shakeDistance,
                    2 * shakeDistance * random.nextFloat() - shakeDistance,
                    2 * shakeDistance * random.nextFloat() - shakeDistance
            );

            var spinF = implodeTicks / 16;
            var spin = spinF * spinF * spinF;
            matrices.multiply(new Quaternionf().rotateAxis(ROTATION_RATE * spin, entity.rotationAxis));
//            matrices.multiply(RotationAxis.POSITIVE_X.rotation(ROTATION_RATE * spin));
//            matrices.multiply(RotationAxis.POSITIVE_Z.rotation(0.8537f * ROTATION_RATE * spin));
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotation(1.3794f * ROTATION_RATE * spin));

            var implodeScale = entity.getImplodeScale(tickDelta);
            matrices.scale(implodeScale, implodeScale, implodeScale);
        }

        matrices.translate(0, -0.5, 0);

        model.render(matrices, vertexConsumers.getBuffer(model.getLayer(TEXTURE)), light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(KevinEntity entity) {
        return TEXTURE;
    }
}
