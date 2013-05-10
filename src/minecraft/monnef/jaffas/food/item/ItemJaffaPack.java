/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.item.common.IItemPack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemJaffaPack extends ItemJaffaBase implements IItemPack {
    private ItemStack content;

    public ItemJaffaPack(int id) {
        super(id);
    }

    public ItemJaffaPack(int v, ItemStack content) {
        super(v);
        this.setContent(content);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        par3EntityPlayer.inventory.addItemStackToInventory(content.copy());

        par1ItemStack.stackSize--;
        return par1ItemStack;
    }

    protected void setContent(ItemStack content) {
        this.content = content;
    }

    @Override
    public Item Setup(ItemStack contents) {
        this.setContent(contents);
        return this;
    }
}
