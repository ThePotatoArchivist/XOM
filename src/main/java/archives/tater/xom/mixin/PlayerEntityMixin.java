package archives.tater.xom.mixin;

import archives.tater.xom.registry.XomItems;
import archives.tater.xom.registry.XomSounds;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(
            method = "getHurtSound",
            at = @At("RETURN")
    )
    private SoundEvent polycarbHurtSound(SoundEvent original) {
        return getEquippedStack(EquipmentSlot.CHEST).isOf(XomItems.POLYCARB_SHEET) ? XomSounds.POLYCARB_HIT : original;
    }
}
