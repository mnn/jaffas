package monnef.jaffas.technic.common;

import com.google.common.collect.HashMultimap;
import monnef.core.utils.ItemHelper;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class CompostRegister {
    private static HashMultimap<Integer, CompostItem> compostItems;

    static {
        compostItems = HashMultimap.create();
    }

    public void add(ItemStack stack, int compostingValue) {
        if (stack == null) {
            throw new RuntimeException("invalid stack in item adding to compost register");
        }
        if (compostingValue <= 0) {
            throw new RuntimeException("invalid composting value");
        }

        compostItems.put(stack.itemID, new CompostItem(stack.copy(), compostingValue));
    }

    public int getCompostValue(ItemStack input) {
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
}
