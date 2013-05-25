/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.utils.RandomHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFungiBox extends TileEntity {
    private static final String TAG_FUNGUS_TYPE = "fungusType";
    private static final String TAG_FUNGUS_STATE = "fungusState";
    private static final String TAG_HUMUS_LEFT = "humusLeft";
    private static final String TAG_DIE = "timeToDie";
    private static final String TAG_SPORE = "timeToSpore";
    private byte rotation;

    private int fungusType = 0; // zero = nothing here
    private int humusTicksLeft = 0; // zero = no humus
    private int fungusState = 0;
    private int timeToDie;
    private int timeToSpore;

    @Override
    public void updateEntity() {
        super.updateEntity();
        // TODO
        if (RandomHelper.rollPercentBooleanDice(5)) {
            fungusType = 1;
            fungusState = RandomHelper.generateRandomFromInterval(0, 3);
            forceUpdate();
        }
    }

    @Override
    public void validate() {
        super.validate();
        init();
    }

    public int getModelIndex() {
        return fungusType - 1;
    }

    public int getRenderState() {
        return fungusState - 1;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger(TAG_FUNGUS_TYPE, fungusType);
        tag.setInteger(TAG_FUNGUS_STATE, fungusState);
        tag.setInteger(TAG_HUMUS_LEFT, humusTicksLeft);
        tag.setInteger(TAG_DIE, timeToDie);
        tag.setInteger(TAG_SPORE, timeToSpore);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        init();
        fungusType = tag.getInteger(TAG_FUNGUS_TYPE);
        fungusState = tag.getInteger(TAG_FUNGUS_STATE);
        humusTicksLeft = tag.getInteger(TAG_HUMUS_LEFT);
        timeToDie = tag.getInteger(TAG_DIE);
        timeToSpore = tag.getInteger(TAG_SPORE);
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

    public byte getRenderRotation() {
        return rotation;
    }

    private void forceUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void init() {
        rotation = (byte) ((22457 + xCoord * 7775213 + yCoord - zCoord * 22177) % 4);
    }
}
