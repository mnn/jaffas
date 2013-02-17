package monnef.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCloth;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class DyeHelper {
    // "black", "red", "green", "brown",
    // "blue", "purple", "cyan", "silver",
    // "gray", "pink", "lime", "yellow",
    // "lightBlue", "magenta", "orange", "white"
    private static final HashMap<String, Integer> dyeNames;
    private static final HashMap<String, Integer> woolNames;

    static {
        dyeNames = new HashMap<String, Integer>();
        woolNames = new HashMap<String, Integer>();
        for (int i = 0; i < ItemDye.dyeColorNames.length; i++) {
            String colorName = ItemDye.dyeColorNames[i];
            dyeNames.put(colorName, i);
            woolNames.put(colorName, BlockCloth.getBlockFromDye(i));
        }
    }

    public static int getDyeNum(String colorName) {
        Integer index = dyeNames.get(colorName);
        if (index == null) {
            throw new RuntimeException("unknown color name: " + colorName);
        }
        return index;
    }

    public static int getWoolNum(String colorName) {
        Integer index = woolNames.get(colorName);
        if (index == null) {
            throw new RuntimeException("unknown color name: " + colorName);
        }
        return index;
    }

    public static ItemStack getDye(String colorName) {
        return new ItemStack(Item.dyePowder, 1, getDyeNum(colorName));
    }

    public static ItemStack getWool(String colorName) {
        return new ItemStack(Block.cloth, 1, getWoolNum(colorName));
    }
}
