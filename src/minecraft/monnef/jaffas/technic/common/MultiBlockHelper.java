/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.world.World;

public class MultiBlockHelper {
    public enum TemplateMark {
        ME,
        AIR,
        CON_ALLOY,
        CON_GLASS
    }

    public static boolean templateFits(World w, int x, int y, int z, TemplateMark[][][] levels) {
        int[] myPos = findMe(levels);
        if (myPos == null) {
            throw new RuntimeException("Invalid template");
        }

        int nx = x - myPos[0];
        int ny = y - myPos[1];
        int nz = z - myPos[2];

        for (int cy = 0; cy < levels.length; cy++) {
            TemplateMark[][] currPlane = levels[cy];
            for (int cz = 0; cz < currPlane.length; cz++) {
                TemplateMark[] currLine = currPlane[cz];
                for (int cx = 0; cx < currLine.length; cx++) {
                    TemplateMark mark = currLine[cx];
                    if (!markCorresponds(mark, w, x, y, z, nx + cx, ny + cy, nz + cz)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static int[] findMe(TemplateMark[][][] levels) {
        for (int y = 0; y < levels.length; y++) {
            TemplateMark[][] currPlane = levels[y];
            for (int z = 0; z < currPlane.length; z++) {
                TemplateMark[] currLine = currPlane[z];
                for (int x = 0; x < currLine.length; x++) {
                    if (currLine[x] == TemplateMark.ME) {
                        return new int[]{x, y, z};
                    }
                }
            }
        }

        return null;
    }

    private static boolean markCorresponds(TemplateMark mark, World w, int x, int y, int z, int cx, int cy, int cz) {
        if (mark == TemplateMark.ME) {
            return x == cx && y == cy && z == cz;
        }

        int blockId = w.getBlockId(cx, cy, cz);
        int meta = w.getBlockMetadata(cx, cy, cz);
        switch (mark) {
            case AIR:
                return blockId == 0;

            case CON_ALLOY:
                return blockId == JaffasTechnic.constructionBlock.blockID && meta == 0;

            case CON_GLASS:
                return blockId == JaffasTechnic.constructionBlock.blockID && meta == 1;
        }

        throw new RuntimeException("unknown mark");
    }

    public static void setBlockByMark(TemplateMark mark, World w, int x, int y, int z) {
        switch (mark) {
            case ME:
                throw new RuntimeException("cannot set ME");

            case AIR:
                w.setBlock(x, y, z, 0);
                break;

            case CON_ALLOY:
                BlockHelper.setBlock(w, x, y, z, JaffasTechnic.constructionBlock.blockID, 0);
                break;

            case CON_GLASS:
                BlockHelper.setBlock(w, x, y, z, JaffasTechnic.constructionBlock.blockID, 1);
                break;
        }
    }
}
