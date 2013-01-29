package monnef.jaffas.food.crafting;

import cpw.mods.fml.common.ICraftingHandler;
import monnef.core.PlayerHelper;
import monnef.jaffas.food.item.ItemManager;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;

public class JaffaCraftingHandler implements ICraftingHandler {

    private boolean debug = false;

    private static HashMap<Integer, PersistentItemInfo> persistentItems = new HashMap<Integer, PersistentItemInfo>();

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
        if (player == null || !mod_jaffas.transferItemsFromCraftingMatrix) {

            int slot = getFreeSlot(matrix);
            if (slot < 0 || slot >= matrix.getSizeInventory()) {
                throw new RuntimeException("No space for recipe output - corrupt recipe?");
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

    private void HandleMallets(IInventory craftMatrix) {
        MalletHelper recipeTestMallet = MalletHelper.findMallet(craftMatrix);

        if (debug) System.out.println("craft from " + Thread.currentThread().getName());

        if (recipeTestMallet != null) {
            if (debug) System.out.println("mallet detected");
            ItemStack mallet = recipeTestMallet.getMallet();
            int position = recipeTestMallet.getPosition();
            ItemStack newTool = new ItemStack(mallet.getItem(), 2);

            int damage;

            if (recipeTestMallet.getIngredientsCount() == 9) {
                //got automation recipe - hurt mallet really bad
                damage = 8;
            } else if (recipeTestMallet.getIngredientsCount() == 2) {
                //simple recipe, minor damage
                damage = 1;
            } else {
                throw new Error("Unknown ingredients count (" + recipeTestMallet.getIngredientsCount() + "), something is terribly broken.");
            }

            int newDamage = mallet.getItemDamage() + damage;
            if (debug)
                System.out.println("damage ~ " + damage + ", newDmg ~ " + newDamage + ", newTool.maxDmg ~ " + newTool.getMaxDamage());
            newTool.setItemDamage(newDamage);

            if (newDamage < newTool.getMaxDamage()) {
                if (debug) System.out.println("adding new mallets");
                craftMatrix.setInventorySlotContents(position, newTool);
            }
        }
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
            if (debug) System.out.println("stickSlot ~ " + stickSlot);
            ItemStack stick = matrix.getStackInSlot(stickSlot);
            stick.stackSize++;
        }
    }

    private void HandleTin(IInventory matrix) {
        int freeSlot = -1, swordSlot = -1, ingredientsCount = 0;
        boolean tinFound = false;
        for (int i = 0; i < matrix.getSizeInventory(); i++) {
            if (matrix.getStackInSlot(i) != null) {
                ingredientsCount++;
                ItemStack item = matrix.getStackInSlot(i);
                if (item.itemID == Item.swordSteel.shiftedIndex || item.itemID == Item.swordDiamond.shiftedIndex) {
                    swordSlot = i;
                } else if (item.itemID == ItemManager.getItem(JaffaItem.browniesInTin).shiftedIndex) {
                    tinFound = true;
                }
            } else {
                freeSlot = i;
            }
        }

        if (ingredientsCount == 2 && tinFound && swordSlot != -1 && freeSlot != -1) {
            if (debug) System.out.println("freeSlot ~ " + freeSlot + ", swordSlot ~ " + swordSlot);

            ItemStack sword = matrix.getStackInSlot(swordSlot);
            sword.stackSize = 2; // 1 sword will be consume, other returned

            // return empty cake tin
            ItemStack tin = new ItemStack(ItemManager.getItem(JaffaItem.cakeTin), 2);
            matrix.setInventorySlotContents(freeSlot, tin);
        }
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {
    }
}
