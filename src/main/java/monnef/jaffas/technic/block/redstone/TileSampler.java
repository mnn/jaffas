/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block.redstone;

import monnef.core.api.IIntegerCoordinates;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public class TileSampler extends TileRedstoneCircuit {
    private static final String POWER_TAG = "power";

    @Override
    public boolean recalculatePower() {
        ForgeDirection inputDir = ForgeDirection.getOrientation(getInputSide());
        IIntegerCoordinates pos = (new IntegerCoordinates(this)).shiftInDirectionBy(inputDir, 1);
        int inputBlockId = pos.getBlockId();

        int oldPower = cachedOutputPower;
        if (inputBlockId == 0) {
            cachedOutputPower = 0;
        } else {
            Block inputBlock = Block.blocksList[inputBlockId];
            if (inputBlock == null) {
                cachedOutputPower = 0;
            } else {
                int sidePower = getMaximumPowerFromSides();
                if (sidePower != 0) {
                    cachedOutputPower = pos.getIndirectPowerFromSide(getInputSide());
                    int redStoneWirePower = pos.getRedstoneWirePowerLevel();
                    if (redStoneWirePower > cachedOutputPower) {
                        cachedOutputPower = redStoneWirePower;
                    }
                } else {
                    // remains unchanged
                }
            }
        }

        return oldPower != cachedOutputPower;
    }

    @Override
    public boolean canConnectRedstone(int side) {
        return true;
    }

    @Override
    protected Block getMyBlockUncached() {
        return JaffasTechnic.repeater;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        cachedOutputPower = tag.getByte(POWER_TAG);
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte(POWER_TAG, (byte) cachedOutputPower);
    }


}
