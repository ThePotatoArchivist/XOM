package archives.tater.xom.mixin;

import archives.tater.xom.Xom;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
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
import net.minecraft.fluid.Fluids;
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

    @WrapOperation(
            method = "dripTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PointedDripstoneBlock;getCauldronPos(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/fluid/Fluid;)Lnet/minecraft/util/math/BlockPos;")
    )
    private static BlockPos makeCone(World world, BlockPos pos, Fluid fluid, Operation<BlockPos> original, @Cancellable CallbackInfo ci) {
        var originalResult = original.call(world, pos, fluid);
        if (!fluid.matchesType(Fluids.LAVA)) return originalResult;
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
