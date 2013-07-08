/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block.redstone;

import monnef.core.api.IIntegerCoordinates;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityAnalogRepeater extends TileEntityRedstoneCircuit {
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
                cachedOutputPower = pos.getIndirectPowerFromSide(getInputSide());
                int redStoneWirePower = pos.getRedstoneWirePowerLevel();
                if (redStoneWirePower > cachedOutputPower) {
                    cachedOutputPower = redStoneWirePower;
                }
            }
        }

        return oldPower != cachedOutputPower;
    }

    @Override
    protected Block getMyBlockUncached() {
        return JaffasTechnic.repeater;
    }

    @Override
    public boolean canConnectRedstone(int side) {
        return side == getInputSide() || side == getOutputSide();
    }
}
