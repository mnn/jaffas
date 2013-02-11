package monnef.jaffas.power;

import monnef.jaffas.power.api.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class PowerConsumerManager extends PowerNodeManager implements IPowerConsumerManager {
    private IPowerNodeCoordinates provider;
    private ForgeDirection sideOfProvider;

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile) {
        if (initialized) {
            throw new JaffasPowerException("already initialized");
        }
        super.initialize(maximalPacketSize, bufferSize, tile);
        sideOfProvider = null;
        initialized = true;
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
    public IPowerProvider getProvider() {
        return provider == null ? null : provider.asProvider();
    }

    private void removeEnergyFromBuffer(int consumed) {
        addEnergyToBuffer(-consumed);
    }
}
