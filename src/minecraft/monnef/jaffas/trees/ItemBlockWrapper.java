package monnef.jaffas.trees;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.lang.reflect.Field;

public class ItemBlockWrapper extends ItemBlock {
    public ItemBlockWrapper(int itemID, int blockID) {
        super(itemID);
        changeBlockID(blockID);
        this.setIconIndex(Block.blocksList[this.getBlockID()].getBlockTextureFromSide(2));
        isDefaultTexture = Block.blocksList[this.getBlockID()].isDefaultTexture;
    }

    private void changeBlockID(int blockID) {
        //this.blockID = par1 + 256;

        // Get the private field
        final Field field;
        try {
            field = this.getClass().getDeclaredField("blockID");
            field.setAccessible(true);
            field.set(this, blockID);
        } catch (NoSuchFieldException e) {
            printReflectionFailure();
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            printReflectionFailure();
            e.printStackTrace();
        }

        if (this.getBlockID() != blockID) {
            printReflectionFailure();
            throw new RuntimeException("getBlockID() != blockID");
        }
    }

    private void printReflectionFailure() {
        System.err.println("jaffas - unable to modify blockID in ItemBlock");
    }
}
