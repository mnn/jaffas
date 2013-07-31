/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block.redstone;

import monnef.core.api.IIntegerCoordinates;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;

public class TileRandomizer extends TileRedstoneCircuit {
    private boolean isPoweredCached;

    private static final int RNG_MUL = 1103515245;
    private static final int RNG_ADD = 12345;
    private int rngCurrent = JaffasFood.rand.nextInt();

    public TileRandomizer() {
        generateRandomOutput();
    }

    @Override
    protected Block getMyBlockUncached() {
        return JaffasTechnic.randomizer;
    }

    @Override
    public boolean recalculatePower() {
        ForgeDirection inputDir = ForgeDirection.getOrientation(getInputSide());
        IIntegerCoordinates pos = (new IntegerCoordinates(this)).shiftInDirectionBy(inputDir, 1);
        int inputBlockId = pos.getBlockId();

        boolean isPoweredNew = false;
        int oldPower = cachedOutputPower;
        if (inputBlockId != 0) {
            Block inputBlock = Block.blocksList[inputBlockId];
            if (inputBlock != null) {
                int input = pos.getIndirectPowerFromSide(getInputSide());
                int redStoneWirePower = pos.getRedstoneWirePowerLevel();
                if (redStoneWirePower > input) {
                    input = redStoneWirePower;
                }
                isPoweredNew = input > 0;
            }
        }

        if (isPoweredNew != isPoweredCached) {
            isPoweredCached = isPoweredNew;
            if (isPoweredNew) {
                // only 0->1 edge
                generateRandomOutput();
            }
        }

        return oldPower != cachedOutputPower;
    }

    private void generateRandomOutput() {
        rngCurrent = rngCurrent * RNG_MUL + RNG_ADD;
        cachedOutputPower = rngCurrent % 16;
        if (cachedOutputPower < 0) cachedOutputPower = -cachedOutputPower;
    }

    @Override
    public boolean canConnectRedstone(int side) {
        return side == getInputSide() || side == getOutputSide();
    }
}
