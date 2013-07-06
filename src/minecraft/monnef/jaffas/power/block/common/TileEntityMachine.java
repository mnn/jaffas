/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.Random;

public abstract class TileEntityMachine extends TileEntity implements IPowerReceptor {
    public static final String ROTATION_TAG_NAME = "rotation";
    public static final Random rand = new Random();
    private static final int DUMMY_CREATION_PHASE_INSTANCE_COUNTER_LIMIT = 5;

    private ForgeDirection rotation;
    protected IPowerProvider powerProvider;

    protected int powerNeeded;
    protected int powerStorage;
    protected int maxEnergyReceived;

    public abstract String getMachineTitle();

    protected TileEntityMachine() {
        onNewInstance(this);
        setRotation(ForgeDirection.UNKNOWN);
        if (PowerFramework.currentFramework != null) {
            powerProvider = PowerFramework.currentFramework.createPowerProvider();
            configurePowerParameters();
            powerProvider.configure(0, 2, maxEnergyReceived, powerNeeded, powerStorage);
        } else {
            if (!dummyCreationPhase) {
                JaffasFood.Log.printWarning("Got null in power framework, this should never happen!");
            }
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        getPowerProvider().update(this);
    }

    protected void configurePowerParameters() {
        powerNeeded = 20;
        maxEnergyReceived = 20;
        powerStorage = 10 * powerNeeded;
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
    public Packet getDescriptionPacket() {
        Packet132TileEntityData packet = (Packet132TileEntityData) super.getDescriptionPacket();
        NBTTagCompound tag = packet != null ? packet.customParam1 : new NBTTagCompound();

        writeToNBT(tag);

        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.customParam1;
        readFromNBT(tag);
    }

    public void sendUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setRotation(int direction) {
        this.setRotation(ForgeDirection.getOrientation(direction));
    }

    @Override
    public void setPowerProvider(IPowerProvider provider) {
        powerProvider = provider;
    }

    @Override
    public IPowerProvider getPowerProvider() {
        return powerProvider;
    }

    @Override
    public int powerRequest(ForgeDirection from) {
        return maxEnergyReceived;
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
}

