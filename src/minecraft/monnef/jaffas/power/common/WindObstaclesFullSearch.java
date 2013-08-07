/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import monnef.core.api.IIntegerCoordinates;
import monnef.core.collection.SpaceHashMap;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.power.block.BlockWindGenerator;
import monnef.jaffas.power.block.TileWindGenerator;
import net.minecraft.block.Block;

public class WindObstaclesFullSearch implements IWindObstacles {
    private SpaceHashMap<Integer, Float> obstacles;
    private float obstaclesVolumeCached;
    private float maximalVolume;
    private int obstaclesCounted;
    private int totalRadius;
    private int totalDiameter;

    private int lastProcessedRX;
    private int lastProcessedRZ;
    private int lastProcessedRY;
    private TileWindGenerator tile;

    public WindObstaclesFullSearch(TileWindGenerator tile) {
        this.tile = tile;
        reset();
    }

    @Override
    public void reset() {
        totalRadius = TileWindGenerator.checkOuterRadius;
        if (tile.getTurbine() != null) totalRadius += tile.getTurbine().getRadius();
        totalDiameter = totalRadius * 2 + 1;
        maximalVolume = TileWindGenerator.checkDistance * totalDiameter * totalDiameter;
        resetVolumeCached();
        obstacles = new SpaceHashMap<Integer, Float>();
        resetProcessedCoordinates();
    }

    private void resetVolumeCached() {
        obstaclesVolumeCached = 0;
        obstaclesCounted = 0;
    }

    private void resetProcessedCoordinates() {
        lastProcessedRX = -totalRadius;
        lastProcessedRY = -totalRadius;
        lastProcessedRZ = getStartingValueOfRelativeZ();
    }

    @Override
    public int getTotalRadius() {
        return totalRadius;
    }

    @Override
    public int getTotalDiameter() {
        return totalDiameter;
    }

    private int getStartingValueOfRelativeZ() {
        return tile.getTurbine() != null && tile.getTurbine().doesCheckBack() ? -TileWindGenerator.checkDistance : 1;
    }

    private float getBlockObstacleValue(IIntegerCoordinates pos) {
        Block block = Block.blocksList[pos.getBlockId()];
        if (block == null) return 0;
        if (block.isOpaqueCube()) return 1;
        if (block instanceof BlockWindGenerator) return 0;
        return 0.2f;
    }

    @Override
    public void compute() {
        int toCheckLeft = TileWindGenerator.blocksToCheckPerTick * tile.getSlowingCoefficient();
        boolean firstIteration = true;
        boolean lastProcessedValuesWereSet = false;
        int rxStart = -totalRadius;
        int ryStart = -totalRadius;
        int rzStart = getStartingValueOfRelativeZ();
        for (int rx = rxStart; rx <= totalRadius; rx++) {
            for (int ry = ryStart; ry <= totalRadius; ry++) {
                for (int rz = rzStart; rz <= TileWindGenerator.checkDistance; rz++) {
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
                for (int rz = startingValueOfRelativeZ; rz <= TileWindGenerator.checkDistance; rz++) {
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

    private IIntegerCoordinates computeAbsoluteCoordinates(int rx, int ry, int rz) {
        return new IntegerCoordinates(tile).applyRelativeCoordinates(tile.getRotation(), rx, ry, rz);
    }

    @Override
    public float getObstaclesVolumeWorstScenario() {
        return obstaclesVolumeCached + (maximalVolume - obstaclesCounted);
    }

    @Override
    public float getObstaclesVolumeCached() {
        return obstaclesVolumeCached;
    }
}
