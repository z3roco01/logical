package z3roco01.logical.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import z3roco01.logical.util.IdentifierUtil;

import java.util.function.Function;

public class LogicalBlocks {
    public static final LogicGateBlock NAND_GATE = createLogicGate("nand_gate", LogicGateBlock.Operation.NAND);

    /**
     * Dummy register method to trigger static variable creation
     */
    public static void register() {

    }

    private static LogicGateBlock createLogicGate(String id, LogicGateBlock.Operation operation) {
        return registerWithItem(id, p -> new LogicGateBlock(operation, p), BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER));
    }

    private static <T extends Block> T register(String id, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, IdentifierUtil.mkId(id));

        return Registry.register(BuiltInRegistries.BLOCK, key, blockFactory.apply(properties.setId(key)));
    }

    private static <T extends Block> T registerWithItem(String id, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties properties) {
        T block = register(id, blockFactory, properties);

        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, IdentifierUtil.mkId(id));

        BlockItem item = new BlockItem(block, new Item.Properties().setId(key).useBlockDescriptionPrefix());
        Registry.register(BuiltInRegistries.ITEM, key, item);

        return block;
    }
}
