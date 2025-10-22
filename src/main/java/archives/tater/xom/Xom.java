package archives.tater.xom;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.ArmorItem.Type;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Xom implements ModInitializer {
	@SuppressWarnings("unused")
    int deed = 0;

	public static final String MOD_ID = "xom";

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Block CONE_BLOCK = Registry.register(
			Registries.BLOCK,
			id("cone"),
			new ConeBlock(AbstractBlock.Settings.create()
					.nonOpaque()
					.strength(0.8f, 1.8f)
					.sounds(XomSounds.CONE_SOUNDS)
			)
	);

    public static final FlowableFluid LIQUID_POLYCARB = Registry.register(Registries.FLUID, id("liquid_polycarb"), new PolycarbFluid.Still());
    public static final FlowableFluid FLOWING_LIQUID_POLYCARB = Registry.register(Registries.FLUID, id("flowing_liquid_polycarb"), new PolycarbFluid.Flowing());

    public static final Block LIQUID_POLYCARB_BLOCK = Registry.register(
            Registries.BLOCK,
            id("liquid_polycarb"),
            new FluidBlock(LIQUID_POLYCARB, AbstractBlock.Settings.copy(Blocks.WATER))
    );

	public static final Item CONE_ITEM = Registry.register(
            Registries.ITEM,
            id("cone"),
            new ConeItem(CONE_BLOCK, new Item.Settings()
                    .attributeModifiers(AttributeModifiersComponent.builder()
                            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, 1, Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                            .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, -2.4, Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                            .add(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(Identifier.ofVanilla("armor." + Type.HELMET.getName()), 1, Operation.ADD_VALUE), AttributeModifierSlot.HEAD)
                            .build())
            )
    );

    public static final Item POLYCARB_SHEET = Registry.register(
            Registries.ITEM,
            id("polycarb_sheet"),
            new Item(new Item.Settings())
    );

    public static final Item POLYCARB_BUCKET = Registry.register(
            Registries.ITEM,
            id("liquid_polycarb_bucket"),
            new BucketItem(LIQUID_POLYCARB, new Item.Settings().maxCount(1).recipeRemainder(Items.BUCKET))
    );

    public static final EntityType<ConeEntity> CONE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            id("cone"),
            EntityType.Builder.<ConeEntity>create(ConeEntity::new, SpawnGroup.MISC)
                    .dimensions(0.98f, 0.98f)
                    .maxTrackingRange(10)
                    .trackingTickInterval(20)
                    .build()
    );

    public static final TagKey<EntityType<?>> CAN_WEAR_CONE = TagKey.of(RegistryKeys.ENTITY_TYPE, id("can_wear_cone"));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ConeSummoning.registerCallbacks();

		LOGGER.info("NATE LIVES");
	}
}