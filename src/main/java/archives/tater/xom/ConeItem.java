package archives.tater.xom;

import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ConeItem extends BlockItem implements Equipment {
    public ConeItem(Block block, Settings settings) {
        super(block, settings);
    }

    public static final int MIN_USE_TIME = 10;

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public RegistryEntry<SoundEvent> getEquipSound() {
        return XomSounds.CONE_EQUIP;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 60 * 60 * 20; // 1 hour, standard
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user.getItemUseTime() < MIN_USE_TIME) return;

        user.swingHand(user.getActiveHand());

        if (world.isClient) return;

        var fallingBlock = new ConeEntity(world, user.getX(), user.getEyeY() - 0.1, user.getZ(), getBlock().getDefaultState());
        fallingBlock.setVelocity(user.getRotationVector());
        world.spawnEntity(fallingBlock);
        stack.decrementUnlessCreative(1, user);
    }
}
