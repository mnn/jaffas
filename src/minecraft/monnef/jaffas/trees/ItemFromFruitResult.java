package monnef.jaffas.trees;

import net.minecraft.item.ItemStack;

public class ItemFromFruitResult {
    private ItemStack stack;
    private String message;
    public Exception exception;

    public ItemFromFruitResult() {
        this.message = null;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
