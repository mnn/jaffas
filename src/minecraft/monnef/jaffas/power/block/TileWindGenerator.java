/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import monnef.core.api.IIntegerCoordinates;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import monnef.jaffas.power.item.ItemWindTurbine;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileWindGenerator extends TileEntityMachineWithInventory {

    public static final int TURBINE_SLOT = 0;

    private enum TurbineState {UNKNOWN, NO_TURBINE, TURBINE_SPAWNED}

    private enum CustomerState {NONE, BACK, BOTTOM}

    private TurbineState turbineState = TurbineState.UNKNOWN;
    private CustomerState customerState = CustomerState.NONE;

    public static float maxNormalEnergyPerTick = 5;
    private static final float lossPerSolidBlock = 0.05f;
    private static final int checkDistance = 15;
    private static final int checkOuterRadius = 2;
    public static int blocksToCheckPerTick = 5;
    public static float rainBonusMax = 1.2f; // max +120%

    private Obstacles obstacles = new Obstacles();

    private ItemWindTurbine turbine;

    private class Obstacles {
        public static final int RANDOM_LOW_VALUE = -100;
        private Table<Integer, Integer, Float> obstacles;
        private float obstaclesVolumeCached;
        private float maximalVolume;
        private int obstaclesCounted;
        private int totalRadius;

        private int lastProcessedRX;
        private int lastProcessedRZ;
        private int lastProcessedRY;

        private Obstacles() {
            reset();
        }

        public void reset() {
            totalRadius = checkOuterRadius;
            if (turbine != null) totalRadius += turbine.getRadius();
            maximalVolume = checkDistance * totalRadius;
            obstaclesVolumeCached = 0;
            obstaclesCounted = 0;
            int totalDiameter = totalRadius * 2 + 1;
            obstacles = HashBasedTable.create(totalDiameter, checkDistance);
            resetProcessedCoordinates();
        }

        private void resetProcessedCoordinates() {
            lastProcessedRX = -totalRadius;
            lastProcessedRY = -totalRadius;
            lastProcessedRZ = 1;
            if (turbine != null && turbine.doesCheckBack()) lastProcessedRZ = -checkDistance;
        }

        private float getBlockObstacleValue(IIntegerCoordinates pos) {
            Block block = Block.blocksList[pos.getBlockId()];
            if (block == null) return 0;
            if (block.isOpaqueCube()) return 1;
            if (block instanceof BlockWindGenerator) return 0;
            return 0.2f;
        }

        public void compute() {
            int toCheckLeft = blocksToCheckPerTick * slowingCoefficient;
            boolean firstIteration = true;
            boolean lastProcessedValuesWereSet = false;
            for (int rx = RANDOM_LOW_VALUE; rx <= totalRadius; rx++) {
                for (int ry = RANDOM_LOW_VALUE; ry <= totalRadius; ry++) {
                    for (int rz = RANDOM_LOW_VALUE; rz <= checkDistance; rz++) {
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
            if (!lastProcessedValuesWereSet) resetProcessedCoordinates();
        }

        private void processRelativeBlockAtCoordinate(int rx, int ry, int rz) {
            IIntegerCoordinates absCoords = computeAbsoluteCoordinates(rx, ry, rz);
        }

        private IIntegerCoordinates computeAbsoluteCoordinates(int rx, int ry, int rz) {
            return new IntegerCoordinates(TileWindGenerator.this).applyRelativeCoordinates(getRotation(), rx, ry, rz);
        }

    }

    public TileWindGenerator() {
        slowingCoefficient = 10;
    }

    protected void refreshTurbineItem() {
        ItemStack stack = getStackInSlot(TURBINE_SLOT);
        turbine = null;
        if (stack == null) {
            return;
        }
        Item item = stack.getItem();
        if (item instanceof ItemWindTurbine) {
            turbine = (ItemWindTurbine) item;
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public String getInvName() {
        return "jaffas.power.windGenerator";
    }

    @Override
    public String getMachineTitle() {
        return "Wind Generator";
    }

    @Override
    protected void doMachineWork() {
        if (worldObj.isRemote) return;


    }

    @Override
    protected void configurePowerParameters() {
        super.configurePowerParameters();
        powerNeeded = 0;
    }

    @Override
    public boolean isPowerBarRenderingEnabled() {
        return false;
    }
}
