/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import com.google.common.collect.Table;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;

public class TileWindGenerator extends TileEntityMachineWithInventory {
    private enum TurbineState {UNKNOWN, NO_TURBINE, TURBINE_SPAWNED}

    private enum CustomerState {NONE, BACK, BOTTOM}

    private TurbineState turbineState = TurbineState.UNKNOWN;
    private CustomerState customerState = CustomerState.NONE;

    public static float maxEnergyPerTick = 5;
    private static final float lossPerSolidBlock = 0.05f;
    private static final int checkDistance = 15;
    private static final int checkOuterRadius = 2;
    public static int blocksToCheckPerTick = 5;

    private Table<Integer, Integer, Float> obstacles;

    public TileWindGenerator() {
        slowingCoefficient = 10;
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
