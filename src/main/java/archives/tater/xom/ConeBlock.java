package archives.tater.xom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ConeBlock extends Block {

    public static final IntProperty STACKED = IntProperty.of("stacked", 0, 6);

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
            return neighborState.isOf(this) ? state.with(STACKED, min(neighborState.get(STACKED) + 4, 6)) : state.with(STACKED, 3);
        if (direction == Direction.DOWN && state.get(STACKED) < 3)
            return neighborState.isOf(this) ? state.with(STACKED, max(neighborState.get(STACKED) - 4, 0)) : Blocks.AIR.getDefaultState();
        return state;
    }
}
