/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityConstructionDummy extends TileEntity {
    private TileEntityCompostCore core;
    int coreX;
    int coreY;
    int coreZ;

    public void setCore(TileEntityCompostCore core) {
        this.core = core;
        coreX = core.xCoord;
        coreY = core.yCoord;
        coreZ = core.zCoord;
    }

    public TileEntityCompostCore getCore() {
        if (core == null)
            core = (TileEntityCompostCore) worldObj.getBlockTileEntity(coreX, coreY, coreZ);

        return core;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        coreX = tagCompound.getInteger("CoreX");
        coreY = tagCompound.getInteger("CoreY");
        coreZ = tagCompound.getInteger("CoreZ");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("CoreX", coreX);
        tagCompound.setInteger("CoreY", coreY);
        tagCompound.setInteger("CoreZ", coreZ);
    }
}
