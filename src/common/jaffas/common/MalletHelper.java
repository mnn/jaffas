package jaffas.common;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class MalletHelper {
    private ItemStack mallet;
    private int position;
    private int ingredientsCount;

    public MalletHelper(ItemStack item, int position, int ingredientsCount) {
        setMallet(item);
        setPosition(position);
        setIngredientsCount(ingredientsCount);
    }

    public static MalletHelper findMallet(IInventory matrix) {
        int ingredientsCount = 0;
        MalletHelper result = null;

        for (int i = 0; i < matrix.getSizeInventory(); i++) {
            ItemStack item = matrix.getStackInSlot(i);
            if (item != null) {
                ingredientsCount++;
                if (isMallet(item.itemID)) {
                    result = new MalletHelper(item, i, ingredientsCount);
                }
            }
        }

        if(result!=null) result.setIngredientsCount(ingredientsCount);
        return result;
    }

    public static boolean isMallet(int itemID) {
        // TODO: extract shifted stuff getting
        return itemID == mod_jaffas.ItemsInfo.get(mod_jaffas.JaffaItem.mallet).getItem().shiftedIndex;
    }

    public ItemStack getMallet() {
        return mallet;
    }

    public int getPosition() {
        return position;
    }

    public void setMallet(ItemStack mallet) {
        this.mallet = mallet;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setIngredientsCount(int ingredientsCount) {
        this.ingredientsCount = ingredientsCount;
    }

    public int getIngredientsCount() {
        return this.ingredientsCount;
    }
}
