package archives.tater.xom.mixin;

import archives.tater.xom.ConeEntity;
import archives.tater.xom.Xom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.Block;
import net.minecraft.entity.FallingBlockEntity;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {
    @ModifyExpressionValue(
            method = "readCustomDataFromNbt",
            at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;SAND:Lnet/minecraft/block/Block;")
    )
    private Block defaultCone(Block original) {
        return ((Object) this instanceof ConeEntity) ? Xom.CONE_BLOCK : original;
    }
}
