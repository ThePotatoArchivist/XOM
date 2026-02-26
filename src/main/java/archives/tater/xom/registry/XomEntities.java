package archives.tater.xom.registry;

import archives.tater.xom.Xom;
import archives.tater.xom.entity.ConeEntity;
import archives.tater.xom.entity.KevinEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Consumer;

public class XomEntities {

    private static <T extends Entity> EntityType<T> register(String path, EntityType<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, Xom.id(path), type);
    }

    private static <T extends Entity> EntityType<T> register(String path, EntityType.EntityFactory<T> factory, Consumer<EntityType.Builder<T>> initType) {
        var builder = EntityType.Builder.create(factory, SpawnGroup.MISC);
        initType.accept(builder);
        return register(path, builder.build());
    }

    public static final EntityType<ConeEntity> CONE = register("cone", ConeEntity::new, builder -> builder
            .dimensions(0.98f, 0.98f)
            .maxTrackingRange(10)
            .trackingTickInterval(20)
    );

    public static final EntityType<KevinEntity> KEVIN = register("kevin", KevinEntity::new, builder -> builder
            .dimensions(KevinEntity.MIN_DIMENSION, KevinEntity.MIN_DIMENSION)
    );

    public static final TagKey<EntityType<?>> CAN_WEAR_CONE = TagKey.of(RegistryKeys.ENTITY_TYPE, Xom.id("can_wear_cone"));

    public static void init() {

    }
}
