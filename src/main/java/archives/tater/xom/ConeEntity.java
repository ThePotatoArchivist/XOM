package archives.tater.xom;

import archives.tater.xom.mixin.FallingBlockEntityAccessor;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ConeEntity extends FallingBlockEntity {
    public ConeEntity(EntityType<? extends ConeEntity> entityType, World world) {
        super(entityType, world);
        ((FallingBlockEntityAccessor) this).setBlock(Xom.CONE_BLOCK.getDefaultState());
    }

    public ConeEntity(World world, double x, double y, double z, BlockState block) {
        this(Xom.CONE_ENTITY, world);
        ((FallingBlockEntityAccessor) this).setBlock(block);
        intersectionChecked = true;
        setPosition(x, y, z);
        setVelocity(Vec3d.ZERO);
        prevX = x;
        prevY = y;
        prevZ = z;
    }

    private static boolean canEquip(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return false;
        return (livingEntity.getType().isIn(Xom.CAN_WEAR_CONE) || (livingEntity instanceof MobEntity mob && mob.canPickUpLoot())) && livingEntity.getEquippedStack(EquipmentSlot.HEAD).isEmpty();
    }

    @Override
    public void tick() {
        var hit = ProjectileUtil.getCollision(this, ConeEntity::canEquip);
        if (hit instanceof EntityHitResult entityHit) {
            ((LivingEntity) entityHit.getEntity()).equipStack(EquipmentSlot.HEAD, getBlockState().getBlock().asItem().getDefaultStack());
            discard();
            return;
        }
        super.tick();
    }
}
