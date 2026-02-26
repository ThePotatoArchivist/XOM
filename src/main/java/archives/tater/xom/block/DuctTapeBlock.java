package archives.tater.xom.block;

import archives.tater.xom.entity.KevinEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import org.jetbrains.annotations.Nullable;

public class DuctTapeBlock extends Block {
    public static final Property<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

    public static final VoxelShape SHAPE_X = createCuboidShape(0, 0, 6, 16, 1, 10);
    public static final VoxelShape SHAPE_Z = createCuboidShape(6, 0, 0, 10, 1, 16);

    public DuctTapeBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient()
                || !entity.getBoundingBox().intersects(state.getOutlineShape(world, pos).getBoundingBox().offset(pos))
                || !(entity instanceof KevinEntity kevin)
                || entity.getMovement().horizontalLengthSquared() < KevinEntity.MIN_VELOCITY * KevinEntity.MIN_VELOCITY) return;

        kevin.addSize(1);
        world.breakBlock(pos, false, kevin);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AXIS);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var down = world.getBlockState(pos.down());
        return Block.isFaceFullSquare(down.getCollisionShape(world, pos.down()), Direction.UP);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : state;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(AXIS, ctx.getHorizontalPlayerFacing().getAxis());
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(AXIS) == Direction.Axis.Z ? SHAPE_Z : SHAPE_X;
    }
}
