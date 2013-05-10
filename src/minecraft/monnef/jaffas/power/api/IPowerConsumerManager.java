/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public interface IPowerConsumerManager extends IPowerNodeManager {
    void initialize(int maximalPacketSize, int bufferSize, TileEntity tile);

    int getCurrentMaximalPacketSize();

    /**
     * Consumes energy from a buffer.
     *
     * @param energy Energy amount.
     * @return Energy successfully consumed.
     */
    int consume(int energy);

    void connect(IPowerNodeCoordinates provider);

    void connectDirect(IPowerNodeCoordinates provider, ForgeDirection side);

    void disconnect();

    /**
     * Is buffer not full?
     *
     * @return Power consumer wants juice.
     */
    boolean energyNeeded();

    IPowerNodeCoordinates getProvider();

    boolean tryDirectConnect();
}
