package z3roco01.logical.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public abstract class LogicalHorizDirectionalBlock extends HorizontalDirectionalBlock {
    protected LogicalHorizDirectionalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }

    /**
     * Converts a relative direction into a absolute direction
     * @param state the BlockState that has the current FACING diretion
     * @param relativeDir the RelativeDirection to translate
     * @return the translated Direction
     */
    protected Direction translateRelativeDirection(BlockState state, RelativeDirection relativeDir) {
        Direction facing = state.getValue(FACING);

        return switch(relativeDir) {
            case FORWARD -> facing;
            case LEFT -> facing.getCounterClockWise();
            case RIGHT -> facing.getClockWise();
            case BACKWARD -> facing.getOpposite();
        };
    }

    /**
     * An enum to make handling relative directions a little easier
     */
    public enum RelativeDirection {
        FORWARD(),
        LEFT(),
        RIGHT(),
        BACKWARD(),
    }
}
