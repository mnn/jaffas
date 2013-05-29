package monnef.jaffas.technic.common;

import com.google.common.collect.HashMultimap;
import monnef.core.utils.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Set;

import static monnef.jaffas.food.JaffasFood.blockSwitchgrass;
import static monnef.jaffas.food.JaffasFood.blockSwitchgrassSolid;
import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

public class CompostRegister {
    private static HashMultimap<Integer, CompostItem> compostItems;

    public static final int DEFAULT_FRUIT_COMPOSTING_VALUE = 50;
    public static final int DEFAULT_BLOCK_COMPOSTING_VALUE = 100;

    static {
        compostItems = HashMultimap.create();

        // vanilla
        addStack(Item.appleRed, DEFAULT_FRUIT_COMPOSTING_VALUE);
        addStack(Item.carrot, DEFAULT_FRUIT_COMPOSTING_VALUE);
        addStack(Item.potato, DEFAULT_FRUIT_COMPOSTING_VALUE);
        addStack(Item.melon, DEFAULT_FRUIT_COMPOSTING_VALUE);
        addStack(Block.reed, DEFAULT_FRUIT_COMPOSTING_VALUE);
        addStack(Block.pumpkin, DEFAULT_BLOCK_COMPOSTING_VALUE);
        addStack(Block.cactus, DEFAULT_BLOCK_COMPOSTING_VALUE);
        addStackAnyDamage(Block.leaves, DEFAULT_BLOCK_COMPOSTING_VALUE);

        // food module
        addStackAnyDamage(blockSwitchgrass, DEFAULT_FRUIT_COMPOSTING_VALUE + DEFAULT_FRUIT_COMPOSTING_VALUE / 10);
        addStackAnyDamage(blockSwitchgrassSolid, getCompostValue(new ItemStack(blockSwitchgrass)) * 10);
    }

    public static void add(ItemStack stack, int compostingValue) {
        if (stack == null) {
            throw new RuntimeException("invalid stack in item adding to compost register");
        }
        if (compostingValue <= 0) {
            throw new RuntimeException("invalid composting value");
        }

        compostItems.put(stack.itemID, new CompostItem(stack.copy(), compostingValue));
    }

    public static int getCompostValue(ItemStack input) {
        if (input == null) {
            return 0;
        }

        Set<CompostItem> found = compostItems.get(input.itemID);
        int best = 0;
        for (CompostItem item : found) {
            if (ItemHelper.haveStacksSameIdAndDamage(item.item, input)) {
                if (best != 0) {
                    throw new RuntimeException("collisions in compost register");
                }

                best = item.value;
            }
        }

        return best;
    }

    private static class CompostItem {
        public ItemStack item;
        public int value;

        private CompostItem(ItemStack item, int value) {
            this.item = item;
            this.value = value;
        }
    }

    // helper adding methods
    public static void addStack(Block block, int compostingValue) {
        addStack(block, 0, compostingValue);
    }

    public static void addStackAnyDamage(Block block, int compostingValue) {
        addStack(block, WILDCARD_VALUE, compostingValue);
    }

    public static void addStack(Block block, int meta, int compostingValue) {
        add(new ItemStack(block, 1, meta), compostingValue);
    }

    public static void addStack(Item item, int compostingValue) {
        addStack(item, 0, compostingValue);
    }

    public static void addStackAnyDamage(Item item, int compostingValue) {
        addStack(item, WILDCARD_VALUE, compostingValue);
    }

    public static void addStack(Item item, int meta, int compostingValue) {
        add(new ItemStack(item, 1, meta), compostingValue);
    }

}
