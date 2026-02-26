package archives.tater.xom.datagen;

import archives.tater.xom.block.ConeBlock;
import archives.tater.xom.block.DuctTapeBlock;
import archives.tater.xom.registry.XomBlocks;
import archives.tater.xom.registry.XomItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import net.minecraft.data.client.*;
import net.minecraft.data.client.VariantSettings.Rotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

import static archives.tater.xom.datagen.BlockStateVariantBuilder.blockStateVariant;
import static net.minecraft.data.client.ModelIds.getBlockModelId;
import static net.minecraft.data.client.ModelIds.getBlockSubModelId;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerBuiltinWithParticle(XomBlocks.LIQUID_POLYCARB, Identifier.ofVanilla("block/water_still"));

        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(XomBlocks.DUCT_TAPE)
                .coordinate(BlockStateVariantMap.create(DuctTapeBlock.AXIS)
                        .register(Direction.Axis.X, blockStateVariant()
                                .model(getBlockModelId(XomBlocks.DUCT_TAPE))
                        )
                        .register(Direction.Axis.Z, blockStateVariant()
                                .model(getBlockModelId(XomBlocks.DUCT_TAPE))
                                .y(Rotation.R90)
                        )
                )
        );

        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(XomBlocks.CONE)
                .coordinate(BlockStateVariantMap.create(ConeBlock.STACKED).register(stacked ->
                        blockStateVariant().model(getBlockSubModelId(XomBlocks.CONE, "_" + (stacked - 3)))
                ))
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(XomItems.CONE, new Model(Optional.of(getBlockSubModelId(XomBlocks.CONE, "_0")), Optional.empty()));
        itemModelGenerator.register(XomItems.DUCT_TAPE, Models.GENERATED);
        itemModelGenerator.register(XomItems.DUCT_TAPE_ROLL, Models.GENERATED);
        itemModelGenerator.register(XomItems.POLYCARB_SHEET, Models.GENERATED);
        itemModelGenerator.register(XomItems.SMOKED_POLYCARB, Models.GENERATED);
        itemModelGenerator.register(XomItems.POLYCARB_BUCKET, Models.GENERATED);
    }
}
