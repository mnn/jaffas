package monnef.jaffas.food.crafting;

import cpw.mods.fml.common.ICraftingHandler;
import monnef.core.PlayerHelper;
import monnef.jaffas.food.item.ItemManager;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;

import static monnef.jaffas.food.mod_jaffas_food.Log;

public class JaffaCraftingHandler implements ICraftingHandler {

    private boolean debug = false;

    private static HashMap<Integer, PersistentItemInfo> persistentItems = new HashMap<Integer, PersistentItemInfo>();

    public JaffaCraftingHandler() {
        debug = mod_jaffas_food.debug;
    }

    public static PersistentItemInfo AddPersistentItem(int ID) {
        return AddPersistentItem(ID, false, -1);
    }

    public static PersistentItemInfo AddPersistentItem(JaffaItem item) {
        return AddPersistentItem(ItemManager.getItem(item).shiftedIndex);
    }

    public static PersistentItemInfo AddPersistentItem(JaffaItem item, boolean takesDamage, int substituteItem) {
        return AddPersistentItem(ItemManager.getItem(item).shiftedIndex, takesDamage, substituteItem);
    }

    public static PersistentItemInfo AddPersistentItem(int ID, boolean takesDamage, int substituteItem) {
        PersistentItemInfo info = new PersistentItemInfo(ID);
        if (takesDamage) info.SetDamageCopies();
        if (substituteItem > -1) info.SetSubstituteItem(substituteItem);

        persistentItems.put(ID, info);
        return info;
    }

    public static PersistentItemInfo AddPersistentItem(JaffaItem item, boolean takesDamage, JaffaItem substitude) {
        return AddPersistentItem(item, takesDamage, ItemManager.getItem(substitude).shiftedIndex);
    }

    @Override
    public void onCrafting(EntityPlayer player, ItemStack item,
                           IInventory craftMatrix) {
        //HandleMallets(craftMatrix);

        //HandleTin(craftMatrix);
        HandleRolls(craftMatrix);

        HandlePersistentItems(craftMatrix, player);
    }

    private void HandlePersistentItems(IInventory matrix, EntityPlayer player) {
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
        if (player == null || !mod_jaffas_food.transferItemsFromCraftingMatrix) {
            // "fix" to not crash/return more on BuildCraft's tables...
            String inventoryClassName = matrix.getClass().getName();
            if (mod_jaffas_food.ignoreBuildCraftsTables) {
                if (inventoryClassName.contains("TileAssemblyAdvancedWorkbench")
                        || inventoryClassName.contains("TileAutoWorkbench")) {
                    return;
                }
            }

            int slot = getFreeSlot(matrix);
            if (slot < 0 || slot >= matrix.getSizeInventory()) {
                throw new RuntimeException("No space for recipe output - corrupt recipe?");
            }

            if (debug) {
                Log.printInfo("name of inventory of matrix is: " + inventoryClassName);
            }

            matrix.setInventorySlotContents(slot, new ItemStack(info.substituteItemID, 1 + info.substituteItemsCount, 0)); // +1 because one will be consumed (hmm)
            processedSlots.add(slot);
        } else {
            ItemStack stack = new ItemStack(info.substituteItemID, info.substituteItemsCount, 0);
            if (!player.worldObj.isRemote) {
                PlayerHelper.giveItemToPlayer(player, stack);
            }
        }

    }

    public static int getFreeSlot(IInventory matrix) {
        for (int i = 0; i < matrix.getSizeInventory(); i++)
            if (matrix.getStackInSlot(i) == null)
                return i;

        return -1;
    }

    private void HandleRolls(IInventory matrix) {
        boolean foundPuff = false;
        int stickSlot = -1, ingredientsCount = 0;

        for (int i = 0; i < matrix.getSizeInventory(); i++) {
            if (matrix.getStackInSlot(i) != null) {
                ingredientsCount++;
                ItemStack item = matrix.getStackInSlot(i);
                if (item.itemID == ItemManager.getItem(JaffaItem.puffPastry).shiftedIndex) {
                    foundPuff = true;
                } else if (item.itemID == Item.stick.shiftedIndex) {
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

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {
    }
}
