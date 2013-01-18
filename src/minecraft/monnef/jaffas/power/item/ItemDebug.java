package monnef.jaffas.power.item;

import monnef.jaffas.power.api.IMachineTool;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.block.machine.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;

public class ItemDebug extends ItemPower implements IMachineTool {
    private EntityPlayer player;

    public ItemDebug(int id, int textureIndex) {
        super(id, textureIndex);
    }

    public boolean onMachineClick(TileEntityMachine machine, EntityPlayer player, int side) {
        if (machine instanceof IPowerProvider) {
            IPowerProviderManager provider = ((IPowerProvider) machine).getPowerManager();
            this.player = player;
            print(getTECoors(machine));
            print(getEnergyInfo(true, provider.getCurrentBufferedEnergy(), provider.getBufferSize(), provider.getMaximalPacketSize()));
            print(getConnectionInfo(provider.constructConnectedSides()));
        }

        return true;
    }

    private String getConnectionInfo(boolean[] sides) {
        StringBuilder s = new StringBuilder();
        for (boolean side : sides) {

        }

        return s.toString();
    }

    private String getTECoors(TileEntityMachine machine) {
        throw new RuntimeException("Not implemented yet.");
    }

    private String getEnergyInfo(boolean provider, int buffer, int maxBuff, int packetSize) {
        throw new RuntimeException("Not implemented yet.");
    }

    private void print(String message) {
        player.addChatMessage(message);
    }
}
