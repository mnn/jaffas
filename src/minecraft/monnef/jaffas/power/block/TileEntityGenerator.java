package monnef.jaffas.power.block;

import monnef.jaffas.power.PowerProviderManager;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityGenerator extends TileEntityMachineWithInventory implements IPowerProvider {
    private final PowerProviderManager manager;

    public TileEntityGenerator() {
        super();

        manager = new PowerProviderManager();
    }

    @Override
    public IPowerProviderManager getPowerProviderManager() {
        return manager;
    }

    @Override
    public String getMachineTitle() {
        return "Generator";
    }

    @Override
    protected void onTick(int number) {
        super.onTick(number);
        if (number == 1) {
            boolean[] sidesMask = new boolean[6];
            sidesMask[ForgeDirection.UP.ordinal()] = true;
            manager.initialize(20, 500, this, false, sidesMask);
        }
    }

    public int getSizeInventory() {
        return 1;
    }

    @Override
    public String getInvName() {
        return "jaffas.power.generator";
    }
}
