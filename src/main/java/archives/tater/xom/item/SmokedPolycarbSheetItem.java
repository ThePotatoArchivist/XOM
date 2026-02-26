package archives.tater.xom.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class SmokedPolycarbSheetItem extends Item {

    public static final String TOOLTIP = "item.xom.smoked_polycarb.tooltip";

    public SmokedPolycarbSheetItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(TOOLTIP).formatted(Formatting.GRAY));
    }
}
