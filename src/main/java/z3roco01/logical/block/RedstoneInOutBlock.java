package z3roco01.logical.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;
import z3roco01.logical.Logical;

import java.util.ArrayList;

/**
 * A block which will take redstone inputs on some sides, and output on others ( at least one of each )
 */
public abstract class RedstoneInOutBlock extends LogicalHorizDirectionalBlock {
    protected final ArrayList<RelativeDirection> inputs;
    protected final ArrayList<RelativeDirection> outputs;

    protected RedstoneInOutBlock(Properties properties) {
        this(properties, new ArrayList<>(), new ArrayList<>());
    }

    protected RedstoneInOutBlock(Properties properties, ArrayList<RelativeDirection> inputs) {
        this(properties, inputs, new ArrayList<>());
    }

    protected RedstoneInOutBlock(Properties properties, ArrayList<RelativeDirection> inputs, ArrayList<RelativeDirection> outputs) {
        super(properties);
        this.inputs = inputs;
        this.outputs = outputs;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        getInputsUnordered(level, state, pos);
    }

    protected int[] getInputsUnordered(Level level, BlockState state, BlockPos pos) {
        int[] values = new int[inputs.size()];

        for(RelativeDirection input : inputs) {
            Direction rotated = translateRelativeDirection(state, input);
            Logical.LOGGER.info(input.toString() + " : " + getInputSignal(level, pos, rotated));
        }

        return values;
    }

    protected int getInputSignal(final Level level, final BlockPos pos, final Direction direction) {
        BlockPos targetPos = pos.relative(direction);
        int input = level.getSignal(targetPos, direction);
        return input;
    }
}
