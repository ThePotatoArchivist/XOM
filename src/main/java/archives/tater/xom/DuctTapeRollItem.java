package archives.tater.xom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;

public class DuctTapeRollItem extends Item {
    public DuctTapeRollItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var result = Xom.DUCT_TAPE_ITEM.getDefaultStack().useOnBlock(new ItemUsageContext(context.getWorld(),
                context.getPlayer(),
                context.getHand(),
                Xom.DUCT_TAPE_ITEM.getDefaultStack(),
                new BlockHitResult(context.getHitPos(), context.getSide(), context.getBlockPos(), context.hitsInsideBlock()))
        );
        if (result.isAccepted() && context.getPlayer() != null)
            context.getStack().damage(1, context.getPlayer(), LivingEntity.getSlotForHand(context.getHand()));
        return result;
    }
}
