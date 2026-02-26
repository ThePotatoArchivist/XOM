package archives.tater.xom.client.model.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;

public class KevinEntityModel<E extends Entity> extends SinglePartEntityModel<E> {

    private final ModelPart root;

    public KevinEntityModel(ModelPart root) {
        this.root = root;
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    public static TexturedModelData getTexturedModelData() {
        var modelData = new ModelData();
        var root = modelData.getRoot();
        root.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-8, 0, -8, 16, 16, 16),
                ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }
}
