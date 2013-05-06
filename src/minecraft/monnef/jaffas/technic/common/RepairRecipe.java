/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.technic.common;

import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RepairRecipe implements IRecipe {
    private int jaffarrolCount = 0;
    private int limsewCount = 0;
    private ItemStack itemToRepair;
    private List<ItemStack> moreItems;
    private int repairValue = 1;

    public RepairRecipe(int jaffarrolCount, int limsewCount, ItemStack itemToRepair, int repairValue, ItemStack... items) {
        this.jaffarrolCount = jaffarrolCount;
        this.limsewCount = limsewCount;
        this.itemToRepair = itemToRepair;
        this.moreItems = Arrays.asList(items);
        this.repairValue = repairValue;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        int jaffarrolLeft = jaffarrolCount;
        int limsewLeft = limsewCount;
        boolean toolFound = false;
        ArrayList<ItemStack> requiredItems = new ArrayList<ItemStack>(moreItems);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

                if (itemstack != null) {
                    boolean found = false;
                    Iterator iterator = requiredItems.iterator();

                    while (iterator.hasNext()) {
                        ItemStack suspectedItem = (ItemStack) iterator.next();

                        if (itemstack.itemID == suspectedItem.itemID && (suspectedItem.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemstack.getItemDamage() == suspectedItem.getItemDamage())) {
                            found = true;
                            requiredItems.remove(suspectedItem);
                            break;
                        }
                    }

                    if (!found && itemstack.itemID == JaffasTechnic.jaffarrolDust.itemID) {
                        found = true;
                        jaffarrolLeft--;
                    }

                    if (!found && itemstack.itemID == JaffasTechnic.limsew.itemID) {
                        found = true;
                        limsewLeft--;
                    }

                    if (!found && itemstack.itemID == itemToRepair.itemID) {
                        toolFound = true;
                        found = true;
                    }

                    if (!found) {
                        return false;
                    }
                }
            }
        }

        return requiredItems.isEmpty() && jaffarrolLeft == 0 && limsewLeft == 0 && toolFound;

    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack tool = null;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                if (itemstack != null) {
                    if (itemstack.itemID == itemToRepair.itemID) {
                        tool = itemstack;
                        break;
                    }
                }
            }
        }

        ItemStack output = tool.copy();
        int dmg = output.getItemDamage() - repairValue;
        output.setItemDamage(dmg < 0 ? 0 : dmg);
        return output;
    }

    @Override
    public int getRecipeSize() {
        return jaffarrolCount + limsewCount + 1 + moreItems.size();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return itemToRepair.copy();
    }
}
