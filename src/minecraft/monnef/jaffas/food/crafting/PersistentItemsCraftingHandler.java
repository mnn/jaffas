/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;

import cpw.mods.fml.common.ICraftingHandler;
import monnef.core.utils.CraftingHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;

import static monnef.jaffas.food.JaffasFood.Log;

public class PersistentItemsCraftingHandler implements ICraftingHandler {

    private boolean debug = false;

    private static HashMap<Integer, PersistentItemInfo> persistentItems = new HashMap<Integer, PersistentItemInfo>();

    public PersistentItemsCraftingHandler() {
        debug = JaffasFood.debug;
    }

    public static PersistentItemInfo AddPersistentItem(int ID) {
        return AddPersistentItem(ID, false, -1);
    }

    public static PersistentItemInfo AddPersistentItem(JaffaItem item) {
        return AddPersistentItem(ItemManager.getItem(item).itemID);
    }

    public static PersistentItemInfo AddPersistentItem(JaffaItem item, boolean takesDamage, int substituteItem) {
        return AddPersistentItem(ItemManager.getItem(item).itemID, takesDamage, substituteItem);
    }

    public static PersistentItemInfo AddPersistentItem(int ID, boolean takesDamage, int substituteItem) {
        PersistentItemInfo info = new PersistentItemInfo(ID);
        if (takesDamage) info.SetDamageCopies();
        if (substituteItem > -1) info.SetSubstituteItem(substituteItem);

        persistentItems.put(ID, info);
        return info;
    }

    public static PersistentItemInfo AddPersistentItem(JaffaItem item, boolean takesDamage, JaffaItem substitude) {
        return AddPersistentItem(item, takesDamage, ItemManager.getItem(substitude).itemID);
    }

    @Override
    public void onCrafting(EntityPlayer player, ItemStack item,
                           IInventory craftMatrix) {
        handleRolls(craftMatrix);

        handlePersistentItems(craftMatrix, player);
    }

    private void handlePersistentItems(IInventory matrix, EntityPlayer player) {
        int ingredientsCount = 0;
        HashSet<Integer> processedSlots = new HashSet<Integer>(); // to not process newly added items (because they're result of some recipe)

        for (int i = 0; i < matrix.getSizeInventory(); i++) {
            if (matrix.getStackInSlot(i) != null && !processedSlots.contains(i)) {
                ingredientsCount++;
                ItemStack item = matrix.getStackInSlot(i);
                PersistentItemInfo info = persistentItems.get(item.itemID);
                if (info != null) {
                    if (info.damage) {
                        ItemStack newItem = item.copy();
                        newItem.stackSize++;
                        int newDamage = item.getItemDamage() + 1;

                        if (newDamage < item.getMaxDamage()) {
                            newItem.setItemDamage(newDamage);
                            matrix.setInventorySlotContents(i, newItem);
                        } else if (info.substituteItemID > -1) {
                            doSubstitution(matrix, processedSlots, info, player);
                        }
                    } else if (info.substituteItemID > -1) {
                        doSubstitution(matrix, processedSlots, info, player);
                    } else {
                        item.stackSize++;
                    }
                }
            }
        }
    }

    private void doSubstitution(IInventory matrix, HashSet<Integer> processedSlots, PersistentItemInfo info, EntityPlayer player) {
        CraftingHelper.returnLeftover(new ItemStack(info.substituteItemID, info.substituteItemsCount, 0), matrix, player, ConfigurationManager.transferItemsFromCraftingMatrix);
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {
    }

    private void handleRolls(IInventory matrix) {
        boolean foundPuff = false;
        int stickSlot = -1, ingredientsCount = 0;

        for (int i = 0; i < matrix.getSizeInventory(); i++) {
            if (matrix.getStackInSlot(i) != null) {
                ingredientsCount++;
                ItemStack item = matrix.getStackInSlot(i);
                if (item.itemID == ItemManager.getItem(JaffaItem.puffPastry).itemID) {
                    foundPuff = true;
                } else if (item.itemID == Item.stick.itemID) {
                    stickSlot = i;
                }
            }
        }

        if (foundPuff && ingredientsCount == 2 && stickSlot != -1) {
            if (debug) Log.printInfo("stickSlot ~ " + stickSlot);
            ItemStack stick = matrix.getStackInSlot(stickSlot);
            stick.stackSize++;
        }
    }
}
