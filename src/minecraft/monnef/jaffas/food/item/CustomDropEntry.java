package monnef.jaffas.food.item;

import net.minecraft.item.ItemStack;

public class CustomDropEntry {
    public final float chance;
    public final ItemStack item;

    public CustomDropEntry(float chance, ItemStack item) {
        this.chance = chance;
        this.item = item;
    }
}
