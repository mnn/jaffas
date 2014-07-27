/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.common;

import monnef.core.api.IIntegerCoordinates;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.power.block.BlockWindGenerator;
import monnef.jaffas.power.block.TileWindGenerator;
import net.minecraft.block.Block;

public abstract class WindObstaclesBase implements IWindObstacles {
    protected float obstaclesVolumeCached;
    protected int obstaclesCounted;
    protected TileWindGenerator tile;
    protected float maximalVolume;
    protected int totalRadius;
    protected int totalDiameter;

    public WindObstaclesBase(TileWindGenerator tile) {
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
        resetSpace();
        resetProcessedCoordinates();
    }

    protected abstract void resetSpace();

    private void resetVolumeCached() {
        obstaclesVolumeCached = 0;
        obstaclesCounted = 0;
    }

    protected abstract void resetProcessedCoordinates();

    @Override
    public int getTotalRadius() {
        return totalRadius;
    }

    @Override
    public int getTotalDiameter() {
        return totalDiameter;
    }

    protected int getStartingValueOfRelativeZ() {
        return tile.getTurbine() != null && tile.getTurbine().doesCheckBack() ? -TileWindGenerator.checkDistance : 1;
    }

    @Override
    public abstract void compute();

    @Override
    public abstract float debugCompute();

    protected IIntegerCoordinates computeAbsoluteCoordinates(int rx, int ry, int rz) {
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

    protected float getBlockObstacleValue(IIntegerCoordinates pos) {
        Block block = pos.getBlock();
        if (block.isOpaqueCube()) return 1;
        if (block instanceof BlockWindGenerator) return 0;
        return 0.2f;
    }

    protected int getBlocksToProcessEveryCompute() {
        return TileWindGenerator.blocksToCheckPerTick * tile.getSlowingCoefficient();
    }

    protected int getCheckDistance() {
        return TileWindGenerator.checkDistance;
    }
}
