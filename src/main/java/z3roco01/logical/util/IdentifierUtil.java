package z3roco01.logical.util;

import net.minecraft.resources.Identifier;
import z3roco01.logical.Logical;

public class IdentifierUtil {
    public static Identifier mkId(String path) {
        //? if <1.21 {
        /*return new Identifier(namespace, path);
         *///?} else
        return Identifier.fromNamespaceAndPath(Logical.MOD_ID, path);
    }
}
