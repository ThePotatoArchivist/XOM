package archives.tater.xom.datagen;

import archives.tater.xom.block.ConeBlock;
import archives.tater.xom.registry.XomBlocks;
import archives.tater.xom.registry.XomItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider {
    public BlockLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    private LootTable.Builder createConeLootTable(Block block, ItemConvertible item) {
        return LootTable.builder()
                .pool(LootPool.builder()
                        .with(AlternativeEntry.builder(List.of(1, 2, 3, 4), drop -> ItemEntry.builder(item)
                                .conditionally(BlockStatePropertyLootCondition.builder(block)
                                        .properties(StatePredicate.Builder.create().exactMatch(ConeBlock.STACKED, drop + 2))
                                )
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(drop)))
                        ))
                        .conditionally(SurvivesExplosionLootCondition.builder())
                );
    }

    @Override
    public void generate() {
        addDrop(XomBlocks.DUCT_TAPE);

        addDrop(XomBlocks.CONE, createConeLootTable(XomBlocks.CONE, XomItems.CONE));
    }
}
