/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.PlayerHelper;
import monnef.core.utils.RandomHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.common.FungiCatalog;
import monnef.jaffas.technic.common.FungusInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static monnef.core.utils.PlayerHelper.playerHasEquipped;
import static monnef.core.utils.RandomHelper.rand;

public class TileEntityFungiBox extends TileEntity {
    private static final String TAG_FUNGUS_TYPE = "fungusType";
    private static final String TAG_FUNGUS_STATE = "fungusState";
    private static final String TAG_HUMUS_LEFT = "humusLeft";
    private static final String TAG_DIE = "timeToDie";
    private static final String TAG_SPORE = "timeToSpore";
    private static final String TAG_GROW = "timeToGrow";

    private byte mushroomRotation;
    private byte boxRotation;

    private int fungusType = 0; // zero = nothing here
    private int humusTicksLeft = 0; // zero = no humus
    private int fungusState = 0;
    private int timeToDie;
    private int timeToSpore;
    private int timeToGrow;

    private int counter = rand.nextInt(20 * 60);

    private FungusInfo fungusTemplate;

    public static final int DEFAULT_QUANTUM_OF_TICKS = 20;
    public static int tickQuantum = DEFAULT_QUANTUM_OF_TICKS;
    private static boolean debugSpeedOverride = false;

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
        boolean compute = (counter % tickQuantum == 0) || debugSpeedOverride;
        if (compute) {
            boolean update = false;

            humusTicksLeft -= tickQuantum;
            if (humusTicksLeft < 0) humusTicksLeft = 0;

            if (mushroomPlanted()) {
                timeToGrow -= tickQuantum;
                if (timeToGrow <= 0) {
                    if (!isMature()) {
                        fungusState++;
                        setupNextGrowTime();
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
                        if (RandomHelper.rollPercentBooleanDice(fungusTemplate.surviveRate)) {
                            fungusState = 0;
                            setupNextGrowTime();
                        } else {
                            fungusType = 0;
                        }
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

    private void setupNextGrowTime() {
        timeToGrow = fungusTemplate.stateLength[fungusState].getRandom();
    }

    private static int[][] eightNeighbour = new int[][]{
            new int[]{-1, -1},
            new int[]{-1, 0},
            new int[]{-1, 1},
            new int[]{1, -1},
            new int[]{1, 0},
            new int[]{1, 1},
            new int[]{0, -1},
            new int[]{0, 1},
    };

    private void doSporage() {
        if (!worldObj.isRemote) {
            boolean found = false;
            TileEntityFungiBox neighbour = null;

            for (int i = 0; i < fungusTemplate.sporeTries; i++) {
                int randomNeighbour = rand.nextInt(8);
                int sx = eightNeighbour[randomNeighbour][0];
                int sz = eightNeighbour[randomNeighbour][1];
                TileEntity tile = worldObj.getBlockTileEntity(xCoord + sx, yCoord, zCoord + sz);
                if (tile == null || !(tile instanceof TileEntityFungiBox)) continue;
                neighbour = (TileEntityFungiBox) tile;
                if (!neighbour.canPlant()) continue;
                found = true;
                break;
            }

            if (found) {
                neighbour.plant(fungusTemplate);
                neighbour.forceUpdate();
            } else {

            }
        }
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

    public byte getRenderRotationMushroom() {
        return mushroomRotation;
    }

    public byte getRenderRotationBox() {
        return boxRotation;
    }

    private void forceUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void init() {
        mushroomRotation = (byte) ((22457 + xCoord * 7775213 + yCoord - zCoord * 22177) % 4);
        boxRotation = (byte) ((5778921 + xCoord * 77213 + yCoord * 227144571 - zCoord * 122177) % 4);
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
            if (playerHasEquipped(player, JaffasTechnic.jaffarrolDust.itemID)) {
                timeToGrow -= 20 * 60;
            } else if (playerHasEquipped(player, JaffasTechnic.limsew.itemID)) {
                timeToDie -= 20 * 60;
            } else if (playerHasEquipped(player, JaffasTechnic.jaffarrol.itemID)) {
                timeToSpore -= 20 * 60;
            } else if (playerHasEquipped(player, JaffasTechnic.swordJaffarrol.itemID)) {
                humusTicksLeft -= 20 * 60;
            }
        }

        if (playerHasEquipped(player, JaffasTechnic.mushroomKnife.itemID)) {
            if (harvest(player)) {
                return true;
            }
        }

        if (tryPlant(player)) {
            return true;
        }

        return false;
    }

    private boolean tryPlant(EntityPlayer player) {
        ItemStack hand = player.getCurrentEquippedItem();
        if (hand == null) return false;
        if (tryPlant(hand)) {
            if (!worldObj.isRemote) {
                hand.stackSize--;
                if (hand.stackSize <= 0) {
                    player.setCurrentItemOrArmor(0, null);
                }
            }
            return true;
        }

        return false;
    }

    private boolean tryPlant(ItemStack stack) {
        if (!canPlant()) {
            return false;
        }

        FungusInfo template = FungiCatalog.findByDrop(stack);
        if (template == null) {
            return false;
        }

        plant(template);
        return true;
    }

    private void plant(FungusInfo template) {
        fungusType = template.id;
        fungusState = 0;
        timeToGrow = template.stateLength[0].getRandom();
        refreshFungusTemplate();
    }

    private boolean canPlant() {
        return !mushroomPlanted();
    }

    private boolean harvest(EntityPlayer player) {
        if (mushroomPlanted() && isMature()) {
            if (!worldObj.isRemote) {
                ItemStack loot = fungusTemplate.createLoot();
                if (player != null) {
                    PlayerHelper.giveItemToPlayer(player, loot);
                } else {
                    //TODO: machine harvesting
                    throw new NotImplementedException();
                }

                fungusState = 0;
                setupNextGrowTime();
                forceUpdate();
            }
            return true;
        }

        return false;
    }

    public boolean isHumusActive() {
        return humusTicksLeft > 0;
    }

    public static void setDebugSpeedOverride(int speed) {
        tickQuantum = speed;
        debugSpeedOverride = true;
    }

    public static void disableDebugSpeedOverride() {
        debugSpeedOverride = false;
        tickQuantum = DEFAULT_QUANTUM_OF_TICKS;
    }
}
