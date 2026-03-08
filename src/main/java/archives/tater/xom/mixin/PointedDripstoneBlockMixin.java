package archives.tater.xom.mixin;

import archives.tater.xom.fluid.PolycarbFluid;
import archives.tater.xom.registry.XomBlocks;
import archives.tater.xom.registry.XomFluids;
import archives.tater.xom.registry.XomParticles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.objectweb.asm.Opcodes;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.block.enums.Thickness;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.ParticleEffect;
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

    @ModifyVariable(
            method = "dripTick",
            at = @At(value = "FIELD:FIRST", target = "Lnet/minecraft/fluid/Fluids;WATER:Lnet/minecraft/fluid/FlowableFluid;", opcode = Opcodes.GETSTATIC)
    )
    private static Fluid allowPolycarbFluid(Fluid fluid, @Share("polycarb") LocalBooleanRef polycarb) {
        if (fluid != XomFluids.LIQUID_POLYCARB) return fluid;
        polycarb.set(true);
        return Fluids.WATER;
    }

    @ModifyVariable(
            method = "dripTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PointedDripstoneBlock;getTipPos(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;IZ)Lnet/minecraft/util/math/BlockPos;")
    )
    private static Fluid fixFluid(Fluid fluid, @Share("polycarb") LocalBooleanRef polycarb) {
        return polycarb.get() ? XomFluids.LIQUID_POLYCARB : fluid;
    }

    @ModifyVariable(
            method = "dripTick",
            ordinal = 1,
            at = @At("STORE")
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
        if (!fluid.matchesType(XomFluids.LIQUID_POLYCARB)) return originalResult;
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
        world.setBlockState(dripstonePos, XomBlocks.CONE.getDefaultState());
        ci.cancel();
        return originalResult;
    }

    @Dynamic
    @ModifyVariable(
            method = "Lnet/minecraft/world/level/block/PointedDripstoneBlock;spawnDripParticle(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/Fluid;)V",
            at = @At("STORE"),
            remap = false
    )
    private static ParticleEffect polycarbParticle(ParticleEffect value, @Local(argsOnly = true) Fluid fluid) {
        return fluid.matchesType(XomFluids.LIQUID_POLYCARB) ? XomParticles.DRIPPING_POLYCARB : value;
    }
}
