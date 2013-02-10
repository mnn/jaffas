package monnef.jaffas.power.block.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public abstract class TileEntityMachine extends TileEntity {
    public static final String ROTATION_TAG_NAME = "rotation";
    private ForgeDirection rotation;

    public abstract String getMachineTitle();

    private int tickCounter = 0;

    protected TileEntityMachine() {
        setRotation(ForgeDirection.UNKNOWN);
    }

    public BlockMachine getMachineBlock() {
        return (BlockMachine) this.getBlockType();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (tickCounter < 10) {
            tickCounter++;
            onTick(tickCounter);
        }
    }

    protected void onTick(int number) {
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
}

