package monnef.jaffas.power.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public interface IPowerProviderManager extends IPowerNode {
    void initialize(int maximalPacketSize, int bufferSize, TileEntity tile, boolean remoteConnection, boolean[] directSidesMask);

    void initialize(int maximalPacketSize, int bufferSize, TileEntity tile, boolean remoteConnection);

    /**
     * Requests energy from provider.
     * Energy returned might be less, than requested.
     *
     * @param amount   An amount.
     * @param consumer
     * @return How much energy is sent (always less than maximalPowerPerPacket()).
     */
    int requestEnergy(int amount, IPowerConsumer consumer);

    int maximalPowerPerPacket();

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

    boolean[] constructConnectedSides();

    IPowerConsumer getConsumer(ForgeDirection side);
}

