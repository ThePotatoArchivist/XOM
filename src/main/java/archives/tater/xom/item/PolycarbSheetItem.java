package archives.tater.xom.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class PolycarbSheetItem extends Item implements Equipment {

    public PolycarbSheetItem(Settings settings) {
        super(settings);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.CHEST;
    }

    public static class Smoked extends PolycarbSheetItem {

        public static final String TOOLTIP = "item.xom.smoked_polycarb.tooltip";

        public Smoked(Settings settings) {
            super(settings);
        }

        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable(TOOLTIP));
        }
    }
}
