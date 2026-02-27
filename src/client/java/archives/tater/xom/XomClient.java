package archives.tater.xom;

import archives.tater.xom.client.model.entity.KevinEntityModel;
import archives.tater.xom.client.render.entity.KevinEntityRenderer;
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
import net.minecraft.util.Identifier;

public class XomClient implements ClientModInitializer {
    public static final EntityModelLayer KEVIN = new EntityModelLayer(Xom.id("kevin"), "main");

    public static final Identifier POLYCARB_STILL = Xom.id("block/polycarb_still");
    public static final Identifier POLYCARB_FLOWING = Xom.id("block/polycarb_flow");

    @Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		BlockRenderLayerMap.INSTANCE.putBlock(XomBlocks.CONE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(XomBlocks.DUCT_TAPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), XomFluids.LIQUID_POLYCARB, XomFluids.FLOWING_LIQUID_POLYCARB);

        EntityRendererRegistry.register(XomEntities.CONE, FallingBlockEntityRenderer::new);
        EntityRendererRegistry.register(XomEntities.KEVIN, KevinEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(KEVIN, KevinEntityModel::getTexturedModelData);

        FluidRenderHandlerRegistry.INSTANCE.register(XomFluids.LIQUID_POLYCARB, XomFluids.FLOWING_LIQUID_POLYCARB, new SimpleFluidRenderHandler(
                POLYCARB_STILL,
                POLYCARB_FLOWING
        ));

        XomClientParticles.init();
    }
}