package monnef.jaffas.power.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public interface IPowerProviderManager {
    void initialize(int maximalPacketSize, int bufferSize, TileEntity tile, boolean remoteConnection, byte directSidesMask);

    int getFreeSpaceInBuffer();

    int getBufferSize();

    int getMaximalPacketSize();

    TileEntity getTile();

    /**
     * Requests energy from provider.
     * Energy returned might be less, than requested.
     *
     * @param amount An amount.
     * @return How much energy is sent (always less than maximalPowerPerPacket()).
     */
    int requestEnergy(int amount);

    int maximalPowerPerPacket();

    int storeEnergy(int amount);

    /**
     * @return Can it be DIRECTLY connected (by player) to a consumer?
     */
    boolean supportRemoteConnection();

    boolean supportDirectConnection();

    boolean sideProvidesPower(ForgeDirection side);

    boolean hasFreeSlotForRemotePower();

    boolean connect(IPowerConsumer output);

    boolean connectDirect(IPowerConsumer output, ForgeDirection side);

    boolean disconnect(IPowerConsumer consumer);

    /**
     * Saves all consumers.
     *
     * @param tagCompound
     */
    void saveConsumersToNBT(NBTTagCompound tagCompound);

    /**
     * Loads all consumers, in first possible tick constructs all connections.
     *
     * @param tagCompound
     */
    void loadConsumersFromNBT(NBTTagCompound tagCompound);

    void tick();

    boolean isRemotelyConnected();

    boolean isConnectedToSide(ForgeDirection side);

    int getCurrentBufferedEnergy();
}

