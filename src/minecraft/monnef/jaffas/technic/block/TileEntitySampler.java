/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import static monnef.core.utils.DirectionHelper.opposite;

public class TileEntitySampler extends TileEntity {
    private int outputSide = -1;
    private int inputSide = -1;
    private int cachedPower = 0;
    private boolean initialized = false;
    private boolean recalculatePowerInNextTick;
    private int myBlockId;

    public int getOutputSide() {
        if (outputSide == -1) {
            outputSide = opposite(JaffasTechnic.sampler.getRotation(getBlockMetadata()));
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
            return cachedPower;
        }
        return 0;
    }

    public void recalculatePower() {
        ForgeDirection inputDir = ForgeDirection.getOrientation(getInputSide());
        int sourceX = xCoord + inputDir.offsetX;
        int sourceY = yCoord + inputDir.offsetY;
        int sourceZ = zCoord + inputDir.offsetZ;
        int inputBlockId = worldObj.getBlockId(sourceX, sourceY, sourceZ);
        int oldPower = cachedPower;
        if (inputBlockId == 0) {
            cachedPower = 0;
        } else {
            Block inputBlock = Block.blocksList[inputBlockId];
            if (inputBlock == null) {
                cachedPower = 0;
            } else {
                cachedPower = worldObj.getIndirectPowerLevelTo(sourceX, sourceY, sourceZ, getInputSide()); // orig. getOutputSide()
                int redStoneWirePower = worldObj.getBlockId(sourceX, sourceY, sourceZ) == Block.redstoneWire.blockID ? worldObj.getBlockMetadata(sourceX, sourceY, sourceZ) : 0;
                if (redStoneWirePower > cachedPower) {
                    cachedPower = redStoneWirePower;
                }
            }
        }

        if (oldPower != cachedPower) {
            //forceUpdateNeighbours();
            //notifyOutputNeighbour();
            notifyBlocksOfMyChange();
        }
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

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (recalculatePowerInNextTick) {
            recalculatePowerInNextTick = false;
            recalculatePower();
        }
    }

    private void init() {
        if (!initialized) {
            initialized = true;
            recalculatePowerInNextTick = true;
        }
    }

    public int getMyBlockId() {
        if (myBlockId == -1) {
            myBlockId = JaffasTechnic.sampler.blockID;
        }

        return myBlockId;
    }
}
