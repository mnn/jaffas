package monnef.jaffas.power.common;

import com.google.common.collect.HashBiMap;
import monnef.core.utils.MathHelper;
import monnef.jaffas.food.jaffasFood;
import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerNodeCoordinates;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.api.JaffasPowerException;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.HashMap;
import java.util.Set;

public class PowerProviderManager extends PowerNodeManager implements IPowerProviderManager {
    private static final int MINIMAL_ENERGY_TO_TRANSFER = 5;
    private boolean remoteConnection;
    private boolean[] directSidesMask;

    private HashBiMap<ForgeDirection, IPowerNodeCoordinates> consumers;
    private HashMap<IPowerNodeCoordinates, Integer> distance;

    private boolean initialized = false;
    private boolean supportDirectConn;

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile, boolean remoteConnection, boolean[] directSidesMask) {
        if (initialized) {
            throw new JaffasPowerException("already initialized");
        }

        if (directSidesMask.length != 6) {
            throw new JaffasPowerException("wrong size of direct side connection mask");
        }

        super.initialize(maximalPacketSize, bufferSize, tile);

        this.remoteConnection = remoteConnection;
        this.directSidesMask = directSidesMask;
        this.consumers = HashBiMap.create(new HashMap<ForgeDirection, IPowerNodeCoordinates>());
        this.distance = new HashMap<IPowerNodeCoordinates, Integer>();

        this.energyBuffer = 0;

        fillDirectConnectionSupport();
        initialized = true;
    }

    @Override
    public void initialize(int maximalPacketSize, int bufferSize, TileEntity tile, boolean remoteConnection) {
        initialize(maximalPacketSize, bufferSize, tile, remoteConnection, new boolean[6]);
    }

    private void fillDirectConnectionSupport() {
        supportDirectConn = false;
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            if (directSidesMask[i]) supportDirectConn = true;
        }
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

        return MathHelper.exactDistanceInt(consumer, myCoordinates);
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
        if (!initialized) return false;

        if (!supportRemoteConnection()) {
            if (jaffasFood.debug) System.err.println("[PPM] provider doesn't support remote connections");
            //            throw new JaffasPowerException("provider doesn't support remote connections");
        }

        if (isRemotelyConnected()) {
            if (jaffasFood.debug) System.err.println("[PPM] provider already connected");
            //            throw new JaffasPowerException("provider already connected");
        }

        SetConsumer(ForgeDirection.UNKNOWN, consumer);
        return true;
    }

    private void SetConsumer(ForgeDirection side, IPowerNodeCoordinates consumer) {
        consumers.put(side, consumer);
    }

    @Override
    public boolean connectDirect(IPowerNodeCoordinates consumer, ForgeDirection side) {
        if (!initialized) return false;

        if (!supportDirectConnection()) {
            if (jaffasFood.debug) System.err.println("[PPM] provider doesn't support direct connections");
            return false;
        }

        if (isConnectedToSide(side)) {
            if (jaffasFood.debug) System.err.println("[PPM] provider already connected, disconnecting");
            disconnect(consumers.get(side));
        }

        if (!sideProvidesPower(side)) {
            if (jaffasFood.debug) System.err.println("[PPM] side does not provide power");
            return false;
        }

        SetConsumer(side, consumer);
        return true;
    }

    @Override
    public boolean disconnect(IPowerNodeCoordinates consumer) {
        if (!initialized) return false;

        if (!consumers.containsValue(consumer)) {
            if (jaffasFood.debug) System.err.println("[PPM] consumer is not connected, cannot disconnect");
//            throw new JaffasPowerException("consumer is not connected, cannot disconnect");
        }

        consumers.remove(consumers.inverse().get(consumer));
        return true;
    }

    @Override
    public boolean isRemotelyConnected() {
        return isConnectedToSide(ForgeDirection.UNKNOWN, false);
    }

    @Override
    public boolean isConnectedToSide(ForgeDirection side) {
        return isConnectedToSide(side, true);
    }

    @Override
    public void doWork() {
    }

    private boolean isConnectedToSide(ForgeDirection side, boolean doCheck) {
        if (doCheck && side == ForgeDirection.UNKNOWN) {
            throw new JaffasPowerException("wrong side");
        }

        return consumers.containsKey(side);
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

    @Override
    public void disconnectAll() {
        for (IPowerNodeCoordinates consumer : consumers.values()) {
            PowerUtils.disconnect(myCoordinates, consumer);
        }
    }
}
