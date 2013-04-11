/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.power.common;

import monnef.jaffas.power.api.IPowerNodeCoordinates;
import monnef.jaffas.power.api.IPowerNodeManager;
import monnef.jaffas.power.api.JaffasPowerException;
import monnef.jaffas.power.block.common.TileEntityMachine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public abstract class PowerNodeManager implements IPowerNodeManager {
    private static final String ENERGY_BUFFER_TAG_NAME = "energyBuffer";
    private static final int WORK_EVERY_XTH_TICK = 10;

    protected int maximalPacketSize = 20;
    protected int bufferSize = 40;
    protected int energyBuffer = 0;

    protected boolean initialized = false;
    protected TileEntityMachine myTile;
    protected IPowerNodeCoordinates myCoordinates;
    private int tickCounter;

    @Override
    public int getMaximalPacketSize() {
        return maximalPacketSize;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    private void setMaximalPacketSize(int size) {
        if (size <= 0) {
            throw new JaffasPowerException("Wrong size of packet.");
        }
        maximalPacketSize = size;
    }

    private void setBufferSize(int size) {
        if (size <= 0) {
            throw new JaffasPowerException("Wrong size of buffer.");
        }
        bufferSize = size;
    }

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
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile) {
        if (initialized) {
            throw new JaffasPowerException("Already initialized.");
        }

        initialized = true;
        setMaximalPacketSize(maximalPacketSize);
        setBufferSize(bufferSize);
        setTile(tile);
        myCoordinates = new PowerNodeCoordinates(tile);
    }

    @Override
    public abstract boolean isRemotelyConnected();

    @Override
    public abstract boolean isConnectedToSide(ForgeDirection side);

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public int getFreeSpaceInBuffer() {
        return bufferSize - energyBuffer;
    }

    private void setTile(TileEntity te) {
        myTile = (TileEntityMachine) te;
    }

    @Override
    public TileEntity getTile() {
        return myTile;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger(ENERGY_BUFFER_TAG_NAME, energyBuffer);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        int storedEnergy = tagCompound.getInteger(ENERGY_BUFFER_TAG_NAME);
        setEnergyBuffer(storedEnergy);
    }

    @Override
    public int getCurrentBufferedEnergy() {
        return this.energyBuffer;
    }

    protected void setEnergyBuffer(int energyBuffer) {
        this.energyBuffer = energyBuffer;
        sendUpdate();
    }

    @Override
    public int storeEnergy(int energy) {
        int toStore = energy;
        if (toStore <= 0) return 0;
        if (bufferSize < energyBuffer + toStore) {
            toStore = bufferSize - energyBuffer;
        }

        addEnergyToBuffer(toStore);
        return toStore;
    }

    @Override
    public void tick() {
        if (!initialized) return;

        tickCounter++;
        if (tickCounter > WORK_EVERY_XTH_TICK) {
            tickCounter = 0;
            doWork();
        }
    }

    protected void addEnergyToBuffer(int toStore) {
        setEnergyBuffer(energyBuffer + toStore);
    }

    @Override
    public IPowerNodeCoordinates getCoordinates() {
        return this.myCoordinates;
    }

    @Override
    public void sendUpdate() {
        if (myTile != null) myTile.sendUpdate();
    }

    @Override
    public void invalidate() {
        disconnectAll();
    }
}
