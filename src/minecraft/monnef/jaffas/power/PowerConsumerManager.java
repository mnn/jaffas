package monnef.jaffas.power;

import monnef.jaffas.power.api.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class PowerConsumerManager extends PowerNodeManager implements IPowerConsumerManager {
    private static final String PROVIDER_TAG_NAME = "provider";
    private static final String PROVIDER_SIDE_TAG_NAME = "providerSide";
    private IPowerNodeCoordinates provider;
    private ForgeDirection sideOfProvider;

    private IPowerNodeCoordinates plannedProvider;
    private ForgeDirection plannedSideOfProvider;

    private boolean firstTick = true;

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
            //throw new JaffasPowerException("Connecting already connected consumer.");
            System.err.println("Connecting already connected consumer.");
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
    public void doWork() {
        if (energyNeeded() && provider != null) {
            int energy = provider.asProvider().getPowerProviderManager().requestEnergy(getCurrentMaximalPacketSize(), getCoordinates());
            storeEnergy(energy);
        }
    }

    @Override
    public boolean energyNeeded() {
        return getCurrentMaximalPacketSize() > 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (!initialized) return;

        if (firstTick) {
            if (plannedProvider != null) plannedProvider.setWorld(myTile.worldObj);
            makePlannedConnection(false);
            firstTick = false;
        }
    }

    private void makePlannedConnection(boolean canUpdate) {
        PowerUtils.connect(plannedProvider, myCoordinates, plannedSideOfProvider);

        plannedProvider = null;
        plannedSideOfProvider = null;
        if (canUpdate) sendUpdate();
    }

    @Override
    public IPowerProvider getProvider() {
        return provider == null ? null : provider.asProvider();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (provider != null) {
            provider.saveTo(tag, PROVIDER_TAG_NAME);
        }
        if (sideOfProvider != null) {
            tag.setByte(PROVIDER_SIDE_TAG_NAME, (byte) sideOfProvider.ordinal());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey(PROVIDER_TAG_NAME)) {
            plannedProvider = new PowerNodeCoordinates(myTile != null ? myTile.worldObj : null, tag, PROVIDER_TAG_NAME);
        } else {
            plannedProvider = null;
        }

        if (tag.hasKey(PROVIDER_SIDE_TAG_NAME) && plannedProvider != null) {
            plannedSideOfProvider = ForgeDirection.getOrientation(tag.getByte(PROVIDER_SIDE_TAG_NAME));
        } else {
            plannedProvider = null;
            plannedSideOfProvider = null;
        }

        if (!firstTick) {
            makePlannedConnection(true);
        }
    }

    private void removeEnergyFromBuffer(int consumed) {
        addEnergyToBuffer(-consumed);
    }
}
