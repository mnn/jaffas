/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.utils.BlockHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import static monnef.jaffas.technic.JaffasTechnic.fermenter;

public class TileEntityFermenter extends TileEntity {
    private boolean integrityCheckScheduled = true;

    public void planIntegrityCheck() {
        if (worldObj.isRemote) return;
        integrityCheckScheduled = true;
    }

    @Override
    public void updateEntity() {
        // 2x bucket of brewed hops -> 1x keg

        checkBlockIntegrity();
    }

    private void checkBlockIntegrity() {
        if (worldObj.isRemote) return;
        if (integrityCheckScheduled) {
            integrityCheckScheduled = false;
            boolean failure = false;
            boolean removeBlock = false;

            int bId = worldObj.getBlockId(xCoord, yCoord + 1, zCoord);
            if (bId != getBlockType().blockID) {
                failure = true;
            } else {
                int meta = worldObj.getBlockMetadata(xCoord, yCoord + 1, zCoord);
                if (!fermenter.isSlave(meta)) {
                    failure = true;
                    removeBlock = true;
                }
            }

            if (failure) {
                if (removeBlock) {
                    BlockHelper.setBlock(worldObj, xCoord, yCoord + 1, zCoord, 0);
                }
                int myMeta = getBlockMetadata();
                BlockHelper.setBlock(worldObj, xCoord, yCoord, zCoord, 0);
                invalidate();
                fermenter.dropBlockAsItem(worldObj, xCoord, yCoord, zCoord, myMeta, 0); // last param is fortune
            }
        }
    }

    public boolean playerActivatedBox(EntityPlayer player) {
        if (!worldObj.isRemote) player.addChatMessage("click");
        return true;
    }
}
