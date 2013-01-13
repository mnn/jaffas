package monnef.jaffas.power;

import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.JaffasPowerException;
import net.minecraft.tileentity.TileEntity;

public class PowerConsumerManager implements IPowerConsumerManager {
    private int maximalPacketSize = 20;
    private int bufferSize = 40;
    private int energyBuffer = 0;
    private IPowerProvider provider;
    private TileEntity myTile;
    private boolean initialized = false;

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile) {
        if (initialized) {
            throw new JaffasPowerException("Already initialized.");
        }

        initialized = true;
        setMaximalPacketSize(maximalPacketSize);
        setBufferSize(bufferSize);
        setTile(tile);
    }

    @Override
    public int getMaximalPacketSize() {
        return maximalPacketSize;
    }

    private void setMaximalPacketSize(int size) {
        if (size <= 0) {
            throw new JaffasPowerException("Wrong size of packet.");
        }
        maximalPacketSize = size;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    private void setBufferSize(int size) {
        if (size <= 0) {
            throw new JaffasPowerException("Wrong size of buffer.");
        }
        bufferSize = size;
    }

    @Override
    public int getCurrentMaximalPacketSize() {
        int max = maximalPacketSize;
        int space = getFreeSpaceInBuffer();
        if (max > space) {
            max = space;
        }

        return max;
    }

    @Override
    public boolean hasBuffered(int energy) {
        return getCurrentBufferedEnergy() >= energy;
    }

    @Override
    public int consume(int energy) {
        int consumed = energy;
        if (consumed > energyBuffer) {
            consumed = energyBuffer;
        }

        removeEnergyFromBuffer(consumed);
        return consumed;
    }

    @Override
    public int store(int energy) {
        int toStore = energy;
        if (bufferSize < energyBuffer + toStore) {
            toStore = bufferSize - energyBuffer;
        }

        addEnergyToBuffer(toStore);
        return toStore;
    }

    @Override
    public void connect(IPowerProvider provider) {
        if (this.provider != null) {
            throw new JaffasPowerException("Connecting already connected consumer.");
        }

        this.provider = provider;
    }

    @Override
    public void disconnect() {
        if (provider == null) return;
        provider.disconnect();
        provider = null;
    }

    @Override
    public boolean isConnected() {
        return provider != null;
    }

    @Override
    public boolean energyNeeded() {
        return getCurrentMaximalPacketSize() > 0;
    }

    @Override
    public void tick() {
        if (energyNeeded()) {
            int energy = provider.requestEnergy(getCurrentMaximalPacketSize());
            store(energy);
        }
    }

    @Override
    public int getFreeSpaceInBuffer() {
        return bufferSize - energyBuffer;
    }

    private void setTile(TileEntity te) {
        myTile = te;
    }

    @Override
    public TileEntity getTile() {
        return myTile;
    }

    @Override
    public int getCurrentBufferedEnergy() {
        return this.energyBuffer;
    }

    private void setEnergyBuffer(int energyBuffer) {
        this.energyBuffer = energyBuffer;
    }

    private void addEnergyToBuffer(int toStore) {
        setEnergyBuffer(energyBuffer + toStore);
    }

    private void removeEnergyFromBuffer(int consumed) {
        addEnergyToBuffer(-consumed);
    }
}
