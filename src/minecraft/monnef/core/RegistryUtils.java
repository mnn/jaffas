package monnef.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class RegistryUtils {
    public static void RegisterBlock(Block block) {
        GameRegistry.registerBlock(block, block.getBlockName());
    }
}
