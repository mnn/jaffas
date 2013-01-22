package monnef.jaffas.food.item;

import net.minecraft.item.ItemStack;

public class CustomDropEntry {
    public final float chance;
    public final ItemStack item;
    public boolean checkBabyFlag = false;
    public boolean expectedValueOfAdultFlag;

    public CustomDropEntry(float chance, ItemStack item) {
        this.chance = chance;
        this.item = item;
    }

    public CustomDropEntry setBabyFlagCheck(boolean expectedValueOfAdultFlag) {
        this.expectedValueOfAdultFlag = expectedValueOfAdultFlag;
        this.checkBabyFlag = true;
        return this;
    }
}
