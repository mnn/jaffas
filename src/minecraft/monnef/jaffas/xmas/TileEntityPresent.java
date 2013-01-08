package monnef.jaffas.xmas;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class TileEntityPresent extends TileEntity {
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
