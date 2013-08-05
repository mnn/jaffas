/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.api.IIntegerCoordinates;
import monnef.core.collection.SpaceHashMap;
import monnef.core.utils.IntegerCoordinates;
import monnef.core.utils.ItemHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import monnef.jaffas.power.entity.EntityWindTurbine;
import monnef.jaffas.power.item.ItemWindTurbine;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;

import java.util.List;

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
    public static int blocksToCheckPerTick = MonnefCorePlugin.debugEnv ? Short.MAX_VALUE : 5;
    public static float rainPowerBonusMax = 1.2f; // max +120%

    private Obstacles obstacles = new Obstacles();

    private ItemWindTurbine turbine;
    private EntityWindTurbine turbineEntity;
    private ItemStack lastTurbineStack;
    private int turbineSpeed;
    private static final int TURBINE_NORMAL_SPEED = 70;
    private static final int TURBINE_MAX_SPEED = 100;
    private IIntegerCoordinates cachedTurbineHubPosition;
    private int lastPowerProduction;

    private class Obstacles {
        public static final int RANDOM_LOW_VALUE = -100;
        private SpaceHashMap<Integer, Float> obstacles;
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
            resetVolumeCached();
            int totalDiameter = totalRadius * 2 + 1;
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

        private int getStartingValueOfRelativeZ() {
            return turbine != null && turbine.doesCheckBack() ? -checkDistance : 1;
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
            int rxStart = -totalRadius;
            int ryStart = -totalRadius;
            int rzStart = getStartingValueOfRelativeZ();
            for (int rx = rxStart; rx <= totalRadius; rx++) {
                for (int ry = ryStart; ry <= totalRadius; ry++) {
                    for (int rz = rzStart; rz <= checkDistance; rz++) {
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
        public float debugCompute() {
            float res = 0;
            int startingValueOfRelativeZ = getStartingValueOfRelativeZ();
            for (int rx = -totalRadius; rx <= totalRadius; rx++) {
                for (int ry = -totalRadius; ry <= totalRadius; ry++) {
                    for (int rz = startingValueOfRelativeZ; rz <= checkDistance; rz++) {
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
            return new IntegerCoordinates(TileWindGenerator.this).applyRelativeCoordinates(getRotation(), rx, ry, rz);
        }
    }

    public TileWindGenerator() {
        slowingCoefficient = 10;
    }

    protected void refreshTurbineItem() {
        ItemStack stack = getStackInSlot(TURBINE_SLOT);
        turbine = null;
        if (stack != null) {
            Item item = stack.getItem();
            if (item instanceof ItemWindTurbine) {
                turbine = (ItemWindTurbine) item;
            }
        }

        if (!ItemHelper.haveStacksSameIdAndDamage(lastTurbineStack, stack)) onTurbineChanged();
        lastTurbineStack = stack;
    }

    private void onTurbineChanged() {
        turbineSpeed = 0;
        refreshTurbineEntity();
    }

    @Override
    protected void doMachineWork() {
        if (worldObj.isRemote) return;

        if (turbineState == TurbineState.UNKNOWN) {
            refreshTurbineEntity();
        }
        if (turbineState == TurbineState.TURBINE_SPAWNED) {
            checkTurbine();
            if (turbineState == TurbineState.TURBINE_SPAWNED) {
                processObstacles();

            }
        }
    }

    private void checkTurbine() {
        if (turbineEntity == null || turbineEntity.isDead) {
            invalidateTurbineEntityRecord();
            return;
        }

        IIntegerCoordinates pos = getTurbineHubPositionInternal();
        AxisAlignedBB box = AxisAlignedBB.getAABBPool().getAABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        List turbs = worldObj.getEntitiesWithinAABB(EntityWindTurbine.class, box);
        if (turbs.size() > 1) {
            killAllTurbinesInFront();
            turbineState = TurbineState.NO_TURBINE;
            return;
        }
        if (turbs.size() < 1) {
            invalidateTurbineEntityRecord();
        }
        if (turbineEntity != turbs.get(0)) {
            killAllTurbinesInFront();
            invalidateTurbineEntityRecord();
        }
    }

    private void invalidateTurbineEntityRecord() {
        turbineState = TurbineState.NO_TURBINE;
        turbineEntity = null;
    }

    private void refreshTurbineEntity() {
        if (worldObj == null || worldObj.isRemote) return;

        if (turbineState == TurbineState.TURBINE_SPAWNED) {
            killAllTurbinesInFront();
        }

        if (turbine == null) {
            turbineState = TurbineState.NO_TURBINE;
        } else {
            if (worldObj != null) {
                turbineEntity = new EntityWindTurbine(worldObj);
                IIntegerCoordinates pos = getTurbineHubPositionInternal();
                turbineEntity.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
                worldObj.spawnEntityInWorld(turbineEntity);
                turbineEntity.configure(getRotation(), getStackInSlot(TURBINE_SLOT), this);
                turbineState = TurbineState.TURBINE_SPAWNED;
            }
        }
    }

    private void killAllTurbinesInFront() {
        IIntegerCoordinates pos = getTurbineHubPositionInternal();
        AxisAlignedBB box = AxisAlignedBB.getAABBPool().getAABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        List turbs = worldObj.getEntitiesWithinAABB(EntityWindTurbine.class, box);
        for (Object turb : turbs) {
            ((EntityWindTurbine) turb).setDead();
        }
        turbineEntity = null;
    }

    private void processObstacles() {
        obstacles.compute();
        if (MonnefCorePlugin.debugEnv) {
            float dc = obstacles.debugCompute();
            if (obstacles.obstaclesVolumeCached != dc) {
                JaffasFood.Log.printDebug(String.format("obstacles volume doesn't match - cached=%f != direct=%f", obstacles.obstaclesVolumeCached, dc));
            }
        }
    }

    protected IIntegerCoordinates getTurbineHubPositionInternal() {
        if (cachedTurbineHubPosition == null) {
            IIntegerCoordinates pos = new IntegerCoordinates(this);
            cachedTurbineHubPosition = pos.shiftInDirectionBy(getRotation(), 1);
        }
        return cachedTurbineHubPosition;
    }

    public IIntegerCoordinates getTurbineHubPosition() {
        return getTurbineHubPositionInternal().copy();
    }

    @Override
    public void onInventoryChanged() {
        super.onInventoryChanged();
        refreshTurbineItem();
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
    protected void configurePowerParameters() {
        super.configurePowerParameters();
        powerNeeded = 0;
    }

    @Override
    public boolean isPowerBarRenderingEnabled() {
        return false;
    }

    @Override
    public int getIntegersToSyncCount() {
        return 2;
    }

    @Override
    public int getCurrentValueOfIntegerToSync(int index) {
        switch (index) {
            case 0:
                return turbineSpeed;

            case 1:
                return lastPowerProduction;
        }

        return -1;
    }

    @Override
    public void setCurrentValueOfIntegerToSync(int index, int value) {
        switch (index) {
            case 0:
                turbineSpeed = value;
                break;

            case 1:
                lastPowerProduction = value;
                break;
        }
    }

    public void onEntityTurbineHit(DamageSource damageSource, int amount, EntityWindTurbine entity) {
        ItemStack stack = getStackInSlot(TURBINE_SLOT);
        if ((stack == null) != (turbine == null)) {
            JaffasFood.Log.printDebug("WindGen: inv and saved turbine are not correct, fixing");
            refreshTurbineItem();
        }
        if (turbine == null) {
            entity.setDead();
            return;
        }
        if (turbineEntity != entity) {
            JaffasFood.Log.printDebug("WindGen: getting message from not my turbine, die!");
            entity.setDead();
            return;
        }
        if (ItemHelper.damageItem(stack, amount * 10)) {
            setInventorySlotContents(TURBINE_SLOT, null);
            entity.setDead();
        }
    }
}
