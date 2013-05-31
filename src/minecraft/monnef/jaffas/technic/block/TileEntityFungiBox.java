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
    private static final String TAG_COMPOST_LEFT = "compostLeft";
    private static final String TAG_DIE = "timeToDie";
    private static final String TAG_SPORE = "timeToSpore";
    private static final String TAG_GROW = "timeToGrow";
    private static final int COMPOST_TICKS_CAN_FERTILIZE_THRESHOLD = 15 * 60 * 20;
    public static final int COMPOST_TICKS_PER_ONE_USE = 5 * 60 * 20;

    private byte mushroomRotation;
    private byte boxRotation;

    private int fungusType = 0; // zero = nothing here
    private int compostTicksLeft = 0; // zero = no humus
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

        counter++;
        boolean compute = (counter % tickQuantum == 0) || debugSpeedOverride;
        if (compute) {
            boolean update = false;

            compostTicksLeft -= (mushroomPlanted() ? fungusTemplate.compostConsumptionSpeed : 1) * tickQuantum;
            if (compostTicksLeft < 0) compostTicksLeft = 0;

            if (mushroomPlanted()) {
                if (canGrow()) {
                    timeToGrow -= (compostPresent() ? fungusTemplate.growMultiplierOfCompost : 1) * tickQuantum;
                    if (timeToGrow <= 0) {
                        if (!isMature()) {
                            update = true;
                            grow();
                        } else {
                            timeToGrow = 20 * 10;
                        }
                    }
                } // cannot grow
                else if (isMature() || (fungusTemplate.revertsWithoutCompost && fungusState > 0)) {
                    // is mature or cannot stay in this state without compost
                    timeToDie -= tickQuantum;
                    if (timeToDie <= 0) {
                        update = true;
                        revertWithDeathChance();
                    } else {
                        // not dead
                        if (isMature()) {
                            // only mature mushrooms can create spores
                            timeToSpore -= tickQuantum;
                            if (timeToSpore <= 0) {
                                doSporage();
                                setupNextSporeTime();
                            }
                        }
                    }
                }
            }
            if (update) {
                forceUpdate();
            }
        }
    }

    public void onFallUpon() {
        revertWithDeathChance();
        forceUpdate();
    }

    private void revertWithDeathChance() {
        if (RandomHelper.rollPercentBooleanDice(fungusTemplate.surviveRate)) {
            fungusState = 0;
            setupNextGrowTime();
        } else {
            fungusType = 0;
        }
    }

    private void grow() {
        fungusState++;
        setupNextGrowTime();
        timeToDie = fungusTemplate.timeToDie.getRandom();
        if (isMature()) {
            setupNextSporeTime();
        }
    }

    private boolean canGrow() {
        return !isMature() && (!fungusTemplate.stateNeedCompostToGrow[fungusState] || compostPresent());
    }

    public boolean compostPresent() {
        return compostTicksLeft > 0;
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
        tag.setInteger(TAG_COMPOST_LEFT, compostTicksLeft);
        tag.setInteger(TAG_DIE, timeToDie);
        tag.setInteger(TAG_SPORE, timeToSpore);
        tag.setInteger(TAG_GROW, timeToGrow);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        fungusType = tag.getInteger(TAG_FUNGUS_TYPE);
        fungusState = tag.getInteger(TAG_FUNGUS_STATE);
        compostTicksLeft = tag.getInteger(TAG_COMPOST_LEFT);
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

    public void forceUpdate() {
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
        msg += String.format(" Time to: grow=%d, die=%d, humus=%d", timeToGrow, timeToDie, compostTicksLeft);
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
                compostTicksLeft -= 20 * 60;
            }
        }

        if (playerHasEquipped(player, JaffasTechnic.mushroomKnife.itemID)) {
            if (harvest(player)) {
                return true;
            }
        } else if (playerHasEquipped(player, JaffasTechnic.compost.itemID)) {
            if (fertilize(player)) {
                return true;
            }
        } else if (tryPlant(player)) {
            return true;
        }

        return false;
    }

    private boolean fertilize(EntityPlayer player) {
        if (player != null) {
            ItemStack hand = player.getCurrentEquippedItem();
            if (hand == null || hand.itemID != JaffasTechnic.compost.itemID) return false;
            hand.stackSize--;
            if (hand.stackSize <= 0) player.setCurrentItemOrArmor(0, null);
        }
        boolean canFertilize = canFertilize();
        if (!worldObj.isRemote) {
            compostTicksLeft += COMPOST_TICKS_PER_ONE_USE;
            forceUpdate();
        }
        return canFertilize;
    }

    private boolean canFertilize() {
        return compostTicksLeft < COMPOST_TICKS_CAN_FERTILIZE_THRESHOLD;
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

        if (!worldObj.isRemote) {
            plant(template);
            forceUpdate();
        }
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

    public static void setDebugSpeedOverride(int speed) {
        tickQuantum = speed;
        debugSpeedOverride = true;
    }

    public static void disableDebugSpeedOverride() {
        debugSpeedOverride = false;
        tickQuantum = DEFAULT_QUANTUM_OF_TICKS;
    }
}
