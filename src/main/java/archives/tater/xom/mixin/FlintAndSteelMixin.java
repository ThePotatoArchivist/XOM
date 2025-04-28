package archives.tater.xom.mixin;

import archives.tater.xom.ConeSummoning;
import net.minecraft.block.Blocks;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelMixin {
	@Inject(
			method = "useOnBlock",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ActionResult;success(Z)Lnet/minecraft/util/ActionResult;")
	)
	private void init(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		if (context.getWorld() instanceof ServerWorld serverWorld && serverWorld.getBlockState(context.getBlockPos()).isOf(Blocks.ORANGE_CANDLE)) {
			ConeSummoning.checkSummon(serverWorld, context.getBlockPos());
		}
	}
}