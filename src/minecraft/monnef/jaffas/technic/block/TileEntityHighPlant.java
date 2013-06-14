/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.tileentity.TileEntity;

public class TileEntityHighPlant extends TileEntity {
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

    public void setStructureHeight(int structureHeight) {
        if (structureHeight != 0) {
            throw new RuntimeException("overriding height setting");
        }
        this.structureHeight = structureHeight;
    }

    private void onQuantumTick() {
        if (integrityCheckScheduled) {
            integrityCheckScheduled = false;

            if (structureHeight == 0) {
                throw new RuntimeException("structureHeight is zero");
            }

            boolean failure = false;
            for (int i = 1; i < structureHeight; i++) {
                int yc = yCoord + i;
                int bId = worldObj.getBlockId(xCoord, yc, zCoord);
                int currMeta = worldObj.getBlockMetadata(xCoord, yc, zCoord);
                if (bId != getBlockType().blockID) {
                    failure = true;
                    break;
                }
                if (!JaffasTechnic.highPlant.isSlave(currMeta)) {
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
                invalidate();
            }
        }
    }


}
