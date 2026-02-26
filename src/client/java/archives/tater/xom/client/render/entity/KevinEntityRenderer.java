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

public class KevinEntityRenderer extends EntityRenderer<KevinEntity> {
    public static final Identifier TEXTURE = Xom.id("textures/item/kevin.png");
    private final KevinEntityModel<KevinEntity> model;

    public static final float SHAKE_DISTANCE = 0.5f / 16;

    public KevinEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        model = new KevinEntityModel<>(ctx.getPart(XomClient.KEVIN));
    }

    @Override
    public void render(KevinEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        var scale = entity.getVisualScale(tickDelta) / 2;
        matrices.scale(scale, -scale, scale);
        matrices.translate(0, -1, 0);
        if (entity.isImploding()) {
            var random = entity.getRandom();
            matrices.translate(
                    2 * SHAKE_DISTANCE * random.nextFloat() - SHAKE_DISTANCE,
                    2 * SHAKE_DISTANCE * random.nextFloat() - SHAKE_DISTANCE,
                    2 * SHAKE_DISTANCE * random.nextFloat() - SHAKE_DISTANCE
            );
        }
        model.render(matrices, vertexConsumers.getBuffer(model.getLayer(TEXTURE)), light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(KevinEntity entity) {
        return TEXTURE;
    }
}
