package archives.tater.xom.registry;

import archives.tater.xom.Xom;
import archives.tater.xom.item.ConeItem;
import archives.tater.xom.item.DuctTapeRollItem;
import archives.tater.xom.item.KevinCoreItem;
import archives.tater.xom.item.PolycarbSheetItem;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class XomItems {

    public static Item register(String path, Item item) {
        return Registry.register(Registries.ITEM, Xom.id(path), item);
    }

    public static final Item CONE = register("cone", new ConeItem(XomBlocks.CONE, new Item.Settings()
            .attributeModifiers(AttributeModifiersComponent.builder()
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, 1, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                    .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, -2.4, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                    .add(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(Identifier.ofVanilla("armor." + ArmorItem.Type.HELMET.getName()), 1, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.HEAD)
                    .build())
    ));

    public static final Item DUCT_TAPE = register("duct_tape", new BlockItem(XomBlocks.DUCT_TAPE, new Item.Settings()));

    public static final Item DUCT_TAPE_ROLL = register("duct_tape_roll", new DuctTapeRollItem(new Item.Settings()
            .maxDamage(64)
    ));

    public static final Item POLYCARB_SHEET = register("polycarb_sheet", new PolycarbSheetItem(new Item.Settings()));

    public static final Item SMOKED_POLYCARB = register("smoked_polycarb", new PolycarbSheetItem.Smoked(new Item.Settings()));

    public static final Item POLYCARB_BUCKET = register("liquid_polycarb_bucket", new BucketItem(XomFluids.LIQUID_POLYCARB, new Item.Settings()
            .maxCount(1)
            .recipeRemainder(Items.BUCKET)
    ));

    public static final Item KEVIN_CORE = register("kevin_core", new KevinCoreItem(new Item.Settings()
            .maxCount(1)
    ));

    public static void init() {

    }
}
