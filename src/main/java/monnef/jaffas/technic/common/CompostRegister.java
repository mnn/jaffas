package monnef.jaffas.technic.common;

import com.google.common.collect.HashMultimap;
import monnef.core.utils.ItemHelper;
import monnef.core.utils.RegistryUtils;
import monnef.jaffas.food.block.BlockSwitchgrass;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static monnef.jaffas.food.common.ContentHolder.blockSwitchgrass;
import static monnef.jaffas.food.common.ContentHolder.blockSwitchgrassSolid;
import static monnef.jaffas.food.crafting.Recipes.ANY_DMG;

public class CompostRegister {
    private static HashMultimap<Item, CompostItem> compostItems;

    public static final int DEFAULT_FRUIT_COMPOSTING_VALUE = 50;
    public static final int DEFAULT_BLOCK_COMPOSTING_VALUE = 100;

    private static boolean initialized = false;

    public static void fillWithVanillaItems() {
        if (!initialized) {
            throw new RuntimeException("not initialized");
        }

        addStack(Items.apple, DEFAULT_FRUIT_COMPOSTING_VALUE);
        addStack(Items.carrot, DEFAULT_FRUIT_COMPOSTING_VALUE);
        addStack(Items.potato, DEFAULT_FRUIT_COMPOSTING_VALUE);
        addStack(Items.melon, DEFAULT_FRUIT_COMPOSTING_VALUE);

        // TODO: finish porting of itemstacks with blocks
        /*
        addStack(Blocks.reeds, DEFAULT_FRUIT_COMPOSTING_VALUE);
        addStack(Blocks.pumpkin, DEFAULT_BLOCK_COMPOSTING_VALUE);
        addStack(Blocks.cactus, DEFAULT_BLOCK_COMPOSTING_VALUE);
        addStackAnyDamage(Blocks.leaves, DEFAULT_BLOCK_COMPOSTING_VALUE).overrideTitle("Leaves");
        addStackAnyDamage(Blocks.leaves2, DEFAULT_BLOCK_COMPOSTING_VALUE).overrideTitle("Leaves");
        */
    }

    public static void fillWithFoodModuleItems() {
        if (!initialized) {
            throw new RuntimeException("not initialized");
        }

        int switchgrassCompostValue = DEFAULT_FRUIT_COMPOSTING_VALUE + DEFAULT_FRUIT_COMPOSTING_VALUE / 10;
        add(new ItemStack(blockSwitchgrass, 1, BlockSwitchgrass.VALUE_TOP), switchgrassCompostValue).overrideTitle("Switchgrass");
        addStackAnyDamage(blockSwitchgrassSolid, switchgrassCompostValue * 10);
    }

    public static void init() {
        if (initialized) {
            throw new RuntimeException("re-initilization");
        }
        initialized = true;
        compostItems = HashMultimap.create();
    }

    public static CompostItem add(ItemStack stack, int compostingValue) {
        if (stack == null) {
            throw new RuntimeException("invalid stack in item adding to compost register");
        }
        if (stack.getItem() == null) {
            throw new RuntimeException("invalid null item in stack in item adding to compost register");
        }
        if (compostingValue <= 0) {
            throw new RuntimeException("invalid composting value");
        }

        CompostItem item = new CompostItem(stack.copy(), compostingValue);
        compostItems.put(stack.getItem(), item);
        return item;
    }

    public static int getCompostValue(ItemStack input) {
        if (input == null) {
            return 0;
        }

        Set<CompostItem> found = compostItems.get(input.getItem());
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
        public String title = null;

        private CompostItem(ItemStack item, int value) {
            this.item = item;
            this.value = value;
        }

        private CompostItem overrideTitle(String newTitle) {
            title = newTitle;
            return this;
        }
    }

    public static List<String> generateTextForGuide() {
        ArrayList<String> res = new ArrayList<String>();
        for (CompostItem item : compostItems.values()) {
            String title = item.title == null ? RegistryUtils.getTitle(item.item) : item.title;
            String line = String.format("%d - %s", item.value, title);
            res.add(line);
        }
        return res;
    }

    // helper adding methods
    public static CompostItem addStack(Block block, int compostingValue) {
        return addStack(block, 0, compostingValue);
    }

    public static CompostItem addStackAnyDamage(Block block, int compostingValue) {
        return addStack(block, ANY_DMG, compostingValue);
    }

    public static CompostItem addStack(Block block, int meta, int compostingValue) {
        return add(ItemHelper.getBlockItemStack(block, 1, meta), compostingValue);
    }

    public static CompostItem addStack(Item item, int compostingValue) {
        return addStack(item, 0, compostingValue);
    }

    public static CompostItem addStackAnyDamage(Item item, int compostingValue) {
        return addStack(item, ANY_DMG, compostingValue);
    }

    public static CompostItem addStack(Item item, int meta, int compostingValue) {
        return add(new ItemStack(item, 1, meta), compostingValue);
    }
}
