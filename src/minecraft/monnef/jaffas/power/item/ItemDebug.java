package monnef.jaffas.power.item;

import monnef.jaffas.power.api.*;
import monnef.jaffas.power.block.common.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;

import static monnef.core.utils.TileEntityHelper.getFormattedCoordinates;
import static monnef.jaffas.power.utils.StringPowerFormatter.getConnectionInfo;
import static monnef.jaffas.power.utils.StringPowerFormatter.formatEnergyInfo;

public class ItemDebug extends ItemPower implements IMachineTool {
    private EntityPlayer player;

    public ItemDebug(int id, int textureIndex) {
        super(id, textureIndex);
        setUnlocalizedName("debugPower");
    }

    public boolean onMachineClick(TileEntityMachine machine, EntityPlayer player, int side) {
        this.player = player;

        if (machine == null) {
            print("TE is null");
        }

        //TODO machine can be both - provider & consumer
        if (machine instanceof IPowerProvider) {
            IPowerProviderManager provider = ((IPowerProvider) machine).getPowerProviderManager();
            print(getFormattedCoordinates(machine) + ": " + formatEnergyInfo(true, false, provider.getCurrentBufferedEnergy(), provider.getBufferSize(), provider.getMaximalPacketSize()));
            print(getConnectionInfo(provider, true));
        }

        if (machine instanceof IPowerConsumer) {
            IPowerConsumerManager consumer = ((IPowerConsumer) machine).getPowerConsumerManager();
            print(getFormattedCoordinates(machine) + ": " + formatEnergyInfo(false, true, consumer.getCurrentBufferedEnergy(), consumer.getBufferSize(), consumer.getMaximalPacketSize()));
            print(getConnectionInfo(consumer, true));
        }

        if (machine.getMachineBlock().supportRotation()) {
            print("dir: " + machine.getRotation().toString());
        }

        return true;
    }

    @Override
    public boolean renderPowerLabels() {
        return true;
    }

    private void print(String message) {
        player.addChatMessage(message);
    }
}
