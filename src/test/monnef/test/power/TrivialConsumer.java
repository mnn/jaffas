package monnef.test.power;

import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerConsumerManager;
import net.minecraft.tileentity.TileEntity;

public class TrivialConsumer extends TileEntity implements IPowerConsumer {
    private IPowerConsumerManager manager;

    @Override
    public IPowerConsumerManager getPowerConsumerManager() {
        return manager;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        manager.tick();
    }
}
