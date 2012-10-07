package jaffas.common;

import cpw.mods.fml.common.ICraftingHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class JaffaCraftingHandler implements ICraftingHandler {

    private boolean debug = false;

    @Override
    public void onCrafting(EntityPlayer player, ItemStack item,
                           IInventory craftMatrix) {
        /*
        if (!Thread.currentThread().getName().equals("Server thread")) {
            if (debug) System.out.println("not server thread, skipping");
            return;
        }
        */

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

        HandleTin(craftMatrix);
        HandleRolls(craftMatrix);
    }

    private void HandleRolls(IInventory matrix) {
        boolean foundPuff = false;
        int stickSlot = -1, ingredientsCount = 0;

        for (int i = 0; i < matrix.getSizeInventory(); i++) {
            if (matrix.getStackInSlot(i) != null) {
                ingredientsCount++;
                ItemStack item = matrix.getStackInSlot(i);
                if (item.itemID == mod_jaffas.ItemsInfo.get(mod_jaffas.JaffaItem.puffPastry).getItem().shiftedIndex) {
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
                } else if (item.itemID == mod_jaffas.ItemsInfo.get(mod_jaffas.JaffaItem.browniesInTin).getItem().shiftedIndex) {
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
            ItemStack tin = new ItemStack(mod_jaffas.ItemsInfo.get(mod_jaffas.JaffaItem.cakeTin).getItem(), 2);
            matrix.setInventorySlotContents(freeSlot, tin);
        }
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {
        // TODO Auto-generated method stub

    }

}
