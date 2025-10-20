package archives.tater.xom;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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

    public static final Block CONE = Registry.register(
			Registries.BLOCK,
			id("cone"),
			new ConeBlock(AbstractBlock.Settings.create()
					.nonOpaque()
					.strength(0.8f, 1.8f)
					.sounds(XomSounds.CONE_SOUNDS)
			)
	);

	public static final Item CONE_ITEM = Registry.register(
            Registries.ITEM,
            id("cone"),
            new ConeItem(CONE, new Item.Settings())
    );

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ConeSummoning.registerCallbacks();

		LOGGER.info("NATE LIVES");
	}
}