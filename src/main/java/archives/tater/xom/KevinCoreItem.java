package archives.tater.xom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

import java.util.function.Consumer;

public class KevinCoreItem extends Item {
    public KevinCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var itemPlacementContext = new ItemPlacementContext(context);
        var blockPos = itemPlacementContext.getBlockPos();
        var itemStack = context.getStack();
        var vec3d = Vec3d.ofBottomCenter(blockPos);
        var box = Xom.KEVIN_ENTITY.getDimensions().getBoxAt(vec3d.getX(), vec3d.getY(), vec3d.getZ());

        if (!world.isSpaceEmpty(null, box) || !world.getOtherEntities(null, box).isEmpty())
            return ActionResult.FAIL;

        if (!(world instanceof ServerWorld serverWorld)) return ActionResult.SUCCESS;

        Consumer<KevinEntity> consumer = EntityType.copier(serverWorld, itemStack, context.getPlayer());
        var kevinEntity = Xom.KEVIN_ENTITY.create(serverWorld, consumer, blockPos, SpawnReason.SPAWN_EGG, true, true);
        if (kevinEntity == null) {
            return ActionResult.FAIL;
        }

        serverWorld.spawnEntityAndPassengers(kevinEntity);
        world.playSound(
                null, kevinEntity.getX(), kevinEntity.getY(), kevinEntity.getZ(), SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F
        );
        kevinEntity.emitGameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());

        itemStack.decrement(1);
        return ActionResult.CONSUME;
    }
}
