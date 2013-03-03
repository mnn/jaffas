package monnef.core.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCloth;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class DyeHelper {
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

    public static int getDyeNum(DyeColor color) {
        Integer index = dyeNames.get(color.getColor());
        if (index == null) {
            throw new RuntimeException("unknown color name: " + color.getColor());
        }
        return index;
    }

    public static int getWoolNum(DyeColor color) {
        Integer index = woolNames.get(color.getColor());
        if (index == null) {
            throw new RuntimeException("unknown color name: " + color.getColor());
        }
        return index;
    }

    public static ItemStack getDye(DyeColor color) {
        return new ItemStack(Item.dyePowder, 1, getDyeNum(color));
    }

    public static ItemStack getWool(DyeColor color) {
        return new ItemStack(Block.cloth, 1, getWoolNum(color));
    }
}
