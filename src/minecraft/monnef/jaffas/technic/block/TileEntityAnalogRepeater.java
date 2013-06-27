/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityAnalogRepeater extends TileEntityRedstoneCircuit {
    @Override
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
                cachedPower = getIndirectPowerFromSide(sourceX, sourceY, sourceZ, getInputSide());
                int redStoneWirePower = getRedstoneWirePowerLevel(sourceX, sourceY, sourceZ);
                if (redStoneWirePower > cachedPower) {
                    cachedPower = redStoneWirePower;
                }
            }
        }

        if (oldPower != cachedPower) {
            notifyBlocksOfMyChange();
        }
    }

    @Override
    protected Block getMyBlockUncached() {
        return JaffasTechnic.repeater;
    }

}
