/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.common.MultiBlockHelper;
import net.minecraft.tileentity.TileEntity;

import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.AIR;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.CON_ALLOY;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.CON_GLASS;
import static monnef.jaffas.technic.common.MultiBlockHelper.TemplateMark.ME;

public class TileEntityCompostCore extends TileEntity {
    private static final TemplateMark[][] level0 = new TemplateMark[][]{
            new TemplateMark[]{CON_ALLOY, CON_ALLOY, CON_ALLOY},
            new TemplateMark[]{CON_ALLOY, ME, CON_ALLOY},
            new TemplateMark[]{CON_ALLOY, CON_ALLOY, CON_ALLOY}
    };
    private static final TemplateMark[][] level1 = new TemplateMark[][]{
            new TemplateMark[]{CON_ALLOY, CON_GLASS, CON_ALLOY},
            new TemplateMark[]{CON_GLASS, AIR, CON_GLASS},
            new TemplateMark[]{CON_ALLOY, CON_GLASS, CON_ALLOY}
    };
    private static final TemplateMark[][][] template = new TemplateMark[][][]{level0, level1};

    private boolean isValid;

    public boolean getIsValidMultiblock() {
        return isValid;
    }

    public void invalidateMultiblock() {
        isValid = false;
        revertDummies();
    }

    public boolean checkIfProperlyFormed() {
        return MultiBlockHelper.templateFits(worldObj, xCoord, yCoord, zCoord, template);
    }

    public void convertDummies() {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y <= 1; y++) {
                    if (x == 0 && z == 0) // air or me
                        continue;

                    int wx = x + xCoord;
                    int wy = y + yCoord;
                    int wz = z + zCoord;

                    worldObj.setBlock(wx, wy, wz, JaffasTechnic.dummyConstructionBlock.blockID);
                    worldObj.markBlockForUpdate(wx, wy, wz);
                    TileEntityConstructionDummy dummyTE = (TileEntityConstructionDummy) worldObj.getBlockTileEntity(wx, wy, wz);
                    dummyTE.setCore(this);
                }
            }
        }

        isValid = true;
    }

    private void revertDummies() {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y <= 1; y++) {
                    if (x == 0 && z == 0 && y == 0) // me
                        continue;

                    int wx = x + xCoord;
                    int wy = y + yCoord;
                    int wz = z + zCoord;
                    int blockId = worldObj.getBlockId(wx, wy, wz);

                    if (blockId != JaffasTechnic.dummyConstructionBlock.blockID)
                        continue;

                    MultiBlockHelper.setBlockByMark(template[y][z + 1][x + 1], worldObj, wx, wy, wz);
                    worldObj.markBlockForUpdate(wx, wy, wz);
                }
            }
        }
        isValid = false;
    }
}
