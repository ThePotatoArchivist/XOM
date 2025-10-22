package archives.tater.xom;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;

public class XomClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		BlockRenderLayerMap.INSTANCE.putBlock(Xom.CONE_BLOCK, RenderLayer.getCutout());
        EntityRendererRegistry.register(Xom.CONE_ENTITY, FallingBlockEntityRenderer::new);

        FluidRenderHandlerRegistry.INSTANCE.register(Xom.LIQUID_POLYCARB, Xom.FLOWING_LIQUID_POLYCARB, new SimpleFluidRenderHandler(
                SimpleFluidRenderHandler.LAVA_STILL,
                SimpleFluidRenderHandler.LAVA_FLOWING,
                0xc0d5db
        ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), Xom.LIQUID_POLYCARB, Xom.FLOWING_LIQUID_POLYCARB);

    }
}