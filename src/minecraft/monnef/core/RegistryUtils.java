package monnef.core;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class RegistryUtils {
    public static void registerBlock(Block block) {
        GameRegistry.registerBlock(block, block.getBlockName());
    }

    public static void registerBlock(Block block, String title) {
        registerBlock(block);
        LanguageRegistry.addName(block, title);
    }

    public static void registerBlock(Block block, String name, String title) {
        block.setBlockName(name);
        registerBlock(block, title);
    }

    public static void registerMultiBlock(Block block, Class<? extends ItemBlock> itemBlock, String[] names) {
        GameRegistry.registerBlock(block, itemBlock, block.getBlockName());
        for (int ix = 0; ix < names.length; ix++) {
            ItemStack multiBlockStack = new ItemStack(block, 1, ix);
            LanguageRegistry.addName(multiBlockStack, names[multiBlockStack.getItemDamage()]);
        }
    }
}
