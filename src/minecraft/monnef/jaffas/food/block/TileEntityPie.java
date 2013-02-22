package monnef.jaffas.food.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

import static monnef.jaffas.food.mod_jaffas_food.Log;

public class TileEntityPie extends TileEntity {
    public static final int PIECES_COUNT = 4;

    public PieType type;
    public boolean[] pieces;
    public int rotation;

    private static Random rand = new Random();

    private boolean debug = true;

    public TileEntityPie() {
        super();
        pieces = new boolean[PIECES_COUNT];
    }

    public void init(int rotation, int type) {
        this.rotation = rotation;
        this.type = PieType.values()[type];
        for (int i = 0; i < PIECES_COUNT; i++) {
            pieces[i] = true;
        }

        sendUpdate();
    }

    public void eatPiece(EntityPlayer player) {
        if (debug) {
            String p = "";
            for (int i = 0; i < PIECES_COUNT; i++) {
                p += pieces[i] ? "1" : "0";
            }
            Log.printInfo("R:" + rotation + " T:" + type + " P:" + p);
        }

        if (worldObj.isRemote) {
            return;
        }

        if (player.canEat(false)) {
            player.getFoodStats().addStats(4, 0.1F);

            int selectedPiece = rand.nextInt(PIECES_COUNT);
            while (!pieces[selectedPiece]) {
                selectedPiece = (selectedPiece + 1) % PIECES_COUNT;
            }

            pieces[selectedPiece] = false;
            sendUpdate();
            checkGotPieces();
        }
    }

    private void checkGotPieces() {
        for (int i = 0; i < PIECES_COUNT; i++) {
            if (pieces[i]) return;
        }

        // all pieces eaten, destroy
        worldObj.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
        this.invalidate();
    }

    private void sendUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public enum PieType {
        STRAWBERRY,
        RASPBERRY,
        VANILLA,
        PLUM
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        addInfoToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        loadInfoFromNBT(tag);
    }

    private void addInfoToNBT(NBTTagCompound tag) {
        for (int i = 0; i < PIECES_COUNT; i++) {
            tag.setBoolean("piece" + i, pieces[i]);
        }
        tag.setByte("rotation", (byte) rotation);
        tag.setByte("type", (byte) type.ordinal());
    }

    private void loadInfoFromNBT(NBTTagCompound tag) {
        for (int i = 0; i < PIECES_COUNT; i++) {
            pieces[i] = tag.getBoolean("piece" + i);
        }
        rotation = tag.getByte("rotation");
        type = PieType.values()[tag.getByte("type")];
    }

    @Override
    public Packet getDescriptionPacket() {
        Packet132TileEntityData packet = (Packet132TileEntityData) super.getDescriptionPacket();
        NBTTagCompound tag = packet != null ? packet.customParam1 : new NBTTagCompound();

        addInfoToNBT(tag);

        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.customParam1;
        loadInfoFromNBT(tag);
        checkGotPieces();
    }
}

