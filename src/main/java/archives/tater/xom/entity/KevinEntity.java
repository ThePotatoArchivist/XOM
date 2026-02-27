package archives.tater.xom.entity;

import archives.tater.xom.registry.XomItems;

import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.util.Objects.requireNonNullElse;

public class KevinEntity extends Entity {

    private static final TrackedData<Integer> SIZE = DataTracker.registerData(KevinEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IMPLODING = DataTracker.registerData(KevinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public static final int IMPLODE_SIZE = 64 - 9;
    public static final float MIN_DIMENSION = 0.5f;
    public static final float MAX_DIMENSION = 2.0f;
    public static final float SCALE_PER_SIZE = (MAX_DIMENSION / MIN_DIMENSION - 1) / IMPLODE_SIZE;
    public static final double MIN_VELOCITY = 0.01f;
    public static final double ASCEND_SPEED = 0.1;
    public static final int IMPLODE_TICKS = 10 * 20;
    private static final List<FireworkExplosionComponent> FIREWORK = List.of(new FireworkExplosionComponent(
            FireworkExplosionComponent.Type.SMALL_BALL,
            IntList.of(0xffffff),
            IntList.of(),
            false,
            false
    ));

    private int implodeTicks = 0;
    public final Vector3f rotationAxis = Vec3d.fromPolar((random.nextFloat() * 180) - 90, random.nextFloat() * 360).toVector3f();

    public KevinEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public int getSize() {
        return dataTracker.get(SIZE);
    }

    public void setSize(int size) {
        dataTracker.set(SIZE, size);
        refreshPosition();
        calculateDimensions();

        if (!getWorld().isClient() && size >= IMPLODE_SIZE)
            startImploding();
    }

    public void addSize(int add) {
        setSize(getSize() + add);
    }

    public boolean isImploding() {
        return dataTracker.get(IMPLODING);
    }

    private void startImploding() {
        dataTracker.set(IMPLODING, true);
        setInvulnerable(true);
        setVelocity(new Vec3d(0, ASCEND_SPEED, 0));
    }

    public float getScale() {
        return 1 + SCALE_PER_SIZE * getSize();
    }

    public float getImplodeTicks(float tickDelta) {
        return implodeTicks + tickDelta;
    }

    public float getImplodeScale(float tickDelta) {
        return isImploding() ? 1 - (getImplodeTicks(tickDelta) / IMPLODE_TICKS) : 1;
    }

    @Override
    public void tick() {
        super.tick();

        if (isImploding()) {
            implodeTicks++;

            if (!getWorld().isClient() && implodeTicks > IMPLODE_TICKS) {
                getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
                var itemEntity = new ItemEntity(getWorld(), getX(), getBodyY(0.5) - EntityType.ITEM.getHeight(), getZ(), XomItems.POLYCARB_SHEET.getDefaultStack(), 0, 0, 0);
                itemEntity.setToDefaultPickupDelay();
                itemEntity.setNoGravity(true);
                getWorld().spawnEntity(itemEntity);
                discard();
                return;
            }
        } else
            applyGravity();

        if (isOnGround() || isImploding())
            setVelocity(getVelocity().multiply(0.75, 0.97, 0.75));

        move(MovementType.SELF, getVelocity());
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            getWorld().addFireworkParticle(getX(), getBodyY(0.5), getZ(), 0, 0, 0, FIREWORK);
            return;
        }
        super.handleStatus(status);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public float getStepHeight() {
        return 0.5f;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public @Nullable ItemStack getPickBlockStack() {
        return XomItems.KEVIN_CORE.getDefaultStack();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) return false;
        if (this.getWorld().isClient) return true;
        var amountRounded = round(amount);
        if (amountRounded <= 0) return false;
        var removed = min(amountRounded, getSize());
        scheduleVelocityUpdate();
        emitGameEvent(GameEvent.ENTITY_DAMAGE);
        addSize(-amountRounded);
        if (!source.isIn(DamageTypeTags.BURNS_ARMOR_STANDS))
            dropStack(XomItems.DUCT_TAPE.getDefaultStack().copyWithCount(removed));
        if (getSize() < 0) {
            dropItem(XomItems.KEVIN_CORE);
            this.discard();
        } else if (!source.isIn(DamageTypeTags.NO_KNOCKBACK))
            addVelocity(getPos().subtract(requireNonNullElse(source.getPosition(), getPos()))
                    .multiply(1, 0, 1)
                    .normalize()
                    .multiply(0.1)
                    .add(0, 0.1, 0)
            );

        return true;
    }

    @Override
    protected double getGravity() {
        return 0.04;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return super.getDimensions(pose).scaled(getScale());
    }

    @Override
    public void calculateDimensions() {
        var x = getX();
        var y = getY();
        var z = getZ();
        super.calculateDimensions();
        setPosition(x, y, z);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(SIZE, 0);
        builder.add(IMPLODING, false);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (data == SIZE)
            calculateDimensions();
        super.onTrackedDataSet(data);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        setSize(nbt.getInt("size"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("size", getSize());
    }
}
