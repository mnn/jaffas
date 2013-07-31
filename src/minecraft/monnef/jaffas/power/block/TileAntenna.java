/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.TileEntityMachine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public class TileAntenna extends TileEntityMachine {
    private boolean lit;

    public TileAntenna() {
    }

    @Override
    public String getMachineTitle() {
        return "Antenna";
    }

    public ForgeDirection changeRotation() {
        if (worldObj.isRemote) return ForgeDirection.UNKNOWN;

        //PowerUtils.disconnectSafelyDirectPowerConsumer(this.consumerManager);

        int rotation = this.getRotation().ordinal();
        rotation++;
        rotation %= ForgeDirection.VALID_DIRECTIONS.length;
        this.setRotation(ForgeDirection.getOrientation(rotation));

        //consumerManager.tryDirectConnect();

        sendUpdate();

        return ForgeDirection.VALID_DIRECTIONS[rotation];
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    public boolean isLit() {
        return lit;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    protected void doMachineWork() {
    }

    @Override
    public boolean toggleRotation() {
        this.changeRotation();
        return true;
    }
}
