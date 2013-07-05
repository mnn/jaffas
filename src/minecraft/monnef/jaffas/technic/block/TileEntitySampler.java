/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public class TileEntitySampler extends TileEntityRedstoneCircuit {
    private static final String POWER_TAG = "power";

    @Override
    public boolean recalculatePower() {
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
                int sidePower = getMaximumPowerFromSides();
                if (sidePower != 0) {
                    cachedPower = getIndirectPowerFromSide(sourceX, sourceY, sourceZ, getInputSide());
                    int redStoneWirePower = getRedstoneWirePowerLevel(sourceX, sourceY, sourceZ);
                    if (redStoneWirePower > cachedPower) {
                        cachedPower = redStoneWirePower;
                    }
                } else {
                    // remains unchanged
                }
            }
        }

        return oldPower != cachedPower;
    }

    @Override
    public boolean canConnectRedstone(int side) {
        return true;
    }

    private int getMaximumPowerFromSides() {
        int max = 0;
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            int dirOrd = dir.ordinal();
            if (dirOrd != getInputSide() && dirOrd != getOutputSide()) {
                int x = xCoord + dir.offsetX;
                int y = yCoord + dir.offsetY;
                int z = zCoord + dir.offsetZ;
                int curr = getIndirectPowerFromSide(x, y, z, dirOrd);
                if (curr > max) max = curr;
            }
        }
        return max;
    }

    @Override
    protected Block getMyBlockUncached() {
        return JaffasTechnic.repeater;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        cachedPower = tag.getByte(POWER_TAG);
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte(POWER_TAG, (byte) cachedPower);
    }


}
