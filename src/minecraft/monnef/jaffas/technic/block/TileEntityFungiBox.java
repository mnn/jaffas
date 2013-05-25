/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.fungi.FungiCatalog;
import monnef.jaffas.technic.fungi.FungusInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import static monnef.core.utils.RandomHelper.rand;

public class TileEntityFungiBox extends TileEntity {
    private static final String TAG_FUNGUS_TYPE = "fungusType";
    private static final String TAG_FUNGUS_STATE = "fungusState";
    private static final String TAG_HUMUS_LEFT = "humusLeft";
    private static final String TAG_DIE = "timeToDie";
    private static final String TAG_SPORE = "timeToSpore";
    private static final String TAG_GROW = "timeToGrow";
    private byte rotation;

    private int fungusType = 0; // zero = nothing here
    private int humusTicksLeft = 0; // zero = no humus
    private int fungusState = 0;
    private int timeToDie;
    private int timeToSpore;
    private int timeToGrow;

    private int counter = rand.nextInt(20 * 60);

    private FungusInfo fungusTemplate;

    public static int tickQuantum = 20;

    @Override
    public void updateEntity() {
        super.updateEntity();
        // TODO

        /*
        if (RandomHelper.rollPercentBooleanDice(5)) {
            fungusType = 1;
            fungusState = RandomHelper.generateRandomFromInterval(0, 3);
            forceUpdate();
        }
        */

        counter++;
        if (counter % tickQuantum == 0) {
            boolean update = false;

            humusTicksLeft -= tickQuantum;
            if (humusTicksLeft < 0) humusTicksLeft = 0;

            if (mushroomPlanted()) {
                timeToGrow -= tickQuantum;
                if (timeToGrow <= 0) {
                    if (!isMature()) {
                        fungusState++;
                        timeToGrow = fungusTemplate.stateLength[fungusState].getRandom();
                        update = true;
                        if (isMature()) {
                            timeToDie = fungusTemplate.timeToDie.getRandom();
                            setupNextSporeTime();
                        }
                    } else {
                        timeToGrow = 20 * 10;
                    }
                }

                if (isMature()) {
                    timeToDie -= tickQuantum;
                    if (timeToDie <= 0) {
                        update = true;
                        fungusType = 0;
                    } else {
                        // not dead
                        timeToSpore -= tickQuantum;
                        if (timeToSpore <= 0) {
                            doSporage();
                            setupNextSporeTime();
                        }
                    }
                }
            }
            if (update) {
                forceUpdate();
            }
        }
    }

    private void doSporage() {
        // TODO
        worldObj.setBlock(xCoord, yCoord + 1, zCoord, Block.glass.blockID);
    }

    private void setupNextSporeTime() {
        timeToSpore = fungusTemplate.sporeTime.getRandom();
    }

    public boolean mushroomPlanted() {
        return fungusType > 0;
    }

    public boolean isMature() {
        if (fungusTemplate == null) {
            throw new RuntimeException("Wrong state of fungi box.");
        }

        return fungusState >= fungusTemplate.statesCount;
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
        tag.setInteger(TAG_GROW, timeToGrow);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        fungusType = tag.getInteger(TAG_FUNGUS_TYPE);
        fungusState = tag.getInteger(TAG_FUNGUS_STATE);
        humusTicksLeft = tag.getInteger(TAG_HUMUS_LEFT);
        timeToDie = tag.getInteger(TAG_DIE);
        timeToSpore = tag.getInteger(TAG_SPORE);
        timeToGrow = tag.getInteger(TAG_GROW);
        init();
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
        refreshFungusTemplate();
    }

    private void refreshFungusTemplate() {
        fungusTemplate = FungiCatalog.get(fungusType);
    }

    public void printDebugInfo(EntityPlayer player) {
        String msg = String.format("Type: %d (%s), State: %d", fungusType, mushroomPlanted() ? fungusTemplate.title : "none", fungusState);
        msg += String.format(" Time to: grow=%d, die=%d, humus=%d", timeToGrow, timeToDie, humusTicksLeft);
        msg += String.format(", spore=%d", timeToSpore);
        if (!player.worldObj.isRemote) {
            player.addChatMessage(msg);
        } else {
            JaffasFood.Log.printDebug("Client: " + msg);
        }
    }

    public boolean playerActivatedBox(EntityPlayer player) {
        if (player.isSneaking()) return false;

        if (MonnefCorePlugin.debugEnv) {
            if (PlayerHelper.playerHasEquipped(player, JaffasTechnic.jaffarrolDust.itemID)) {
                timeToGrow -= 20 * 60;
            } else if (PlayerHelper.playerHasEquipped(player, JaffasTechnic.limsew.itemID)) {
                timeToDie -= 20 * 60;
            } else if (PlayerHelper.playerHasEquipped(player, JaffasTechnic.jaffarrol.itemID)) {
                timeToSpore -= 20 * 60;
            } else if (PlayerHelper.playerHasEquipped(player, JaffasTechnic.swordJaffarrol.itemID)) {
                humusTicksLeft -= 20 * 60;
            }
        }

        return false;
    }

    public boolean isHumusActive() {
        return humusTicksLeft > 0;
    }
}
