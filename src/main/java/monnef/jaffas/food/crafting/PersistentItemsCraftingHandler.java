/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;

import monnef.core.common.MonnefCoreCraftingHandler;
import monnef.core.utils.CraftingHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import scala.Option;

import java.util.HashMap;
import java.util.HashSet;

import static monnef.jaffas.food.JaffasFood.Log;

// TODO: fix method names - remove starting capitals
public class PersistentItemsCraftingHandler extends MonnefCoreCraftingHandler {

    private boolean debug = false;

    private static HashMap<Item, PersistentItemInfo> persistentItems = new HashMap<Item, PersistentItemInfo>();

    public PersistentItemsCraftingHandler() {
        debug = JaffasFood.debug;
    }

    public static PersistentItemInfo AddPersistentItem(Item item) {
        return AddPersistentItem(item, false, null);
    }

    public static PersistentItemInfo AddPersistentItem(JaffaItem item) {
        return AddPersistentItem(ItemManager.getItem(item));
    }

    public static PersistentItemInfo AddPersistentItemWhichTakesDamage(JaffaItem item) {
        return AddPersistentItem(ItemManager.getItem(item), true, null);
    }

    public static PersistentItemInfo AddPersistentItemRetuningNonJaffaItem(JaffaItem item, boolean takesDamage, Item substituteItem) {
        return AddPersistentItem(ItemManager.getItem(item), takesDamage, substituteItem);
    }

    public static PersistentItemInfo AddPersistentItem(Item item, boolean takesDamage, Item substituteItem) {
        PersistentItemInfo info = new PersistentItemInfo(item);
        if (takesDamage) info.setDamageCopies();
        info.setSubstituteItem(substituteItem);

        persistentItems.put(item, info);
        return info;
    }

    public static PersistentItemInfo AddPersistentItem(JaffaItem item, boolean takesDamage, JaffaItem substitude) {
        return AddPersistentItemRetuningNonJaffaItem(item, takesDamage, ItemManager.getItem(substitude));
    }

    @Override
    public void onCrafting(EntityPlayer player, ItemStack item,
                           IInventory craftMatrix) {
        if (!JaffasCraftingHelper.doesCraftingTableSupportCraftingHandlers(craftMatrix.getClass().getName())) {
            // do nothing when dealing with broken crafter
            return;
        }

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
                PersistentItemInfo info = persistentItems.get(item);
                if (info != null) {
                    if (info.damage) {
                        ItemStack newItem = item.copy();
                        newItem.stackSize++;
                        int newDamage = item.getItemDamage() + 1;

                        if (newDamage < item.getMaxDamage()) {
                            newItem.setItemDamage(newDamage);
                            matrix.setInventorySlotContents(i, newItem);
                        } else if (info.substituteItem != null) {
                            doSubstitution(matrix, processedSlots, info, player);
                        }
                    } else if (info.substituteItem != null) {
                        doSubstitution(matrix, processedSlots, info, player);
                    } else {
                        item.stackSize++;
                    }
                }
            }
        }
    }

    private void doSubstitution(IInventory matrix, HashSet<Integer> processedSlots, PersistentItemInfo info, EntityPlayer player) {
        Option<Object> ret = CraftingHelper.returnLeftover(new ItemStack(info.substituteItem, info.substituteItemsCount, 0), matrix, player, ConfigurationManager.transferItemsFromCraftingMatrix);
        if (ret.isDefined()) processedSlots.add((Integer) ret.get());
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
                ItemStack stack = matrix.getStackInSlot(i);
                if (stack.getItem() == ItemManager.getItem(JaffaItem.puffPastry)) {
                    foundPuff = true;
                } else if (stack.getItem() == Items.stick) { // TODO: accept any stick (oredict)
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
