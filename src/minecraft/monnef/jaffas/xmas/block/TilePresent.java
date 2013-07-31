/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.block;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TilePresent extends TileEntity {
    private ItemStack content = null;

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        boolean empty = tagCompound.getBoolean("empty");
        if (!empty) {
            NBTTagCompound i = (NBTTagCompound) tagCompound.getTag("item");
            content = ItemStack.loadItemStackFromNBT(i);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        boolean empty = content == null;

        tagCompound.setBoolean("empty", empty);
        NBTTagCompound i = new NBTTagCompound();
        if (!empty) {
            content.writeToNBT(i);
        }
        tagCompound.setTag("item", i);
    }

    public ItemStack getContent() {
        return content;
    }

    public void setContent(ItemStack content) {
        this.content = content;
    }
}
