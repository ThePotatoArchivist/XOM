package archives.tater.xom.mixin;

import archives.tater.xom.Xom;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;

import org.jetbrains.annotations.Nullable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    @Inject(
            method = "canAcceptRecipeOutput",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void allowPolycarbRecipe(DynamicRegistryManager registryManager, @Nullable RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int count, CallbackInfoReturnable<Boolean> cir) {
        if (slots.getFirst().isOf(Xom.POLYCARB_SHEET) && slots.get(1).isOf(Items.BUCKET))
            cir.setReturnValue(true);
    }

    @Inject(
            method = "craftRecipe",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
    )
    private static void performPolycarbRecipe(DynamicRegistryManager registryManager, @Nullable RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int count, CallbackInfoReturnable<Boolean> cir) {
        if (slots.getFirst().isOf(Xom.POLYCARB_SHEET) && slots.get(1).isOf(Items.BUCKET))
            slots.set(1, Xom.POLYCARB_BUCKET.getDefaultStack());
    }

    @WrapWithCondition(
            method = "craftRecipe",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;", ordinal = 0)
    )
    private static boolean preventOutput(DefaultedList<ItemStack> slots, int index, Object element) {
        return !slots.getFirst().isOf(Xom.POLYCARB_SHEET) || !slots.get(1).isOf(Items.BUCKET);
    }

    @WrapWithCondition(
            method = "craftRecipe",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;increment(I)V")
    )
    private static boolean preventOutput(ItemStack instance, int amount, @Local(argsOnly = true) DefaultedList<ItemStack> slots) {
        return !slots.getFirst().isOf(Xom.POLYCARB_SHEET) || !slots.get(1).isOf(Items.BUCKET);
    }
}
