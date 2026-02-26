package archives.tater.xom.datagen;

import archives.tater.xom.registry.XomItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends FabricRecipeProvider {
    public RecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    private void offerSmeltAndSmoke(RecipeExporter exporter, ItemConvertible input, RecipeCategory category, ItemConvertible output, float experience) {
        offerSmelting(exporter, List.of(input), category, output, experience, 20, null);
        offerFoodCookingRecipe(exporter, "smoker", RecipeSerializer.SMOKING, SmokingRecipe::new, 10, input, output, experience / 2);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, XomItems.DUCT_TAPE_ROLL)
                .pattern("###")
                .pattern("#@#")
                .pattern("###")
                .input('#', Items.PAPER)
                .input('@', Items.SLIME_BALL)
                .criterion(hasItem(Items.SLIME_BALL), conditionsFromItem(Items.SLIME_BALL))
                .offerTo(recipeExporter);

        offerCompactingRecipe(recipeExporter, RecipeCategory.DECORATIONS, XomItems.KEVIN_CORE, XomItems.DUCT_TAPE);

        offerSmeltAndSmoke(recipeExporter, XomItems.POLYCARB_SHEET, RecipeCategory.DECORATIONS, XomItems.SMOKED_POLYCARB, 0f);
    }
}
