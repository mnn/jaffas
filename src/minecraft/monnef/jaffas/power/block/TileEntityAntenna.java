package monnef.jaffas.power.block;

import monnef.jaffas.power.PowerConsumerManager;
import monnef.jaffas.power.PowerProviderManager;
import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.block.common.TileEntityMachine;

public class TileEntityAntenna extends TileEntityMachine implements IPowerConsumer, IPowerProvider {
    private IPowerConsumerManager consumerManager;
    private IPowerProviderManager providerManager;

    public TileEntityAntenna() {
        this.providerManager = new PowerProviderManager();
        providerManager.initialize(20, 40, this, true, new boolean[]{true, false, true, true, true, true});

        this.consumerManager = new PowerConsumerManager();
        consumerManager.initialize(20, 40, this);
    }

    @Override
    public String getMachineTitle() {
        return "Antenna";
    }

    @Override
    public IPowerConsumerManager getPowerConsumerManager() {
        return this.consumerManager;
    }

    @Override
    public IPowerProviderManager getPowerProviderManager() {
        return this.providerManager;
    }
}
