/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.api.IIntegerCoordinates;
import monnef.core.block.TileMachineWithInventory;
import monnef.core.common.ContainerRegistry;
import monnef.core.utils.IntegerCoordinates;
import monnef.core.utils.ItemHelper;
import monnef.core.utils.PlayerHelper;
import monnef.core.utils.RandomHelper;
import monnef.core.utils.WorldHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.power.common.IWindObstacles;
import monnef.jaffas.power.common.WindObstaclesFastFail;
import monnef.jaffas.power.entity.EntityWindTurbine;
import monnef.jaffas.power.item.ItemWindTurbine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

@ContainerRegistry.ContainerTag(slotsCount = 1, outputSlotsCount = 0, containerClassName = "monnef.core.block.ContainerMachine", guiClassName = "monnef.jaffas.power.client.GuiContainerWindGenerator")
public class TileWindGenerator extends TileMachineWithInventory {
    public static final int TURBINE_SLOT = 0;
    public static final int TICKS_PER_MINUTE = 60 * 20;
    public static final int DAMAGE_TIMER_BASE = TICKS_PER_MINUTE;
    public static final int DAMAGE_TIMER_SPREAD = DAMAGE_TIMER_BASE / 6;
    private static final String SPEED_TAG = "turbineSpeed";
    public static final int WORK_EVERY_N_TICKS = 10;
    private int currentMaximalSpeed;

    private enum TurbineState {UNKNOWN, NO_TURBINE, TURBINE_SPAWNED}

    private TurbineState turbineState = TurbineState.UNKNOWN;

    public static final int checkDistance = 15;
    public static final int checkOuterRadius = 2;
    public static int blocksToCheckPerTick = MonnefCorePlugin.debugEnv ? 100 : 5;

    private IWindObstacles obstacles = new WindObstaclesFastFail(this);

    private ItemWindTurbine turbine;
    private EntityWindTurbine turbineEntity;
    private ItemStack lastTurbineStack;
    private float turbineSpeed;
    private static final int TURBINE_NORMAL_SPEED = 70;
    public static final int TURBINE_MAX_SPEED = 100;
    private IIntegerCoordinates cachedTurbineHubPosition;
    private int speedChangeCoolDown;
    private int ticksToDamageTurbine;

    private int energyGeneratedCurrentTick = 0;

    public TileWindGenerator() {
        slowingCoefficient = WORK_EVERY_N_TICKS;
    }

    public void setCurrentMaximalSpeed(int currentMaximalSpeed) {
        this.currentMaximalSpeed = currentMaximalSpeed;
    }

    public int getCurrentMaximalSpeed() {
        return currentMaximalSpeed;
    }

    public int getTurbineSpeed() {
        return Math.round(turbineSpeed);
    }

    public void setTurbineSpeed(int turbineSpeed) {
        this.turbineSpeed = turbineSpeed;
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
        if (worldObj == null || worldObj.isRemote) return;
        turbineSpeed = 0;
        refreshTurbineEntity();
        obstacles.reset();
    }

    @Override
    protected void doMachineWork() {
        if (worldObj.isRemote) return;
        energyGeneratedCurrentTick = 0;
        setGeneratedPowerLastTick(0);

        if (turbineState == TurbineState.UNKNOWN) {
            refreshTurbineEntity();
        }
        if (turbineState == TurbineState.TURBINE_SPAWNED) {
            checkTurbine();
            if (turbineState == TurbineState.TURBINE_SPAWNED) {
                processObstacles();
                computeMaximalSpeed();
                adjustCurrentSpeed();
                updateSpeedOfTurbineEntity();
                producePower();
                damageIfTime();
            }
        }
    }

    private void producePower() {
        energyGeneratedCurrentTick = Math.round(turbine.getMaximalEnergyPerRainyTick() * (turbineSpeed / TURBINE_MAX_SPEED));
        setGeneratedPowerLastTick(energyGeneratedCurrentTick);
    }

    @Override
    public int getEnergyGeneratedThisTick() {
        return energyGeneratedCurrentTick;
    }

    @Override
    public ForgeDirection[] getValidCustomerDirections() {
        return CUSTOMER_DIRECTIONS_BACK_AND_BOTTOM;
    }

    private void damageIfTime() {
        ticksToDamageTurbine -= getCurrentMaximalSpeed() < 30 ? (rand.nextBoolean() ? 0 : slowingCoefficient) : slowingCoefficient;
        if (ticksToDamageTurbine <= 0) {
            damageTurbineItem(1);
            ticksToDamageTurbine = RandomHelper.generateRandomFromBaseAndSpread(DAMAGE_TIMER_BASE, DAMAGE_TIMER_SPREAD);
        }
    }

    private void updateSpeedOfTurbineEntity() {
        turbineEntity.updateStatus(getTurbineSpeed());
    }

    private void adjustCurrentSpeed() {
        if (speedChangeCoolDown-- <= 0) {
            int currentMaxSpeed = getCurrentMaximalSpeed();
            boolean rain = worldObj.isRaining();
            if (currentMaxSpeed > TURBINE_NORMAL_SPEED && !rain) currentMaxSpeed = TURBINE_NORMAL_SPEED;

            float step = turbine.getNormalStepSize();
            step *= rain ? (worldObj.isThundering() ? turbine.getStormStepCoef() : turbine.getRainStepCoef()) : 1;
            if (turbineSpeed < currentMaxSpeed) turbineSpeed += step;
            else if (turbineSpeed > currentMaxSpeed) turbineSpeed -= step;
            if (rain || turbine.getRandomChangeChance() > rand.nextFloat())
                turbineSpeed += step * (rand.nextBoolean() ? 1 : -1);

            if (turbineSpeed < 0) turbineSpeed = 0;
            if (turbineSpeed > TURBINE_MAX_SPEED) turbineSpeed = TURBINE_MAX_SPEED;
            speedChangeCoolDown = RandomHelper.generateRandomFromInterval(
                    rain ? turbine.getSpeedChangeInRainCoolDownMin() : turbine.getSpeedChangeCoolDownMin(),
                    rain ? turbine.getSpeedChangeInRainCoolDownMax() : turbine.getSpeedChangeCoolDownMax());
        }
    }

