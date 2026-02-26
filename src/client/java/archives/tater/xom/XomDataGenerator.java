package archives.tater.xom;

import archives.tater.xom.datagen.*;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class XomDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(BlockLootTableGenerator::new);
        pack.addProvider(FluidTagGenerator::new);
        pack.addProvider(EntityTagGenerator::new);
        pack.addProvider(LangGenerator::new);
        pack.addProvider(RecipeGenerator::new);
    }
}
