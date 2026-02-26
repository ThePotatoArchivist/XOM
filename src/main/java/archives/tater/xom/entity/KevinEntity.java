package archives.tater.xom.entity;

import archives.tater.xom.registry.XomItems;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNullElse;

public class KevinEntity extends Entity {

    public static final TrackedData<Integer> SIZE = DataTracker.registerData(KevinEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public static final int IMPLODE_SIZE = 64 - 9;
    public static final float MIN_DIMENSION = 0.5f;
    public static final float MAX_DIMENSION = 2.0f;
    public static final float SCALE_PER_SIZE = (MAX_DIMENSION / MIN_DIMENSION - 1) / IMPLODE_SIZE;
    public static final double MIN_VELOCITY = 0.01f;

    private float health = 10;

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
    }

    public void addSize(int add) {
        setSize(getSize() + add);
    }

    public float getScale() {
        return 1 + SCALE_PER_SIZE * getSize();
    }

    @Override
    public void tick() {
        super.tick();
        applyGravity();
        if (isOnGround())
            setVelocity(getVelocity().multiply(0.75));
        move(MovementType.SELF, getVelocity());

        if (age % 100 == 0)
            health += 1;
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
        scheduleVelocityUpdate();
        emitGameEvent(GameEvent.ENTITY_DAMAGE);
        health -= amount;
        if (health <= 0) {
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
        if (nbt.contains("health"))
            health = nbt.getFloat("health");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("size", getSize());
        nbt.putFloat("health", health);
    }
}
