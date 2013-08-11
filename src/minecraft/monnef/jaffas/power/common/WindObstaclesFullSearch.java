/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import monnef.core.api.IIntegerCoordinates;
import monnef.core.collection.SpaceHashMap;
import monnef.jaffas.power.block.TileWindGenerator;

public class WindObstaclesFullSearch extends WindObstaclesBase {
    private SpaceHashMap<Integer, Float> obstacles;

    private int lastProcessedRX;
    private int lastProcessedRZ;
    private int lastProcessedRY;

    public WindObstaclesFullSearch(TileWindGenerator tile) {
        super(tile);
    }

    @Override
    protected void resetProcessedCoordinates() {
        lastProcessedRX = -totalRadius;
        lastProcessedRY = -totalRadius;
        lastProcessedRZ = getStartingValueOfRelativeZ();
    }

    @Override
    public void compute() {
        int toCheckLeft = getBlocksToProcessEveryCompute();
        boolean firstIteration = true;
        boolean lastProcessedValuesWereSet = false;
        int rxStart = -totalRadius;
        int ryStart = -totalRadius;
        int rzStart = getStartingValueOfRelativeZ();
        for (int rx = rxStart; rx <= totalRadius; rx++) {
            for (int ry = ryStart; ry <= totalRadius; ry++) {
                for (int rz = rzStart; rz <= getCheckDistance(); rz++) {
                    if (firstIteration) {
                        firstIteration = false;
                        rx = lastProcessedRX;
                        ry = lastProcessedRY;
                        rz = lastProcessedRZ;
                    }

                    toCheckLeft--;
                    if (toCheckLeft < 0) {
                        lastProcessedValuesWereSet = true;
                        lastProcessedRX = rx;
                        lastProcessedRY = ry;
                        lastProcessedRZ = rz;
                        break;
                    }

                    processRelativeBlockAtCoordinate(rx, ry, rz);
                }
                if (lastProcessedValuesWereSet) break;
            }
            if (lastProcessedValuesWereSet) break;
        }

        // calculation has reached end
        if (!lastProcessedValuesWereSet) {
            resetProcessedCoordinates();
        }
    }

    /**
     * Performance heavy version of {@link #compute()} method
     *
     * @return obstacles volume
     */
    @Override
    public float debugCompute() {
        float res = 0;
        int startingValueOfRelativeZ = getStartingValueOfRelativeZ();
        for (int rx = -totalRadius; rx <= totalRadius; rx++) {
            for (int ry = -totalRadius; ry <= totalRadius; ry++) {
                for (int rz = startingValueOfRelativeZ; rz <= getCheckDistance(); rz++) {
                    res += getBlockObstacleValue(computeAbsoluteCoordinates(rx, ry, rz));
                }
            }
        }
        return res;
    }

    private void processRelativeBlockAtCoordinate(int rx, int ry, int rz) {
        IIntegerCoordinates absCoords = computeAbsoluteCoordinates(rx, ry, rz);
        Float currentCellValue = getCellValue(rx, ry, rz);
        if (currentCellValue == null) {
            obstaclesCounted++;
        } else {
            obstaclesVolumeCached -= currentCellValue;
        }
        float currentObstacleValue = getBlockObstacleValue(absCoords);
        obstaclesVolumeCached += currentObstacleValue;
        setCellValue(rx, ry, rz, currentObstacleValue);
    }

    private void setCellValue(int rx, int ry, int rz, float value) {
        obstacles.put(rx, ry, rz, value);
    }

    private Float getCellValue(int rx, int ry, int rz) {
        return obstacles.get(rx, ry, rz);
    }

    protected void resetSpace() {
        obstacles = new SpaceHashMap<Integer, Float>();
    }
}
