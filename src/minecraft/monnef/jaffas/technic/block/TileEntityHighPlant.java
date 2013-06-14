/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.utils.BlockHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.technic.JaffasTechnic.highPlant;

public class TileEntityHighPlant extends TileEntity {
    private static final String STRUCTURE_HEIGHT_TAG = "height";
    private int counter = -1;
    private final static int TICK_QUANTUM = 20;

    private boolean integrityCheckScheduled = true;

    private int structureHeight;

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;

        counter++;
        if (counter >= TICK_QUANTUM) {
            counter = 0;

            onQuantumTick();
        }
    }

    public void setStructureHeight(int newHeight) {
        if (structureHeight != 0) {
            Log.printWarning("overriding height setting");
        }
        structureHeight = newHeight;
    }

    private void onQuantumTick() {
        checkBlockIntegrity();
    }

    public void planIntegrityCheck() {
        if (worldObj.isRemote) return;
        integrityCheckScheduled = true;
    }

    private void checkBlockIntegrity() {
        if (worldObj.isRemote) return;
        if (integrityCheckScheduled) {
            integrityCheckScheduled = false;
            boolean failure = false;

            if (structureHeight == 0) {
                failure = true;
                Log.printSevere(String.format("%s: structureHeight is zero, destroying myself(%d, %d, %d @ %d).", this.getClass().getSimpleName(), xCoord, yCoord, zCoord, worldObj.provider.dimensionId));
            }

            for (int i = 1; i < structureHeight; i++) {
                if (failure) break;
                int yc = yCoord + i;
                int bId = worldObj.getBlockId(xCoord, yc, zCoord);
                int currMeta = worldObj.getBlockMetadata(xCoord, yc, zCoord);
                if (bId != getBlockType().blockID) {
                    failure = true;
                    break;
                }
                if (!highPlant.isSlave(currMeta)) {
                    failure = true;
                }
            }

            if (failure) {
                for (int i = 1; i < structureHeight; i++) {
                    int yc = yCoord + i;
                    if (worldObj.getBlockId(xCoord, yc, zCoord) == getBlockType().blockID) {
                        BlockHelper.setBlock(worldObj, xCoord, yc, zCoord, 0);
                    }
                }
                int myMeta = getBlockMetadata();
                BlockHelper.setBlock(worldObj, xCoord, yCoord, zCoord, 0);
                invalidate();
                highPlant.dropBlockAsItem(worldObj, xCoord, yCoord, zCoord, myMeta, 0); // last param is fortune
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        structureHeight = tag.getByte(STRUCTURE_HEIGHT_TAG);
        if (structureHeight == 0) {
            Log.printWarning(String.format("TEHighPlant loaded incorrect data, self-destroying."));
            BlockHelper.setBlock(worldObj, xCoord, yCoord, zCoord, 0);
            invalidate();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte(STRUCTURE_HEIGHT_TAG, (byte) structureHeight);
    }
}
