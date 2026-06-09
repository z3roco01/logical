package z3roco01.logical;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import z3roco01.logical.block.Blocks;

public class Logical implements ModInitializer {
    public static final String MOD_ID = "logical";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Blocks.register();
        LOGGER.info("who up logicing it");
    }
}