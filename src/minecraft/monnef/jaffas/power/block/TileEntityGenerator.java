package monnef.jaffas.power.block;

import monnef.jaffas.power.PowerProviderManager;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.block.common.TileEntityMachine;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityGenerator extends TileEntityMachine implements IPowerProvider {
    private final PowerProviderManager manager;

    public TileEntityGenerator() {
        super();

        manager = new PowerProviderManager();
        boolean[] sidesMask = new boolean[6];
        sidesMask[ForgeDirection.UP.ordinal()] = true;
        manager.initialize(20, 500, this, false, sidesMask);
    }

    @Override
    public IPowerProviderManager getPowerProviderManager() {
        return manager;
    }

    @Override
    public String getMachineTitle() {
        return "Generator";
    }
}
