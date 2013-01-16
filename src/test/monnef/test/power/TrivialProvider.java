package monnef.test.power;

import monnef.jaffas.power.PowerProviderManager;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.IPowerProviderManager;
import net.minecraft.tileentity.TileEntity;

public class TrivialProvider extends TileEntity implements IPowerProvider {
    private IPowerProviderManager manager;

    public TrivialProvider() {
        super();

        manager = new PowerProviderManager();
        manager.initialize(20, 100, this, true, (byte) 0);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        manager.tick();
    }

    @Override
    public IPowerProviderManager getPowerManager() {
        return manager;
    }

    public int generatePower(int energy) {
        return manager.storeEnergy(energy);
    }
}
