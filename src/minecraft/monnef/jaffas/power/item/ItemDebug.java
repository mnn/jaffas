package monnef.jaffas.power.item;

import com.google.common.base.Joiner;
import monnef.jaffas.power.api.*;
import monnef.jaffas.power.block.common.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class ItemDebug extends ItemPower implements IMachineTool {
    private EntityPlayer player;

    public ItemDebug(int id, int textureIndex) {
        super(id, textureIndex);
        setItemName("debugPower");
    }

    public boolean onMachineClick(TileEntityMachine machine, EntityPlayer player, int side) {
        this.player = player;

        if (machine == null) {
            print("TE is null");
        }

        if (machine instanceof IPowerProvider) {
            IPowerProviderManager provider = ((IPowerProvider) machine).getPowerManager();
            print(getTECoors(machine) + ": " + getEnergyInfo(true, provider.getCurrentBufferedEnergy(), provider.getBufferSize(), provider.getMaximalPacketSize()));
            print(getConnectionInfo(provider));
        }

        if (machine instanceof IPowerConsumer) {
            IPowerConsumerManager consumer = ((IPowerConsumer) machine).getPowerManager();
            print(getTECoors(machine) + ": " + getEnergyInfo(true, consumer.getCurrentBufferedEnergy(), consumer.getBufferSize(), consumer.getMaximalPacketSize()));
            print(getConnectionInfo(consumer));
        }

        return true;
    }

    private String getConnectionInfo(IPowerConsumerManager consumer) {
        StringBuilder s = new StringBuilder("Cs: ");
        IPowerProvider provider = consumer.getProvider();
        if (provider != null) {
            s.append(getTECoors(provider.getPowerManager().getTile()));
        } else {
            s.append("-");
        }

        return s.toString();
    }

    private String getConnectionInfo(IPowerProviderManager provider) {
        List<String> list = new ArrayList<String>();
        boolean[] sides = provider.constructConnectedSides();
        for (int i = 0; i < sides.length; i++) {
            StringBuilder s = new StringBuilder();
            if (sides[i]) {
                s.append(getTECoors((TileEntity) provider.getConsumer(ForgeDirection.getOrientation(i))));
            } else {
                s.append("-");
            }
            list.add(s.toString());
        }

        return "Cs: " + Joiner.on("|").join(list);
    }

    private String getTECoors(TileEntity machine) {
        StringBuilder s = new StringBuilder();
        s.append(machine.xCoord);
        s.append("x");
        s.append(machine.yCoord);
        s.append("x");
        s.append(machine.zCoord);
        return s.toString();
    }

    private String getEnergyInfo(boolean provider, int buffer, int maxBuff, int packetSize) {
        StringBuilder s = new StringBuilder();
        s.append(provider ? "P>" : ">C");
        s.append(":");
        s.append(buffer);
        s.append("/");
        s.append(maxBuff);
        s.append("(");
        s.append(packetSize);
        s.append(")");
        return s.toString();
    }

    private void print(String message) {
        player.addChatMessage(message);
    }
}
