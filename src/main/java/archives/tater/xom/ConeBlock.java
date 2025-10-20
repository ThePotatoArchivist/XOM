package archives.tater.xom;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static archives.tater.xom.XomUtil.associateWith;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class ConeBlock extends Block {

    public static final IntProperty STACKED = IntProperty.of("stacked", 0, 6);

    private static VoxelShape getShape(int stack, boolean fenceHeight) {
        if (stack < 3)
            return createCuboidShape(4, 0, 4, 12, (fenceHeight ? 12 : 4) + 4 * stack, 12);
        var height = 2 + 4 * (stack - 3);
        return VoxelShapes.union(
                createCuboidShape(0, 0, 0, 16, height, 16),
                createCuboidShape(4, height, 4, 12, (fenceHeight && stack == 3) ? 24 : 16, 12)
        );
    }

    private static final Map<Integer, VoxelShape> OUTLINE_SHAPES = STACKED.getValues().stream().collect(associateWith(stack -> getShape(stack, false)));
    private static final Map<Integer, VoxelShape> COLLISION_SHAPES = STACKED.getValues().stream().collect(associateWith(stack -> getShape(stack, true)));

    public ConeBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(STACKED, 3));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STACKED);
    }

    @Override
    protected boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().isOf(this.asItem())
                && switch (state.get(STACKED)) {
                    case 0, 1, 2 -> true;
                    case 3, 4, 5 -> {
                        var aboveState = context.getWorld().getBlockState(context.getBlockPos().up());
                        yield aboveState.isReplaceable() || aboveState.isOf(this);
                    }
                    default -> false;
                };

    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        var currentState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        return currentState.isOf(this)
                ? currentState.with(STACKED, min(currentState.get(STACKED) + 1, 6))
                : super.getPlacementState(ctx);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        var up = pos.up();
        if (state.get(STACKED) > 3 && !world.getBlockState(up).isOf(this))
            world.setBlockState(up, state.with(STACKED, state.get(STACKED) - 4));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && state.get(STACKED) > 3)
            return neighborState.isOf(this) ? state.with(STACKED, min(neighborState.get(STACKED) + 4, 6)) : state;
        if (direction == Direction.DOWN && state.get(STACKED) < 3)
            return neighborState.isOf(this) && neighborState.get(STACKED) > 3 ? state.with(STACKED, max(neighborState.get(STACKED) - 4, 0)) : Blocks.AIR.getDefaultState();
        return state;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        if (newState.isOf(this)) return;
        var down = world.getBlockState(pos.down());
        if (down.isOf(this) && down.get(STACKED) > 3)
            if (state.get(STACKED) < 3)
                world.breakBlock(pos.down(), true);
            else
                world.setBlockState(pos, state.with(STACKED, max(down.get(STACKED) - 4, 0)));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPES.get(state.get(STACKED));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPES.get(state.get(STACKED));
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
