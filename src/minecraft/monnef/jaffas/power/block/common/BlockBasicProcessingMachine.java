package monnef.jaffas.power.block.common;

import monnef.jaffas.power.client.GuiHandler;
import monnef.jaffas.technic.JaffasTechnic;

public abstract class BlockBasicProcessingMachine extends BlockMachineWithInventory {
    public BlockBasicProcessingMachine(int id, int index) {
        super(id, index, JaffasTechnic.breakableIronMaterial, false);
    }

    @Override
    public boolean supportRotation() {
        return true;
    }
}
