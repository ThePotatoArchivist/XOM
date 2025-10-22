package archives.tater.xom;

import archives.tater.xom.mixin.FallingBlockEntityAccessor;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
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

    private boolean onHit(HitResult hit) {
        if (hit.getType() == Type.MISS) return false;

        if (hit instanceof EntityHitResult entityHit) {
            ((LivingEntity) entityHit.getEntity()).equipStack(EquipmentSlot.HEAD, getBlockState().getBlock().asItem().getDefaultStack());
            if (entityHit.getEntity() instanceof MobEntity mobEntity)
                mobEntity.setEquipmentDropChance(EquipmentSlot.HEAD, 1);
            return true;
        }

        if (!(hit instanceof BlockHitResult blockHit)) return false;
        var pos = blockHit.getBlockPos();

        if (getY() <= pos.getY()) return false;

        var hitState = getWorld().getBlockState(pos);
        if (!hitState.isOf(Xom.CONE_BLOCK)) return false;

        var stacked = hitState.get(ConeBlock.STACKED);
        if (stacked >= 6) return false;

        var offsetState = getWorld().getBlockState(pos.up());
        if (!offsetState.isOf(Xom.CONE_BLOCK) && !offsetState.isReplaceable()) return false;

        getWorld().setBlockState(pos, hitState.with(ConeBlock.STACKED, stacked + 1));
        if (stacked >= 3 && !offsetState.isOf(Xom.CONE_BLOCK))
            getWorld().setBlockState(pos.up(), Xom.CONE_BLOCK.getDefaultState().with(ConeBlock.STACKED, 0));

        playSound(XomSounds.CONE_LAND, 1f, 1f);

        return true;
    }

    @Override
    public void tick() {
        if (onHit(ProjectileUtil.getCollision(this, ConeEntity::canEquip))) {
            discard();
            return;
        }
        super.tick();
    }

    @Override
    public void onLanding() {
        super.onLanding();
        playSound(XomSounds.CONE_FALLS, 1f, 1f);
    }
}
