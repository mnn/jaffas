/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.utils.Interval;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;

public class TileMeatDryer extends TileEntity {
    public static final int MEAT_COUNT = 4;

    private static final int tickQuantum = 20;
    private static final int TPS = 20;
    private static final int NOTHING_PLANNED_TICKS = TPS * 60 * 5;
    private static final String MEAT_TAG = "meat";

    private int counter = 0;
    private MeatStatus[] meats = new MeatStatus[MEAT_COUNT];
    private static final HashMap<Integer, MeatState> itemIdToMeatType = new HashMap<Integer, MeatState>();
    private static final HashMap<MeatState, ItemStack> finishedMeatToItemStack = new HashMap<MeatState, ItemStack>();

    static {
        addNormalMeat(Item.beefRaw);
        addNormalMeat(Item.fishRaw);
        addNormalMeat(Item.porkRaw);
        addNormalMeat(Item.chickenRaw);
        addZombieMeat(Item.rottenFlesh);

        finishedMeatToItemStack.put(MeatState.NORMAL_DONE, Recipes.getItemStack(JaffaItem.driedMeat));
        finishedMeatToItemStack.put(MeatState.ZOMBIE_DONE, Recipes.getItemStack(JaffaItem.driedMeat));
    }

    private static void addItemIdToMeatTypeMapping(Item item, MeatState state) {
        if (item == null) {
            throw new RuntimeException("Item cannot be null, error occurred during meat registration in a drying rack TE.");
        }
        if (state == null) {
            throw new RuntimeException("State cannot be null, error occurred during meat registration in a drying rack TE.");
        }

        itemIdToMeatType.put(item.itemID, state);
    }

    public static void addNormalMeat(Item item) {
        addItemIdToMeatTypeMapping(item, MeatState.NORMAL_RAW);
    }

    public static void addZombieMeat(Item item) {
        addItemIdToMeatTypeMapping(item, MeatState.ZOMBIE_RAW);
    }

    public TileMeatDryer() {
        for (int i = 0; i < meats.length; i++) {
            meats[i] = new MeatStatus();
        }
    }

    private static int calcTicks(int seconds) {
        return seconds * TPS;
    }

    public enum MeatState {
        ZOMBIE_RAW(7, 10),
        ZOMBIE_HALF_DONE(5, 10),
        ZOMBIE_DONE,
        NORMAL_RAW(3, 6),
        NORMAL_HALF_DONE(4, 6),
        NORMAL_DONE,
        NO_MEAT;

        private final Interval duration;

        MeatState() {
            duration = null;
        }

        MeatState(int minMinutes, int maxMinutes) {
            this.duration = new Interval(calcTicks(minMinutes * 60), calcTicks(maxMinutes * 60));
        }

        public int getStateDurationRandomly() {
            return duration == null ? 0 : duration.getRandom();
        }

        public MeatState getNextStage() {
            switch (this) {
                case ZOMBIE_DONE:
                case NORMAL_DONE:
                case NO_MEAT:
                    return this;

                case ZOMBIE_RAW:
                    return ZOMBIE_HALF_DONE;

                case ZOMBIE_HALF_DONE:
                    return ZOMBIE_DONE;

                case NORMAL_RAW:
                    return NORMAL_HALF_DONE;

                case NORMAL_HALF_DONE:
                    return NORMAL_DONE;
            }

            throw new RuntimeException("unknown meat");
        }
    }

    private static class MeatStatus {
        private static final String MEAT_STATE_TAG = "meatState";
        private static final String TICKS_LEFT_TAG = "ticksLeft";
        private MeatState state;
        private int ticksLeft;

        public MeatStatus() {
            state = MeatState.NO_MEAT;
        }

        public void setNewMeat(MeatState newState) {
            if (newState != MeatState.ZOMBIE_RAW && newState != MeatState.NORMAL_RAW) {
                throw new RuntimeException("Incorrect parameter in newMeat");
            }
            if (state != MeatState.NO_MEAT) {
                throw new RuntimeException("Overriding meat");
            }

            state = newState;
            computeNewTicksLeft();
        }

        private void computeNewTicksLeft() {
            ticksLeft = state.getStateDurationRandomly();
            if (ticksLeft == 0) ticksLeft = NOTHING_PLANNED_TICKS;
        }

        // only on a server side
        public boolean tick() {
            ticksLeft -= tickQuantum;
            if (ticksLeft <= 0) {
                ticksLeft = 0;
                state = state.getNextStage();
                computeNewTicksLeft();

                return true;
            }
            return false;
        }

