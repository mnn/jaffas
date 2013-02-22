package monnef.jaffas.power;

import monnef.jaffas.food.mod_jaffas_food;
import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerNodeCoordinates;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.JaffasPowerException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class PowerConsumerManager extends PowerNodeManager implements IPowerConsumerManager {
    private static final String PROVIDER_TAG_NAME = "provider";
    private static final String PROVIDER_SIDE_TAG_NAME = "providerSide";
    private IPowerNodeCoordinates provider;
    private ForgeDirection sideToProvider;

    private IPowerNodeCoordinates plannedProvider;
    private ForgeDirection plannedSideToProvider;

    private boolean firstTick = true;

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile) {
        if (initialized) {
            throw new JaffasPowerException("already initialized");
        }
        super.initialize(maximalPacketSize, bufferSize, tile);
        sideToProvider = null;
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
    public void connectDirect(IPowerNodeCoordinates provider, ForgeDirection sideToProvider) {
        if (this.provider != null) {
            //throw new JaffasPowerException("Connecting already connected consumer.");
            System.err.println("Connecting already connected consumer.");
            //TODO disconnect?
        }

        if (sideToProvider != myTile.getRotation()) {
            mod_jaffas_food.Log.printDebug("Rotations of connection does not match");
        }

        setProvider(provider, sideToProvider);
    }

    private void setProvider(IPowerNodeCoordinates provider, ForgeDirection sideToProvider) {
        this.provider = provider;
        this.sideToProvider = sideToProvider;
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
        if (sideToProvider == ForgeDirection.UNKNOWN) return false;
        return side == sideToProvider;
    }

    @Override
    public void doWork() {
        if (energyNeeded() && provider != null && provider.asProvider() != null) {
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
        if (plannedSideToProvider == null) return;

        // side in connect is from provider's view => invert rotation
        PowerUtils.connect(plannedProvider, myCoordinates, plannedSideToProvider.getOpposite());

        plannedProvider = null;
        plannedSideToProvider = null;
        if (canUpdate) sendUpdate();
    }

    @Override
    public IPowerNodeCoordinates getProvider() {
        return provider;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (provider != null) {
            provider.saveTo(tag, PROVIDER_TAG_NAME);
        }
        if (sideToProvider != null) {
            tag.setByte(PROVIDER_SIDE_TAG_NAME, (byte) sideToProvider.ordinal());
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
            plannedSideToProvider = ForgeDirection.getOrientation(tag.getByte(PROVIDER_SIDE_TAG_NAME));
        } else {
            plannedProvider = null;
            plannedSideToProvider = null;
        }

        if (!firstTick) {
            makePlannedConnection(true);
        }
    }

    private void removeEnergyFromBuffer(int consumed) {
        addEnergyToBuffer(-consumed);
    }

    @Override
    public void disconnectAll() {
        PowerUtils.disconnect(provider, myCoordinates);
    }

    @Override
    public void tryDirectConnect() {
        if (myCoordinates.getWorld().isRemote) return;

        ForgeDirection rot = myTile.getRotation();
        ForgeDirection rotInv = rot.getOpposite();
        int provX = myTile.xCoord + rot.offsetX;
        int provY = myTile.yCoord + rot.offsetY;
        int provZ = myTile.zCoord + rot.offsetZ;

        TileEntity te = myCoordinates.getWorld().getBlockTileEntity(provX, provY, provZ);

        if (te instanceof IPowerProvider) {
            IPowerProvider provider = (IPowerProvider) te;
            if (provider.getPowerProviderManager().supportDirectConnection()) {
                PowerUtils.connect(provider.getPowerProviderManager().getCoordinates(), myCoordinates, rotInv);
            }
        }
    }

}
