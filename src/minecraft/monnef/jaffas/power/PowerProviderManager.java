package monnef.jaffas.power;

import com.google.common.collect.HashBiMap;
import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.api.JaffasPowerException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;

import java.util.HashMap;

import static monnef.core.MathHelper.Square;

public class PowerProviderManager implements IPowerProviderManager {
    private static final int MINIMAL_ENERGY_TO_TRANSFER = 5;
    private int maximalPacketSize;
    private int bufferSize;
    private TileEntity tile;
    private boolean remoteConnection;
    private boolean[] directSidesMask;

    private HashBiMap<ForgeDirection, IPowerConsumerManager> consumers;
    private HashMap<IPowerConsumerManager, Integer> distance;
    private int energyBuffer;

    private boolean firstTick = true;

    private boolean initialized = false;
    private boolean supportDirectConn;

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile, boolean remoteConnection, boolean[] directSidesMask) {
        if (initialized) {
            throw new JaffasPowerException("already initialized");
        }
        initialized = true;

        this.maximalPacketSize = maximalPacketSize;
        this.bufferSize = bufferSize;
        this.tile = tile;
        this.remoteConnection = remoteConnection;
        this.directSidesMask = directSidesMask;
        this.consumers = HashBiMap.create(new HashMap<ForgeDirection, IPowerConsumerManager>());

        this.energyBuffer = 0;

        fillDirectConnectionSupport();
    }

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile, boolean remoteConnection) {
        initialize(maximalPacketSize, bufferSize, tile, remoteConnection, new boolean[7]);
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
    public int getMaximalPacketSize() {
        return maximalPacketSize;
    }

    @Override
    public TileEntity getTile() {
        return tile;
    }

    @Override
    public int requestEnergy(int amount, IPowerConsumer consumer) {
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

    private int getDistance(IPowerConsumer consumer) {
        if (distance.containsKey(consumer)) {
            return distance.get(consumer);
        }

        distance.put(consumer.getPowerManager(), computeDistance(consumer));
        return getDistance(consumer);
    }

    private Integer computeDistance(IPowerConsumer consumer) {
        TileEntity consumerTile = consumer.getPowerManager().getTile();
        float f = MathHelper.sqrt_float(Square(getTile().xCoord - consumerTile.xCoord) + Square(getTile().yCoord - consumerTile.yCoord) + Square(getTile().zCoord - consumerTile.zCoord));
        return MathHelper.ceiling_float_int(f);
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
    public boolean connect(IPowerConsumer output) {
        if (!supportRemoteConnection()) {
            throw new JaffasPowerException("provider doesn't support remote connections");
        }

        if (isRemotelyConnected()) {
            throw new JaffasPowerException("provider already connected");
        }

        SetConsumer(ForgeDirection.UNKNOWN, output);
        return true;
    }

    private void SetConsumer(ForgeDirection side, IPowerConsumer consumer) {
        consumers.put(side, consumer.getPowerManager());
    }

    @Override
    public boolean connectDirect(IPowerConsumer output, ForgeDirection side) {
        if (!supportDirectConnection()) {
            throw new JaffasPowerException("provider doesn't support direct connections");
        }

        if (isConnectedToSide(side)) {
            throw new JaffasPowerException("provider already connected");
        }

        SetConsumer(side, output);
        return true;
    }

    @Override
    public boolean disconnect(IPowerConsumer consumer) {
        if (!consumers.containsValue(consumer)) {
            throw new JaffasPowerException("consumer is not connected, cannot disconnect");
        }

        consumers.remove(consumers.inverse().get(consumer));
        return true;
    }

    @Override
    public void saveConsumersToNBT(NBTTagCompound tagCompound) {
        // TODO
        return;
    }

    @Override
    public void loadConsumersFromNBT(NBTTagCompound tagCompound) {
        // TODO
        return;
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

    @Override
    public boolean[] constructConnectedSides() {
        boolean[] res = new boolean[7];
        for (ForgeDirection dir : consumers.keySet()) {
            res[dir.ordinal()] = true;
        }

        return res;
    }

    @Override
    public IPowerConsumer getConsumer(ForgeDirection side) {
        IPowerConsumerManager consumer = consumers.get(side);
        return consumer == null ? null : (IPowerConsumer) consumer.getTile();
    }
}
