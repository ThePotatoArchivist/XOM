package archives.tater.xom;

import archives.tater.xom.registry.XomBlocks;
import archives.tater.xom.registry.XomEntities;
import archives.tater.xom.registry.XomFluids;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class XomClient implements ClientModInitializer {
    public static final EntityModelLayer KEVIN = new EntityModelLayer(Xom.id("kevin"), "main");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		BlockRenderLayerMap.INSTANCE.putBlock(XomBlocks.CONE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(XomBlocks.DUCT_TAPE, RenderLayer.getCutout());

        EntityRendererRegistry.register(XomEntities.CONE, FallingBlockEntityRenderer::new);
        EntityRendererRegistry.register(XomEntities.KEVIN, KevinEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(KEVIN, KevinEntityModel::getTexturedModelData);

        FluidRenderHandlerRegistry.INSTANCE.register(XomFluids.LIQUID_POLYCARB, XomFluids.FLOWING_LIQUID_POLYCARB, new SimpleFluidRenderHandler(
                SimpleFluidRenderHandler.LAVA_STILL,
                SimpleFluidRenderHandler.LAVA_FLOWING,
                0xc0d5db
        ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), XomFluids.LIQUID_POLYCARB, XomFluids.FLOWING_LIQUID_POLYCARB);

    }
}