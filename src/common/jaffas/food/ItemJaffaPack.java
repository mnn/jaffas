package jaffas.food;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemJaffaPack extends ItemJaffaBase {
    private ItemStack content;

    public ItemJaffaPack(int v, ItemStack content) {
        super(v);

        this.content = content;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        par3EntityPlayer.inventory.addItemStackToInventory(content.copy());

        par1ItemStack.stackSize--;
        return par1ItemStack;
    }
}
