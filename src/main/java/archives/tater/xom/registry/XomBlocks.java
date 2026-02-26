package archives.tater.xom.registry;

import archives.tater.xom.Xom;
import archives.tater.xom.block.ConeBlock;
import archives.tater.xom.block.DuctTapeBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class XomBlocks {

    private static Block register(String path, Block block) {
        return Registry.register(Registries.BLOCK, Xom.id(path), block);
    }

    public static final Block CONE = register("cone", new ConeBlock(AbstractBlock.Settings.create()
            .nonOpaque()
            .strength(0.8f, 1.8f)
            .sounds(XomSounds.CONE_SOUNDS)
    ));

    public static final Block DUCT_TAPE = register("duct_tape", new DuctTapeBlock(AbstractBlock.Settings.create()
            .noCollision()
            .strength(0.5f, 0f)
            .sounds(BlockSoundGroup.WOOL)
    ));

    public static final Block LIQUID_POLYCARB = register("liquid_polycarb", new FluidBlock(
            XomFluids.LIQUID_POLYCARB,
            AbstractBlock.Settings.copy(Blocks.WATER)
                    .luminance(state -> 15)
    ));

    public static void init() {

    }
}
