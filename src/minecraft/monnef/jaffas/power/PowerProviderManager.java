package monnef.jaffas.power;

import com.google.common.collect.HashBiMap;
import monnef.jaffas.food.mod_jaffas;
import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerNodeCoordinates;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.api.JaffasPowerException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.HashMap;
import java.util.Set;

public class PowerProviderManager implements IPowerProviderManager {
    private static final int MINIMAL_ENERGY_TO_TRANSFER = 5;
    private int maximalPacketSize;
    private int bufferSize;
    private TileEntity tile;
    private boolean remoteConnection;
    private boolean[] directSidesMask;

    private HashBiMap<ForgeDirection, IPowerNodeCoordinates> consumers;
    private HashMap<IPowerNodeCoordinates, Integer> distance;
    private int energyBuffer;

    private boolean firstTick = true;

    private boolean initialized = false;
    private boolean supportDirectConn;
    private IPowerNodeCoordinates myCoordinates;

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile, boolean remoteConnection, boolean[] directSidesMask) {
        if (initialized) {
            throw new JaffasPowerException("already initialized");
        }
        initialized = true;

        if (directSidesMask.length != 6) {
            throw new JaffasPowerException("wrong size of direct side connection mask");
        }

        this.maximalPacketSize = maximalPacketSize;
        this.bufferSize = bufferSize;
        this.tile = tile;
        this.remoteConnection = remoteConnection;
        this.directSidesMask = directSidesMask;
        this.consumers = HashBiMap.create(new HashMap<ForgeDirection, IPowerNodeCoordinates>());

        this.energyBuffer = 0;
        this.myCoordinates = new PowerNodeCoordinates(tile);

        fillDirectConnectionSupport();
    }

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile, boolean remoteConnection) {
        initialize(maximalPacketSize, bufferSize, tile, remoteConnection, new boolean[6]);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    private void fillDirectConnectionSupport() {
        supportDirectConn = false;
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            if (directSidesMask[i]) supportDirectConn = true;
        }
    }

    @Override
    public int getFreeSpaceInBuffer() {
        return getBufferSize() - energyBuffer;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public boolean hasBuffered(int energy) {
        return getCurrentBufferedEnergy() >= energy;
    }

    @Override
    public int getMaximalPacketSize() {
        return maximalPacketSize;
    }

    @Override
    public TileEntity getTile() {
        return tile;
    }

    @Override
    public IPowerNodeCoordinates getCoordinates() {
        return this.myCoordinates;
    }

    @Override
    public int requestEnergy(int amount, IPowerNodeCoordinates consumer) {
        if (energyBuffer < MINIMAL_ENERGY_TO_TRANSFER) return 0;

        int toTransfer = amount;
        if (toTransfer > getMaximalPacketSize()) {
            toTransfer = getMaximalPacketSize();
        }

        if (toTransfer > getCurrentBufferedEnergy()) {
            toTransfer = getCurrentBufferedEnergy();
        }

        setEnergyBuffer(getCurrentBufferedEnergy() - toTransfer);

        int energyAfterLoss = PowerUtils.loseEnergy(toTransfer, getDistance(consumer));

        return energyAfterLoss;
    }

    private int getDistance(IPowerNodeCoordinates consumer) {
        if (distance.containsKey(consumer)) {
            return distance.get(consumer);
        }

        distance.put(consumer, computeDistance(consumer));
        return getDistance(consumer);
    }

    private Integer computeDistance(IPowerNodeCoordinates consumer) {
        //TileEntity consumerTile = consumer.getPowerConsumerManager().getTile();
        //float f = MathHelper.sqrt_float(Square(getTile().xCoord - consumerTile.xCoord) + Square(getTile().yCoord - consumerTile.yCoord) + Square(getTile().zCoord - consumerTile.zCoord));

        return monnef.core.MathHelper.exactDistanceInt(consumer, myCoordinates);
    }

    @Override
    public int maximalPowerPerPacket() {
        return maximalPacketSize;
    }

    @Override
    public int storeEnergy(int amount) {
        int toStore = amount;
        if (energyBuffer + toStore > bufferSize) {
            toStore = bufferSize - energyBuffer;
        }

        setEnergyBuffer(energyBuffer + toStore);
        return toStore;
    }

    @Override
    public boolean supportRemoteConnection() {
        return remoteConnection;
    }

    @Override
    public boolean supportDirectConnection() {
        return supportDirectConn;
    }

    @Override
    public boolean sideProvidesPower(ForgeDirection side) {
        return directSidesMask[side.ordinal()];
    }

    @Override
    public boolean hasFreeSlotForRemotePower() {
        if (!supportRemoteConnection()) {
            return false;
        } else {
            return !isRemotelyConnected();
        }
    }

    @Override
    public boolean connect(IPowerNodeCoordinates consumer) {
        if (!supportRemoteConnection()) {
            throw new JaffasPowerException("provider doesn't support remote connections");
        }

        if (isRemotelyConnected()) {
            throw new JaffasPowerException("provider already connected");
        }

        SetConsumer(ForgeDirection.UNKNOWN, consumer);
        return true;
    }

    private void SetConsumer(ForgeDirection side, IPowerNodeCoordinates consumer) {
        consumers.put(side, consumer);
    }

    @Override
    public boolean connectDirect(IPowerNodeCoordinates consumer, ForgeDirection side) {
        if (!supportDirectConnection()) {
            if (mod_jaffas.debug) System.err.println("[PPM] provider doesn't support direct connections");
            //throw new JaffasPowerException("provider doesn't support direct connections");
        }

        if (isConnectedToSide(side)) {
            if (mod_jaffas.debug) System.err.println("[PPM] provider already connected");
            //throw new JaffasPowerException("provider already connected");
        }

        SetConsumer(side, consumer);
        return true;
    }

    @Override
    public boolean disconnect(IPowerNodeCoordinates consumer) {
        if (!consumers.containsValue(consumer)) {
            if (mod_jaffas.debug) System.err.println("[PPM] consumer is not connected, cannot disconnect");
//            throw new JaffasPowerException("consumer is not connected, cannot disconnect");
        }

        consumers.remove(consumers.inverse().get(consumer));
        return true;
    }

    @Override
    public void tick() {
        if (firstTick) {
            firstTick = false;

            //TODO make connections
        }
    }

    @Override
    public boolean isRemotelyConnected() {
        return isConnectedToSide(ForgeDirection.UNKNOWN, false);
    }

    @Override
    public boolean isConnectedToSide(ForgeDirection side) {
        return isConnectedToSide(side, true);
    }

    private boolean isConnectedToSide(ForgeDirection side, boolean doCheck) {
        if (doCheck && side == ForgeDirection.UNKNOWN) {
            throw new JaffasPowerException("wrong side");
        }

        return consumers.containsKey(side);
    }

    private void setEnergyBuffer(int energyBuffer) {
        this.energyBuffer = energyBuffer;
    }

    @Override
    public int getCurrentBufferedEnergy() {
        return energyBuffer;
    }

    // save consumers
    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        throw new RuntimeException("Not implemented yet.");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    // load consumers
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        throw new RuntimeException("Not implemented yet.");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean[] constructConnectedSides() {
        boolean[] res = new boolean[7];
        if (consumers == null) {
            throw new RuntimeException("consumers map is null");
        }
        Set<ForgeDirection> consumersSides = consumers.keySet();
        for (ForgeDirection dir : consumersSides) {
            res[dir.ordinal()] = true;
        }

        return res;
    }

    @Override
    public IPowerConsumer getConsumer(ForgeDirection side) {
        IPowerNodeCoordinates consumer = consumers.get(side);
        return consumer == null ? null : (IPowerConsumer) consumer.getTile();
    }
}
