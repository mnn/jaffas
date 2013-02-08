package monnef.jaffas.power;

import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerNodeCoordinates;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.JaffasPowerException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class PowerConsumerManager implements IPowerConsumerManager {
    private static final String energyBufferTagName = "energyBuffer";

    private int maximalPacketSize = 20;
    private int bufferSize = 40;
    private int energyBuffer = 0;
    private IPowerNodeCoordinates provider;
    private TileEntity myTile;
    private boolean initialized = false;
    private ForgeDirection sideOfProvider;
    private IPowerNodeCoordinates myCoordinates;

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile) {
        if (initialized) {
            throw new JaffasPowerException("Already initialized.");
        }

        initialized = true;
        setMaximalPacketSize(maximalPacketSize);
        setBufferSize(bufferSize);
        setTile(tile);
        sideOfProvider = null;
        myCoordinates = new PowerNodeCoordinates(tile);
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
    public int storeEnergy(int energy) {
        int toStore = energy;
        if (bufferSize < energyBuffer + toStore) {
            toStore = bufferSize - energyBuffer;
        }

        addEnergyToBuffer(toStore);
        return toStore;
    }

    @Override
    public void connect(IPowerNodeCoordinates provider) {
        if (this.provider != null) {
            throw new JaffasPowerException("Connecting already connected consumer.");
        }

        setProvider(provider, ForgeDirection.UNKNOWN);
    }

    @Override
    public void connectDirect(IPowerNodeCoordinates provider, ForgeDirection side) {
        if (this.provider != null) {
            throw new JaffasPowerException("Connecting already connected consumer.");
        }

        setProvider(provider, side);
    }

    private void setProvider(IPowerNodeCoordinates provider, ForgeDirection side) {
        this.provider = provider;
        this.sideOfProvider = side;
    }

    @Override
    public void disconnect() {
        provider = null;
    }

    @Override
    public boolean isRemotelyConnected() {
        return provider != null;
    }

    @Override
    public boolean isConnectedToSide(ForgeDirection side) {
        if (provider == null) return false;
        if (sideOfProvider == ForgeDirection.UNKNOWN) return false;
        return side == sideOfProvider;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean energyNeeded() {
        return getCurrentMaximalPacketSize() > 0;
    }

    @Override
    public void tick() {
        if (energyNeeded()) {
            int energy = provider.asProvider().getPowerProviderManager().requestEnergy(getCurrentMaximalPacketSize(), getCoordinates());
            storeEnergy(energy);
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
    public IPowerNodeCoordinates getCoordinates() {
        return this.myCoordinates;
    }

    @Override
    public int getCurrentBufferedEnergy() {
        return this.energyBuffer;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger(energyBufferTagName, energyBuffer);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        int storedEnergy = tagCompound.getInteger(energyBufferTagName);
        setEnergyBuffer(storedEnergy);
    }

    @Override
    public IPowerProvider getProvider() {
        return provider == null ? null : provider.asProvider();
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
