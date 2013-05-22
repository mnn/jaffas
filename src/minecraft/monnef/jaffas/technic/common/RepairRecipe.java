/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.core.utils.ItemHelper;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

public class RepairRecipe extends ShapelessRecipes {
    private int jaffarrolCount = 0;
    private int limsewCount = 0;
    private ItemStack itemToRepair;
    private List<ItemStack> moreItems;
    private int repairValue = 1;

    public RepairRecipe(int jaffarrolCount, int limsewCount, ItemStack itemToRepair, int repairValue, ItemStack... items) {
        super(itemToRepair.copy(), constructInputList(jaffarrolCount, limsewCount, itemToRepair, items));
        this.jaffarrolCount = jaffarrolCount;
        this.limsewCount = limsewCount;
        this.itemToRepair = itemToRepair;
        this.moreItems = Arrays.asList(items);
        this.repairValue = repairValue;
    }

    private static List<ItemStack> constructInputList(int jaffarrolCount, int limsewCount, ItemStack itemToRepair, ItemStack[] items) {
        List<ItemStack> res = new ArrayList<ItemStack>();
        ItemHelper.insertStackMultipleTimes(res, new ItemStack(JaffasTechnic.jaffarrolDust), jaffarrolCount);
        ItemHelper.insertStackMultipleTimes(res, new ItemStack(JaffasTechnic.limsew), limsewCount);
        ItemStack outputItem = itemToRepair.copy();
        outputItem.setItemDamage(WILDCARD_VALUE);
        res.add(outputItem);
        for (ItemStack item : items) {
            res.add(item.copy());
        }
        return res;
    }

    /*
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
    */

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

    /*
    @Override
    public int getRecipeSize() {
        return jaffarrolCount + limsewCount + 1 + moreItems.size();
    }
    */

    @Override
    public ItemStack getRecipeOutput() {
        return itemToRepair.copy();
    }
}
