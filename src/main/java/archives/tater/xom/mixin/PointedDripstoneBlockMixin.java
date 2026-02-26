package archives.tater.xom.mixin;

import archives.tater.xom.PolycarbFluid;
import archives.tater.xom.Xom;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.block.enums.Thickness;
import net.minecraft.fluid.Fluid;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.AxisDirection;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Mixin(PointedDripstoneBlock.class)
public abstract class PointedDripstoneBlockMixin {
    @Shadow
    private static Optional<BlockPos> searchInDirection(WorldAccess world, BlockPos pos, AxisDirection direction, BiPredicate<BlockPos, BlockState> continuePredicate, Predicate<BlockState> stopPredicate, int range) {
        return Optional.empty();
    }

    @Shadow
    private static boolean canDripThrough(BlockView world, BlockPos pos, BlockState state) {
        return false;
    }

    @Shadow
    @Final
    public static BooleanProperty WATERLOGGED;

    @Definition(id = "fluid", local = @Local(type = Fluid.class))
    @Definition(id = "WATER", field = "Lnet/minecraft/fluid/Fluids;WATER:Lnet/minecraft/fluid/FlowableFluid;")
    @Expression("fluid == WATER")
    @WrapOperation(
            method = "dripTick",
            at = @At("MIXINEXTRAS:EXPRESSION:FIRST")
    )
    private static boolean allowPolycarbFluid(Object left, Object right, Operation<Boolean> original, @Share("polycarb") LocalBooleanRef polycarb) {
        if (original.call(left, right)) return true;
        if (left != Xom.LIQUID_POLYCARB) return false;
        polycarb.set(true);
        return true;
    }

    @Definition(id = "f", local = @Local(type = float.class, ordinal = 1))
    @Expression("f = @(?)")
    @ModifyExpressionValue(
            method = "dripTick",
            at = @At("MIXINEXTRAS:EXPRESSION:FIRST")
    )
    private static float polycarbFluidChance(float original, @Share("polycarb") LocalBooleanRef polycarb) {
        return polycarb.get() ? PolycarbFluid.DRIP_CHANCE : original;
    }

    @WrapOperation(
            method = "dripTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PointedDripstoneBlock;getCauldronPos(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/fluid/Fluid;)Lnet/minecraft/util/math/BlockPos;")
    )
    private static BlockPos makeCone(World world, BlockPos pos, Fluid fluid, Operation<BlockPos> original, @Cancellable CallbackInfo ci) {
        var originalResult = original.call(world, pos, fluid);
        if (!fluid.matchesType(Xom.LIQUID_POLYCARB)) return originalResult;
        var dripstonePos = searchInDirection(world,
                pos,
                Direction.DOWN.getDirection(),
                (posx, statex) -> canDripThrough(world, posx, statex),
                statex -> statex.isOf(Blocks.POINTED_DRIPSTONE)
                        && statex.get(PointedDripstoneBlock.VERTICAL_DIRECTION) == Direction.UP
                        && statex.get(PointedDripstoneBlock.THICKNESS) == Thickness.TIP
                        && !statex.get(WATERLOGGED),
                11
        ).orElse(null);
        if (dripstonePos == null || world.getBlockState(dripstonePos.down()).isOf(Blocks.POINTED_DRIPSTONE)) return originalResult;
        world.syncWorldEvent(WorldEvents.POINTED_DRIPSTONE_DRIPS, pos, 0);
        world.setBlockState(dripstonePos, Xom.CONE_BLOCK.getDefaultState());
        ci.cancel();
        return originalResult;
    }
}
