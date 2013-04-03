package monnef.core.utils;

import com.google.common.collect.Multimap;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.BlockProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import monnef.jaffas.food.block.ItemBlockJaffas;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static monnef.jaffas.food.JaffasFood.Log;

public class RegistryUtils {
    public static void registerBlock(Block block) {
        GameRegistry.registerBlock(block, block.getUnlocalizedName());
    }

    public static void registerBlock(Block block, String title) {
        registerBlock(block);
        LanguageRegistry.addName(block, title);
    }

    public static void registerBlock(Block block, String name, String title) {
        block.setUnlocalizedName(name);
        registerBlock(block, title);
    }

    public static void registerMultiBlock(Block block, Class<?> itemBlock, String[] names) {
        String blockName = block.getUnlocalizedName();
        if (blockName == null) {
            throw new RuntimeException("Block name not set - " + itemBlock.getSimpleName() + ".");
        }

        Class<?> cls = itemBlock;
        if (ItemBlock.class.isAssignableFrom(cls)) {
            GameRegistry.registerBlock(block, (Class<? extends ItemBlock>) itemBlock, blockName);
        } else if (ItemBlockJaffas.class.isAssignableFrom(cls)) {
            registerMyBlock(block, (Class<? extends ItemBlockJaffas>) itemBlock, blockName);
        } else {
            throw new RuntimeException("Unknown class in block registration.");
        }

        registerSubBlocks(block, names);
    }

    private static void registerMyBlock(Block block, Class<? extends ItemBlockJaffas> itemclass, String blockName) {
        // heavily based on GameRegistry.registerBlock of Forge
        if (Loader.instance().isInState(LoaderState.CONSTRUCTING)) {
            Log.printWarning("Registering block in non-constructing state!");
        }
        try {
            assert block != null : "registerBlock: block cannot be null";
            assert itemclass != null : "registerBlock: itemclass cannot be null";
            int blockItemId = block.blockID - 256;
            Constructor<? extends ItemBlockJaffas> itemCtor;
            Item i;
            try {
                itemCtor = itemclass.getConstructor(int.class);
                i = itemCtor.newInstance(blockItemId);
            } catch (NoSuchMethodException e) {
                itemCtor = itemclass.getConstructor(int.class, Block.class);
                i = itemCtor.newInstance(blockItemId, block);
            }
            GameRegistry.registerItem(i, blockName, null);
        } catch (Exception e) {
            Log.printSevere("Problem in registerMyBlock.");
            throw new RuntimeException(e);
        }

        Field br = ReflectionHelper.findField(GameRegistry.class, "blockRegistry");
        Multimap<ModContainer, BlockProxy> blockRegistry;
        try {
            blockRegistry = (Multimap<ModContainer, BlockProxy>) br.get(null);
        } catch (IllegalAccessException e) {
            Log.printSevere("Problem in registerMyBlock.");
            throw new RuntimeException(e);
        }
        blockRegistry.put(Loader.instance().activeModContainer(), (BlockProxy) block);
    }

    private static void registerSubBlocks(Block block, String[] names) {
        for (int ix = 0; ix < names.length; ix++) {
            ItemStack multiBlockStack = new ItemStack(block, 1, ix);
            LanguageRegistry.addName(multiBlockStack, names[multiBlockStack.getItemDamage()]);
        }
    }

    public static <T extends Item> T registerItem(T item, String name, String title) {
        item.setUnlocalizedName(name);
        GameRegistry.registerItem(item, item.getUnlocalizedName());
        LanguageRegistry.addName(item, title);
        return item;
    }
}
