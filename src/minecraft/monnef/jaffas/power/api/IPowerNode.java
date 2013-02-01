package monnef.jaffas.power.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public interface IPowerNode {
    int getMaximalPacketSize();

    int getBufferSize();

    boolean hasBuffered(int energy);

    int getFreeSpaceInBuffer();

    TileEntity getTile();

    int getCurrentBufferedEnergy();

    /**
     * Saves inner energy state.
     *
     * @param tagCompound
     */
    void writeToNBT(NBTTagCompound tagCompound);

    void readFromNBT(NBTTagCompound tagCompound);

    /**
     * Stores energy in a buffer (called by antennas).
     *
     * @param energy Energy amount.
     * @return Energy successfully stored.
     */
    int storeEnergy(int energy);

    /**
     * Only in this method will manager request power (maximal one packet).
     * Do NOT call more often than once per tick.
     */
    void tick();

    boolean isRemotelyConnected();

    boolean isConnectedToSide(ForgeDirection side);
}
