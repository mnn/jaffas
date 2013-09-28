/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import monnef.core.api.IIntegerCoordinates;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.Random;

public abstract class TileEntityMachine extends TileEntity implements IPowerReceptor, IPowerEmitter {
    public static final String ROTATION_TAG_NAME = "rotation";
    public static final Random rand = new Random();
    private static final int DUMMY_CREATION_PHASE_INSTANCE_COUNTER_LIMIT = 5;
    protected int slowingCoefficient = 1;
    protected int doWorkCounter;

    private ForgeDirection rotation;
    protected PowerHandler powerHandler;

    protected int powerNeeded;
    protected int powerStorage;
    protected int maxEnergyReceived;
    protected PowerHandler.Type bcPowerType;
    private boolean isRedstoneSensitive = false;
    private boolean cachedRedstoneStatus;
    private boolean isRedstoneStatusDirty;
    private boolean forceFullCubeRenderBoundingBox;
    private boolean isPowerSource;

    protected TileEntityMachine() {
        onNewInstance(this);
        setRotation(ForgeDirection.UNKNOWN);

        configurePowerParameters();
        powerHandler = new PowerHandler(this, bcPowerType);
        powerHandler.configure(2, maxEnergyReceived, powerNeeded, powerStorage);
    }

    public void setForceFullCubeRenderBoundingBox(boolean value) {
        forceFullCubeRenderBoundingBox = value;
    }

    public boolean isRedstoneSensitive() {
        return isRedstoneSensitive;
    }

    public void setIsRedstoneSensitive() {
        isRedstoneSensitive = true;
    }

    public abstract String getMachineTitle();

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (isRedstoneStatusDirty) {
            isRedstoneStatusDirty = false;
            refreshCachedRedstoneStatus();
        }
        powerHandler.update();
    }

    /**
     * Configures this instance to serve as an engine.
     * Use only from {@link #configurePowerParameters}.
     */
    protected void configureAsPowerSource() {
        isPowerSource = true;
        bcPowerType = PowerHandler.Type.ENGINE;
        powerNeeded = 0;
    }

    protected void configurePowerParameters() {
        powerNeeded = 20;
        maxEnergyReceived = 20;
        powerStorage = 10 * powerNeeded;
        bcPowerType = PowerHandler.Type.MACHINE;
    }

    public BlockMachine getMachineBlock() {
        return (BlockMachine) this.getBlockType();
    }

    public ForgeDirection getRotation() {
        return rotation;
    }

    public void setRotation(ForgeDirection rotation) {
        this.rotation = rotation;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.rotation = ForgeDirection.getOrientation(tag.getByte(ROTATION_TAG_NAME));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte(ROTATION_TAG_NAME, (byte) this.rotation.ordinal());
    }

    @Override
    public void validate() {
        super.validate();
        markRedstoneStatusDirty();
    }

    public void markRedstoneStatusDirty() {
        isRedstoneStatusDirty = true;
    }

    @Override
    public Packet getDescriptionPacket() {
        Packet132TileEntityData packet = (Packet132TileEntityData) super.getDescriptionPacket();
        NBTTagCompound tag = packet != null ? packet.data : new NBTTagCompound();

        writeToNBT(tag);

        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.data;
        readFromNBT(tag);
    }

    public void sendUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setRotation(int direction) {
        this.setRotation(ForgeDirection.getOrientation(direction));
    }

    private static boolean dummyCreationPhase = false;
    private static int dummyCreationPhaseCounter;

    public static void enableDummyCreationPhase() {
        if (dummyCreationPhase) {
            throw new RuntimeException("Already in dummy creation phase.");
        }

        dummyCreationPhase = true;
        dummyCreationPhaseCounter = 0;
    }

    public static void disableDummyCreationPhase() {
        if (!dummyCreationPhase) {
            throw new RuntimeException("Not in dummy creation phase.");
        }

        dummyCreationPhase = false;
    }

    private static void onNewInstance(TileEntityMachine instance) {
        if (dummyCreationPhase) {
            dummyCreationPhaseCounter++;
        }

        if (dummyCreationPhaseCounter >= DUMMY_CREATION_PHASE_INSTANCE_COUNTER_LIMIT) {
            JaffasFood.Log.printSevere(instance.getClass().getSimpleName() + ": limit of dummy creation has been exceeded!");
        }
    }

    protected void refreshCachedRedstoneStatus() {
        if (!isRedstoneSensitive()) return;
        cachedRedstoneStatus = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
    }

    /**
     * Returns if machine is getting any redstone power.
     * Cached!
     *
     * @return True if so.
     */
    public boolean isBeingPoweredByRedstone() {
        return cachedRedstoneStatus;
    }

    public boolean toggleRotation() {
        rotation = ForgeDirection.VALID_DIRECTIONS[(rotation.ordinal() + 1) % 4];
        return true;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (forceFullCubeRenderBoundingBox) {
            return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
        }
        return super.getRenderBoundingBox();
    }

    protected abstract void doMachineWork();

    public IIntegerCoordinates getPosition() {
        return new IntegerCoordinates(this);
    }

    public void onItemDebug(EntityPlayer player) {
    }

    //<editor-fold desc="BuildCraft API">
    @Override
    public final void doWork(PowerHandler workProvider) {
        doWorkCounter++;
        if (doWorkCounter >= slowingCoefficient) {
            doWorkCounter = 0;
            doMachineWork();
        }
    }

    @Override
    public boolean canEmitPowerFrom(ForgeDirection side) {
        return isPowerSource;
    }

    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection side) {
        return powerHandler.getPowerReceiver();
    }

    @Override
    public World getWorld() {
        return worldObj;
    }
    //</editor-fold>
}