        public void saveToNBT(NBTTagCompound tag) {
            tag.setByte(MEAT_STATE_TAG, (byte) state.ordinal());
            tag.setInteger(TICKS_LEFT_TAG, ticksLeft);
        }

        public void loadFromNBT(NBTTagCompound tag) {
            state = MeatState.values()[tag.getByte(MEAT_STATE_TAG)];
            ticksLeft = tag.getInteger(TICKS_LEFT_TAG);
        }

        public boolean isFree() {
            return state == MeatState.NO_MEAT;
        }

        public boolean isOccupied() {
            return !isFree();
        }

        public boolean isFinished() {
            return state == MeatState.NORMAL_DONE || state == MeatState.ZOMBIE_DONE;
        }

        public ItemStack harvest() {
            if (!isFinished()) {
                throw new RuntimeException("harvesting not finished meat");
            }

            MeatState oldState = state;
            state = MeatState.NO_MEAT;
            computeNewTicksLeft();
            return finishedMeatToItemStack.get(oldState).copy();
        }

        @Override
        public String toString() {
            return state.toString() + "(" + ticksLeft + ")";
        }
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;

        counter++;
        if (counter % tickQuantum == 0) {
            tickMeats();
        }
    }

    private void tickMeats() {
        boolean dirty = false;
        for (MeatStatus meat : meats) {
            dirty |= meat.tick();
        }

        if (dirty) {
            forceUpdate();
        }
    }

    public MeatState getCurrentMeatState(int meatNumber) {
        return meats[meatNumber].state;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        for (int i = 0; i < meats.length; i++) {
            NBTTagCompound comp = tag.getCompoundTag(MEAT_TAG + i);
            meats[i].loadFromNBT(comp);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        for (int i = 0; i < meats.length; i++) {
            NBTTagCompound comp = new NBTTagCompound();
            meats[i].saveToNBT(comp);
            tag.setCompoundTag(MEAT_TAG + i, comp);
        }
    }

    public boolean tryPlaceMeat(ItemStack input, boolean decreaseStackSize) {
        if (!hasFreeSpace()) return false;
        MeatState newMeat = getMeatStateFromStack(input);
        if (newMeat == null) return false; // wrong item
        int pos = getRandomFreePosition();
        meats[pos].setNewMeat(newMeat);
        forceUpdate();
        if (decreaseStackSize) input.stackSize--;
        return true;
    }

    private int getRandomFreePosition() {
        int r = JaffasFood.rand.nextInt(MEAT_COUNT);
        int current = getIndexOfNextFreeSpot(JaffasFood.rand.nextInt(MEAT_COUNT));
        for (int i = 0; i < r; i++) {
            current++;
            if (current >= meats.length) current = 0;
            current = getIndexOfNextFreeSpot(current);
        }
        return current;
    }

    // only call when there is at least one valid result!
    private int getIndexOfNextFreeSpot(int index) {
        while (meats[index].isOccupied()) {
            index++;
            if (index >= meats.length) index = 0;
        }
        return index;
    }

    private MeatState getMeatStateFromStack(ItemStack stack) {
        if (stack == null) return null;
        return itemIdToMeatType.get(stack.itemID);
    }

    public boolean isStackValidInput(ItemStack stack) {
        return getMeatStateFromStack(stack) != null;
    }

    public boolean hasFreeSpace() {
        for (int i = 0; i < meats.length; i++) {
            if (meats[i].isFree()) return true;
        }
        return false;
    }

    public boolean hasFinishedMeat() {
        for (int i = 0; i < meats.length; i++) {
            if (meats[i].isFinished()) return true;
        }
        return false;
    }

    public ItemStack harvestMeat() {
        if (!hasFinishedMeat()) return null;
        int toHarvest = getRandomFinishedMeatPosition();
        ItemStack result = meats[toHarvest].harvest();
        forceUpdate();
        return result;
    }

    private int getRandomFinishedMeatPosition() {
        int r = JaffasFood.rand.nextInt(MEAT_COUNT);
        int current = getIndexOfNextFinishedMeat(JaffasFood.rand.nextInt(MEAT_COUNT));
        for (int i = 0; i < r; i++) {
            current++;
            if (current >= meats.length) current = 0;
            current = getIndexOfNextFinishedMeat(current);
        }
        return current;
    }

    // only call when there is at least one valid result!
    private int getIndexOfNextFinishedMeat(int index) {
        while (!meats[index].isFinished()) {
            index++;
            if (index >= meats.length) index = 0;
        }
        return index;
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

    public void forceUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void printDebugInfo(EntityPlayer player) {
        String m = "";
        for (int i = 0; i < meats.length; i++) {
            m += meats[i].toString() + " ";
        }
        player.addChatMessage(m);
    }
}
