package z3roco01.logical.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;
import z3roco01.logical.Logical;

/**
 * A block which will take redstone inputs on some sides, and output on others ( at least one of each )
 */
public abstract class RedstoneInOutBlock extends HorizontalDirectionalBlock {
    protected RedstoneInOutBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        for(Direction dir : Direction.values()) {
            Logical.LOGGER.info(dir.toString() + " : " + getInputSignals(level, pos, state, dir));
        }
    }

    protected int getInputSignals(final Level level, final BlockPos pos, final BlockState state, final Direction direction) {
        BlockPos targetPos = pos.relative(direction);
        int input = level.getSignal(targetPos, direction);
        return input;
    }
}
