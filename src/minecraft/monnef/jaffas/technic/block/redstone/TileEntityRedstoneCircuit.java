/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block.redstone;

import monnef.core.api.IIntegerCoordinates;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.technic.block.BlockDirectionalTechnic;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import static monnef.core.utils.DirectionHelper.opposite;

public abstract class TileEntityRedstoneCircuit extends TileEntity {
    protected int cachedOutputPower = 0;
    private int outputSide = -1;
    private int inputSide = -1;
    private int myBlockId;
    private boolean initialized = false;
    private boolean recalculatePowerInNextTick;

    public int getOutputSide() {
        if (outputSide == -1) {
            outputSide = opposite(((BlockDirectionalTechnic) getMyBlockUncached()).getRotation(getBlockMetadata()));
        }
        return outputSide;
    }

    public int getInputSide() {
        if (inputSide == -1) {
            inputSide = opposite(getOutputSide());
        }
        return inputSide;
    }

    public int getCurrentPowerFromSide(int side) {
        if (opposite(side) == getOutputSide()) {
            return cachedOutputPower;
        }
        return 0;
    }

    public void forceUpdateNeighbours() {
        worldObj.notifyBlocksOfNeighborChange(xCoord + 1, yCoord, zCoord, getMyBlockId());
        worldObj.notifyBlocksOfNeighborChange(xCoord - 1, yCoord, zCoord, getMyBlockId());
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord + 1, getMyBlockId());
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord - 1, getMyBlockId());
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getMyBlockId());
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord + 1, zCoord, getMyBlockId());

        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getMyBlockId());
    }

    public void notifyOutputNeighbour() {
        ForgeDirection outDir = ForgeDirection.getOrientation(getOutputSide());
        int nx = xCoord + outDir.offsetX;
        int ny = yCoord + outDir.offsetY;
        int nz = zCoord + outDir.offsetZ;
        worldObj.notifyBlocksOfNeighborChange(nx, ny, nz, getMyBlockId());
    }

    public void notifyOutputNeighbourTwo() {
        ForgeDirection outDir = ForgeDirection.getOrientation(getOutputSide());
        int nx = xCoord + 2 * outDir.offsetX;
        int ny = yCoord + 2 * outDir.offsetY;
        int nz = zCoord + 2 * outDir.offsetZ;
        worldObj.notifyBlocksOfNeighborChange(nx, ny, nz, getMyBlockId());
    }

    public void notifyBlocksOfMyChange() {
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getMyBlockId());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        init();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
    }

    @Override
    public void validate() {
        super.validate();
        init();
    }

    protected void init() {
        if (!initialized) {
            initialized = true;
            recalculatePowerInNextTick = true;
        }
    }

    public int getMyBlockId() {
        if (myBlockId == -1) {
            myBlockId = getMyBlockUncached().blockID;
        }

        return myBlockId;
    }

    protected abstract Block getMyBlockUncached();

    public abstract boolean recalculatePower();

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (recalculatePowerInNextTick) {
            recalculatePowerInNextTick = false;
            recalculatePower();
        }
    }

    public abstract boolean canConnectRedstone(int side);

    protected int getMaximumPowerFromSides() {
        int max = 0;
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            int dirOrd = dir.ordinal();
            if (dirOrd != getInputSide() && dirOrd != getOutputSide()) {
                IIntegerCoordinates pos = (new IntegerCoordinates(this)).shiftInDirectionBy(dir, 1);
                int curr = pos.getIndirectPowerFromSide(dirOrd);

                if (curr > max) max = curr;
            }
        }
        return max;
    }
}
