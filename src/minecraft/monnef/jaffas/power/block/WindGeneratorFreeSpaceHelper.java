/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.core.api.IIntegerCoordinates;
import monnef.core.utils.IntegerCoordinates;

import java.util.ArrayList;
import java.util.List;

public class WindGeneratorFreeSpaceHelper {
    private static final int MAX_RADIUS = 10;

    private static final List<List<Coords>> precomputedRings;

    private static class Coords {
        public final int x;
        public final int y;

        private Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static {
        precomputedRings = new ArrayList<List<Coords>>(MAX_RADIUS + 1);
        for (int radius = 0; radius <= MAX_RADIUS; radius++) {
            ArrayList<Coords> currCoordList = new ArrayList<Coords>(radius * 4);
            for (int rx = -radius; rx <= radius; rx++) {
                currCoordList.add(new Coords(rx, radius));
                currCoordList.add(new Coords(rx, -radius));
            }
            for (int ry = -radius; ry <= radius; ry++) {
                currCoordList.add(new Coords(radius, ry));
                currCoordList.add(new Coords(-radius, ry));
            }
            precomputedRings.add(currCoordList);
        }
    }

    public static class FreeSpaceResult {
        private final int freeRadius;
        private final IIntegerCoordinates collidingBlock;

        public FreeSpaceResult(int freeRadius, IIntegerCoordinates collidingBlock) {
            this.freeRadius = freeRadius;
            this.collidingBlock = collidingBlock;
        }

        public int getFreeRadius() {
            return freeRadius;
        }

        public IIntegerCoordinates getCollidingBlock() {
            return collidingBlock;
        }
    }

    public static FreeSpaceResult compute(TileWindGenerator tile) {
        for (int radius = 0; radius <= MAX_RADIUS; radius++) {
            for (Coords c : precomputedRings.get(radius)) {
                IIntegerCoordinates err = checkCoords(tile, c);
                if (err != null) {
                    return new FreeSpaceResult(radius - 1, err);
                }
            }
        }

        return new FreeSpaceResult(MAX_RADIUS, null);
    }

    private static IIntegerCoordinates checkCoords(TileWindGenerator tile, Coords c) {
        IIntegerCoordinates pos = new IntegerCoordinates(tile).applyRelativeCoordinates(tile.getRotation(), c.x, c.y, 1);
        return pos.getBlockId() == 0 ? null : pos;
    }

}
