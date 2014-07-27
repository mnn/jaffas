/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;

import static monnef.jaffas.food.JaffasFood.getItem;

public class TileKeg extends TileEntity {
    public static final int KEG_CAPACITY = 10;

    private static final String KEG_TYPE_TAG = "kegType";
    private static final String LIQUID_AMOUNT_TAG = "liquid";

    private KegType type;
    private int liquidAmount;

    private static HashMap<Item, KegVesselEntry> vessel;

    static {
        vessel = new HashMap<Item, KegVesselEntry>();

        addVessel(KegType.BEER, getItem(JaffaItem.beerMugEmpty), new ItemStack(getItem(JaffaItem.beerMugFull)));
    }

    public static KegVesselEntry addVessel(KegType validFor, Item item, ItemStack filledItem) {
        KegVesselEntry entry = new KegVesselEntry();
        entry.item = item;
        entry.validFor = validFor;
        entry.filledVessel = filledItem;
        vessel.put(item, entry);
        return entry;
    }

    public enum KegType {
        EMPTY("empty"),
        BEER("beer"),
        WINE("wine"),;

        private final String title;

        KegType(String title) {
            this.title = title;
        }

        KegType() {
            this.title = "NOT SET";
        }

        public String getTitle() {
            return this.title;
        }
    }

    public static boolean isValidVessel(ItemStack input) {
        if (input == null) return false;
        return vessel.containsKey(input);
    }

    public static KegVesselEntry getVessel(ItemStack input) {
        return vessel.get(input);
    }

    public boolean onBlockActivated(EntityPlayer player) {
        ItemStack hand = player.getCurrentEquippedItem();

        if (worldObj.isRemote) return isValidVessel(hand);

        if (hand == null) {
            printStatusMessage(player);
            return true;
        } else if (isValidVessel(hand)) {
            KegVesselEntry vessel = getVessel(hand);
            if (vessel.validFor != type) {
                return false;
            }
            if (type == KegType.EMPTY) {
                throw new RuntimeException("vessel record is valid for EMPTY keg, this is error.");
            }
            if (liquidAmount < vessel.capacity) {
                return false;
            }

            liquidAmount -= vessel.capacity;
            hand.stackSize--;
            PlayerHelper.giveItemToPlayer(player, vessel.filledVessel.copy());
            return true;
        } else {
            return false;
        }
    }

    private void printStatusMessage(EntityPlayer player) {
        String msg;
        if (liquidAmount == 0) msg = "Keg is empty.";
        else if (liquidAmount == KEG_CAPACITY) msg = String.format("Keg is full of %s.", type.getTitle());
        else if (liquidAmount >= KEG_CAPACITY / 2)
            msg = String.format("Keg is more than half-full of %s.", type.getTitle());
        else if (liquidAmount < KEG_CAPACITY / 2)
            msg = String.format("Keg is less than half-full of %s.", type.getTitle());
        else msg = "keg is broken? " + liquidAmount;

        if (MonnefCorePlugin.debugEnv) msg += " [" + liquidAmount + "]";

        PlayerHelper.addMessage(player, msg);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        type = KegType.values()[tag.getByte(KEG_TYPE_TAG)];
        liquidAmount = tag.getInteger(LIQUID_AMOUNT_TAG);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte(KEG_TYPE_TAG, (byte) type.ordinal());
        tag.setInteger(LIQUID_AMOUNT_TAG, liquidAmount);
    }

    public void initNewKeg(KegType newType) {
        liquidAmount = newType == KegType.EMPTY ? 0 : KEG_CAPACITY;
        type = newType;
    }

    private static class KegVesselEntry {
        public KegType validFor;
        public int capacity = 1;
        public Item item;
        public ItemStack filledVessel;
    }
}
