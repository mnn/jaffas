package monnef.jaffas.power;

import com.google.common.collect.HashBiMap;
import monnef.core.BitHelper;
import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.api.JaffasPowerException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.HashMap;

public class PowerProviderManager implements IPowerProviderManager {
    private int maximalPacketSize;
    private int bufferSize;
    private TileEntity tile;
    private boolean remoteConnection;
    private byte directSidesMask;

    private HashBiMap<ForgeDirection, IPowerConsumerManager> consumers;
    private int energyBuffer;

    private boolean firstTick = true;

    private boolean initialized = false;

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile, boolean remoteConnection, byte directSidesMask) {
        if (initialized) {
            throw new JaffasPowerException("already initialized");
        }
        initialized = true;

        this.maximalPacketSize = maximalPacketSize;
        this.bufferSize = bufferSize;
        this.tile = tile;
        this.remoteConnection = remoteConnection;
        this.directSidesMask = directSidesMask;
        this.consumers = HashBiMap.create(new HashMap<ForgeDirection, IPowerConsumerManager>());

        this.energyBuffer = 0;
    }

    @Override
    public int getFreeSpaceInBuffer() {
        return getBufferSize() - energyBuffer;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public int getMaximalPacketSize() {
        return maximalPacketSize;
    }

    @Override
    public TileEntity getTile() {
        return tile;
    }

    @Override
    public int requestEnergy(int amount) {
        //TODO
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int maximalPowerPerPacket() {
        return maximalPacketSize;
    }

    @Override
    public int storeEnergy(int amount) {
        int toStore = amount;
        if (energyBuffer + toStore > bufferSize) {
            toStore = bufferSize - energyBuffer;
        }

        setEnergyBuffer(energyBuffer + toStore);
        return toStore;
    }

    @Override
    public boolean supportRemoteConnection() {
        return remoteConnection;
    }

    @Override
    public boolean supportDirectConnection() {
        return directSidesMask != 0;
    }

    @Override
    public boolean sideProvidesPower(ForgeDirection side) {
        return BitHelper.isBitSet(directSidesMask, side.ordinal());
    }

    @Override
    public boolean hasFreeSlotForRemotePower() {
        if (!supportRemoteConnection()) {
            return false;
        } else {
            return !isRemotelyConnected();
        }
    }

    @Override
    public boolean connect(IPowerConsumer output) {
        if (!supportRemoteConnection()) {
            throw new JaffasPowerException("provider doesn't support remote connections");
        }

        if (isRemotelyConnected()) {
            throw new JaffasPowerException("provider already connected");
        }

        SetConsumer(ForgeDirection.UNKNOWN, output);
        return true;
    }

    private void SetConsumer(ForgeDirection side, IPowerConsumer consumer) {
        consumers.put(side, consumer.getPowerManager());
    }

    @Override
    public boolean connectDirect(IPowerConsumer output, ForgeDirection side) {
        if (!supportDirectConnection()) {
            throw new JaffasPowerException("provider doesn't support direct connections");
        }

        if (isConnectedToSide(side)) {
            throw new JaffasPowerException("provider already connected");
        }

        SetConsumer(side, output);
        return true;
    }

    @Override
    public boolean disconnect(IPowerConsumer consumer) {
        if (!consumers.containsValue(consumer)) {
            throw new JaffasPowerException("consumer is not connected, cannot disconnect");
        }

        consumers.remove(consumers.inverse().get(consumer));
        return true;
    }

    @Override
    public void saveConsumersToNBT(NBTTagCompound tagCompound) {
        // TODO
        return;
    }

    @Override
    public void loadConsumersFromNBT(NBTTagCompound tagCompound) {
        // TODO
        return;
    }

    @Override
    public void tick() {
        if (firstTick) {
            firstTick = false;

            //TODO make connections
        }
    }

    @Override
    public boolean isRemotelyConnected() {
        return isConnectedToSide(ForgeDirection.UNKNOWN, false);
    }

    @Override
    public boolean isConnectedToSide(ForgeDirection side) {
        return isConnectedToSide(side, true);
    }

    private boolean isConnectedToSide(ForgeDirection side, boolean doCheck) {
        if (doCheck && side == ForgeDirection.UNKNOWN) {
            throw new JaffasPowerException("wrong side");
        }

        return consumers.containsKey(side);
    }

    private void setEnergyBuffer(int energyBuffer) {
        this.energyBuffer = energyBuffer;
    }

    @Override
    public int getCurrentBufferedEnergy() {
        return energyBuffer;
    }
}
