package z3roco01.logical.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import z3roco01.logical.Logical;

import java.util.ArrayList;
import java.util.List;

/**
 * A block which performs a logical operation on two redstone inputs from the left and right, outputting the result forward
 */
public class LogicGateBlock extends RedstoneInOutBlock {
    public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);
    public static final IntegerProperty OUTPUT = IntegerProperty.create("output", 0, 15);
    private final Operation operation;

    protected LogicGateBlock(Operation operation, Properties properties) {
        super(properties, new ArrayList<>(List.of(RelativeDirection.RIGHT, RelativeDirection.LEFT)), new ArrayList<>(List.of(RelativeDirection.FORWARD)));

        this.operation = operation;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(MODE, Mode.ANALOG).setValue(OUTPUT, 0));
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getSignal(level, pos, direction);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if(direction == state.getValue(FACING).getOpposite()) {
            return state.getValue(OUTPUT);
        }else {
            return 0;
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return super.getAnalogOutputSignal(state, level, pos, direction);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        updateOutput(state, level, pos);
    }

    @Override
    protected void neighborChanged(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        updateOutput(state, level, pos);
    }

    protected void updateOutput(BlockState state, Level level, BlockPos pos) {
        int[] inputs = getModeInputs(level, state, pos);

        if(inputs.length != 2)
            throw new RuntimeException("Logic gate @ " + pos + " has invalid input count: " + inputs.length);

        int a = inputs[0];
        int b = inputs[1];
        Logical.LOGGER.info(a + " " + b);

        int output = switch(operation) {
            case AND -> a & b;
            case OR -> a | b;
            case XOR -> a ^ b;
            case NAND -> ~(a & b);
            case NOR -> ~(a | b);
            case XNOR -> ~(a ^ b);
        };

        storeOutput(state, level, pos, output);

    }

    protected void storeOutput(BlockState state, Level level, BlockPos pos, int value) {
        Mode mode = state.getValue(MODE);
        // value to be outputted forward
        int result = 0;

        if(mode == Mode.ANALOG) {
            // get first
            result = value & 0b1111;
        }else if(mode == Mode.DIGITAL) {
            if(value == 1)
                result = 15;
        }

        level.setBlockAndUpdate(pos, state.setValue(OUTPUT, result));
    }

    protected int[] getModeInputs(Level level, BlockState state, BlockPos pos) {
        int[] rawInputs = getInputsUnordered(level, state, pos);
        int[] inputs = new int[rawInputs.length];
        Mode mode = state.getValue(MODE);

        for(int i = 0; i < rawInputs.length; ++i) {
            if(mode == Mode.DIGITAL) {
                if(rawInputs[i] == 15)
                    inputs[i] = 1;
                else
                    inputs[i] = 0;
            }else if(mode == Mode.ANALOG) {
                inputs[i] = rawInputs[i];
            }
        }

        return inputs;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MODE).add(OUTPUT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(MODE, Mode.ANALOG).setValue(OUTPUT, 0);
    }

    /**
     * The operation that this gate will perform, used in the constructor
     */
    public enum Operation {
        AND,
        OR,
        XOR,
        NAND,
        NOR,
        XNOR;
    }

    public enum Mode implements StringRepresentable {
        /**
         * Treats the redstone input as a 3 bit number, 0 being 000, 15 beiung 111
         */
        ANALOG("analog"),
        /**
         * Treats the redstone input as an on/off signal, 15 being on, anything else being off
         */
        DIGITAL("digital");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }
    }
}
