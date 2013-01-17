package monnef.jaffas.power.item;

import monnef.jaffas.power.api.IMachineTool;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.block.machine.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;

public class ItemDebug extends ItemPower implements IMachineTool {
    public ItemDebug(int id, int textureIndex) {
        super(id, textureIndex);
    }

    public boolean onMachineClick(TileEntityMachine machine, EntityPlayer player) {
        if (machine instanceof IPowerProvider) {
            IPowerProviderManager provider = ((IPowerProvider) machine).getPowerManager();
            printTECoors(player,machine);
            printEnergyInfo(player,true, provider.getCurrentBufferedEnergy(), provider.getBufferSize(), provider.getMaximalPacketSize());
            //printConnectionInfo(player)
        }

        return true;
    }

    private void printTECoors(EntityPlayer player, TileEntityMachine machine) {
        throw new RuntimeException("Not implemented yet.");
    }

    private void printEnergyInfo(EntityPlayer player, boolean provider, int buffer, int maxBuff, int packetSize) {
        throw new RuntimeException("Not implemented yet.");
    }
}
