package monnef.jaffas.power.common;

import com.google.common.collect.HashBasedTable;
import monnef.core.api.IIntegerCoordinates;
import monnef.jaffas.power.block.TileWindGenerator;

public class WindObstaclesFastFail extends WindObstaclesBase {
    private HashBasedTable<Integer, Integer, Float> obstacles;
    private int lastRX;
    private int lastRY;
    private float maximalZColumnValue;

    public WindObstaclesFastFail(TileWindGenerator tile) {
        super(tile);
    }

    @Override
    public void reset() {
        super.reset();
        maximalZColumnValue = getCheckDistance();
    }

    @Override
    protected void resetSpace() {
        obstacles = HashBasedTable.create(totalDiameter, totalDiameter);
    }

    @Override
    protected void resetProcessedCoordinates() {
        lastRX = -totalRadius;
        lastRY = -totalRadius;
    }

    @Override
    public void compute() {
        int toCheckLeft = getBlocksToProcessEveryCompute();
        boolean firstIteration = true;
        boolean lastProcessedValuesWereSet = false;
        int rxStart = -totalRadius;
        int ryStart = -totalRadius;
        for (int rx = rxStart; rx <= totalRadius; rx++) {
            for (int ry = ryStart; ry <= totalRadius; ry++) {
                if (firstIteration) {
                    firstIteration = false;
                    rx = lastRX;
                    ry = lastRY;
                }

                toCheckLeft -= getCheckDistance();
                if (toCheckLeft < 0) {
                    lastProcessedValuesWereSet = true;
                    lastRX = rx;
                    lastRY = ry;
                    break;
                }

                processRelativeZColumnAtCoordinate(rx, ry);
            }
            if (lastProcessedValuesWereSet) break;
        }

        // calculation has reached end
        if (!lastProcessedValuesWereSet) {
            resetProcessedCoordinates();
        }
    }

    private void processRelativeZColumnAtCoordinate(int rx, int ry) {
        IIntegerCoordinates absCoords = computeAbsoluteCoordinates(rx, ry, 0);
        Float currentCellValue = getCellValue(rx, ry);
        if (currentCellValue == null) {
            obstaclesCounted += getZColumnLength();
        } else {
            obstaclesVolumeCached -= currentCellValue;
        }
        float currentObstacleValue = getZColumnValue(absCoords);
        obstaclesVolumeCached += currentObstacleValue;
        setCellValue(rx, ry, currentObstacleValue);
    }

    private int getZColumnLength() {
        return getCheckDistance() - getStartingValueOfRelativeZ() + 1;
    }

    private float getZColumnValue(IIntegerCoordinates absCoords) {
        int maxZ = getCheckDistance();
        int minZ = getStartingValueOfRelativeZ();
        float currVolume = 0;
        int processed = 0;
        int expectedBlocksCount = getZColumnLength();
        for (int i = minZ; i <= maxZ; i++) {
            IIntegerCoordinates currPos = absCoords.shiftInDirectionBy(tile.getRotation(), i);
            float lastScore = getBlockObstacleValue(currPos);
            if (lastScore < 1) {
                currVolume += lastScore;
                processed++;
            } else {
                break;
            }
        }

        if (expectedBlocksCount > processed) {
            currVolume += expectedBlocksCount - processed;
        }

        return currVolume;
    }

    private void setCellValue(int rx, int ry, float value) {
        obstacles.put(rx, ry, value);
    }

    private Float getCellValue(int rx, int ry) {
        return obstacles.get(rx, ry);
    }

    @Override
    public float debugCompute() {
        float res = 0;
        for (int rx = -totalRadius; rx <= totalRadius; rx++) {
            for (int ry = -totalRadius; ry <= totalRadius; ry++) {
                res += getZColumnValueDebug(computeAbsoluteCoordinates(rx, ry, 0));
            }
        }
        return res;
    }

    private float getZColumnValueDebug(IIntegerCoordinates absCoords) {
        int maxZ = getCheckDistance();
        int minZ = getStartingValueOfRelativeZ();
        float currVolume = 0;
        boolean badFlag = false;
        for (int i = minZ; i <= maxZ; i++) {
            IIntegerCoordinates currPos = absCoords.shiftInDirectionBy(tile.getRotation(), i);
            float lastScore = getBlockObstacleValue(currPos);
            if (lastScore >= 1) {
                badFlag = true;
            }
            currVolume += badFlag ? 1 : lastScore;
        }

        return currVolume;
    }
}
