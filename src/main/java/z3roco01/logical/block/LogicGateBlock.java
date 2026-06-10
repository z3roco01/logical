package z3roco01.logical.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogicGateBlock extends RedstoneInOutBlock {
    public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);
    private final Operation operation;

    protected LogicGateBlock(Operation operation, Properties properties) {
        super(properties, new ArrayList<>(List.of(RelativeDirection.RIGHT, RelativeDirection.LEFT)));

        this.operation = operation;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(MODE, Mode.DIGITAL));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MODE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(MODE, Mode.DIGITAL);
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
