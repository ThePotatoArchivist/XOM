package archives.tater.xom.mixin;

import archives.tater.xom.Xom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;

import org.jetbrains.annotations.Nullable;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin {

    @Shadow
    @Final
    private @Nullable Entity attacker;

    @ModifyExpressionValue(
            method = "getDeathMessage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageType;msgId()Ljava/lang/String;")
    )
    private String coneDeathMessage(String original) {
        if (attacker == null) return original;
        var weapon = attacker.getWeaponStack();
        if (weapon == null) return original;
        return (weapon.isOf(Xom.CONE_ITEM)) ? "xom.cone" : original;
    }
}
