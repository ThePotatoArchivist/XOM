package archives.tater.xom.datagen;

import archives.tater.xom.item.SmokedPolycarbSheetItem;
import archives.tater.xom.registry.XomBlocks;
import archives.tater.xom.registry.XomEntities;
import archives.tater.xom.registry.XomItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LangGenerator extends FabricLanguageProvider {
    public LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(XomBlocks.CONE, "Cone");
        translationBuilder.add(XomBlocks.LIQUID_POLYCARB, "Liquid Polycarb");
        translationBuilder.add(XomBlocks.DUCT_TAPE, "Duct Tape");
        translationBuilder.add(XomItems.POLYCARB_SHEET, "Polycarb Sheet");
        translationBuilder.add(XomItems.SMOKED_POLYCARB, "Smoked Polycarb");
        translationBuilder.add(SmokedPolycarbSheetItem.TOOLTIP, "Useless");
        translationBuilder.add(XomItems.DUCT_TAPE_ROLL, "Roll of Duct Tape");
        translationBuilder.add(XomItems.POLYCARB_BUCKET, "Bucket of Liquid Polycarb");
        translationBuilder.add(XomItems.KEVIN_CORE, "Kevin Core");
        translationBuilder.add(XomEntities.CONE, "Cone");
        translationBuilder.add(XomEntities.KEVIN, "Kevin");
        translationBuilder.add("death.attack.xom.cone", "%s was conespicuously killed by %s");
        translationBuilder.add("death.attack.xom.cone.item", "%s was conespicuously killed by %s using %s");
        translationBuilder.add("subtitles.entity.xom.cone.throw", "Cone flies");
        translationBuilder.add("subtitles.entity.xom.polycarb_hit", "Polycarb rattles");
    }
}