    private void computeMaximalSpeed() {
        float obstaclesVolume = obstacles.getObstaclesVolumeWorstScenario();
        int totalDiamSquare = obstacles.getTotalDiameter() * obstacles.getTotalDiameter();
        float obstaclesToleratedWithoutLoss = totalDiamSquare / 2f;
        float obstaclesThresholdForNoTurbineMovement = totalDiamSquare * 3f;
        if (obstaclesVolume > obstaclesThresholdForNoTurbineMovement) setCurrentMaximalSpeed(0);
        else if (obstaclesVolume < obstaclesToleratedWithoutLoss) setCurrentMaximalSpeed(TURBINE_MAX_SPEED);
        else {
            float coef = (obstaclesVolume - obstaclesToleratedWithoutLoss) * 1 / (obstaclesThresholdForNoTurbineMovement - obstaclesToleratedWithoutLoss);
            float newSpeed = TURBINE_MAX_SPEED * (1 - coef);
            setCurrentMaximalSpeed(Math.round(newSpeed));
        }
    }

    private void checkTurbine() {
        if (turbineEntity == null || turbineEntity.isDead) {
            invalidateTurbineEntityRecord();
            return;
        }

        IIntegerCoordinates pos = getTurbineHubPositionInternal();
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
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

    public void killAllTurbinesInFront() {
        IIntegerCoordinates pos = getTurbineHubPositionInternal();
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        List turbs = worldObj.getEntitiesWithinAABB(EntityWindTurbine.class, box);
        for (Object turb : turbs) {
            ((EntityWindTurbine) turb).setDead();
        }
        turbineEntity = null;
        resetTurbineSpeed();
    }

    private void resetTurbineSpeed() {
        turbineSpeed = 0;
        currentMaximalSpeed = 0;
        ticksToDamageTurbine = 1;
    }

    private void processObstacles() {
        obstacles.compute();
        if (MonnefCorePlugin.debugEnv) {
            float dc = obstacles.debugCompute();
            if (obstacles.getObstaclesVolumeCached() != dc) {
                JaffasFood.Log.printDebug(String.format("obstacles volume doesn't match - cached=%f != direct=%f", obstacles.getObstaclesVolumeCached(), dc));
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
    public void markDirty() {
        super.markDirty();
        refreshTurbineItem();
    }

    @Override
    public String getInventoryName() {
        return "jaffas.power.windGenerator";
    }

    @Override
    public String getMachineTitle() {
        return "Wind Generator";
    }

    @Override
    protected void configurePowerParameters() {
        super.configurePowerParameters();
        configureAsPowerSource();
    }

    @Override
    public int getIntegersToSyncCount() {
        return super.getIntegersToSyncCount() + 1;
    }

    @Override
    public int getCurrentValueOfIntegerToSync(int index) {
        int ret = super.getCurrentValueOfIntegerToSync(index);
        switch (index - super.getIntegersToSyncCount()) {
            case 0:
                ret = getTurbineSpeed();
                break;
        }

        return ret;
    }

    @Override
    public void setCurrentValueOfIntegerToSync(int index, int value) {
        super.setCurrentValueOfIntegerToSync(index, value);
        switch (index - super.getIntegersToSyncCount()) {
            case 0:
                turbineSpeed = value;
                break;
        }
    }

    public void onEntityTurbineHit(DamageSource damageSource, float amount, EntityWindTurbine entity) {
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
        damageTurbineItem(Math.round(amount * 10));
    }

    private void damageTurbineItem(int amount) {
        if (ItemHelper.damageItem(getStackInSlot(TURBINE_SLOT), amount)) {
            setInventorySlotContents(TURBINE_SLOT, null);
            if (turbineEntity != null) {
                turbineEntity.breakTurbine();
            }
        }
    }

    public void onEntityTurbineDeath(EntityWindTurbine entity) {
        resetTurbineSpeed();
        setInventorySlotContents(TURBINE_SLOT, null);
        refreshTurbineItem();
        refreshTurbineEntity();
        WorldHelper.createExplosion(worldObj, entity, entity.posX, entity.posY, entity.posZ, 0.1f, false, true);
    }

    public ItemWindTurbine getTurbine() {
        return turbine;
    }

    public int getSlowingCoefficient() {
        return slowingCoefficient;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        setTurbineSpeed(tag.getInteger(SPEED_TAG));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger(SPEED_TAG, getTurbineSpeed());
    }

    @Override
    public void onItemDebug(EntityPlayer player) {
        if (worldObj.isRemote) return;
        PlayerHelper.addMessage(player, String.format("ObsVolumeCached: %.2f, ObsDebug: %.2f, cMaxSpeed: %d, totRad: %d", obstacles.getObstaclesVolumeWorstScenario(), obstacles.debugCompute(), getCurrentMaximalSpeed(), obstacles.getTotalRadius()));
    }
}
