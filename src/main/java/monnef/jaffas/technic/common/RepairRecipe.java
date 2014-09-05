/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.core.utils.ItemHelper;
import monnef.jaffas.food.item.ItemJaffaTool;
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

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack tool = null;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                if (itemstack != null) {
                    if (itemstack.getItem() == itemToRepair.getItem()) {
                        tool = itemstack;
                        break;
                    }
                }
            }
        }

        ItemStack output = tool.copy();
        int dmg = output.getItemDamage() - repairValue;
        output.setItemDamage(dmg < 0 ? 0 : dmg);
        ((ItemJaffaTool) output.getItem()).refreshDamageAttribute(output);
        return output;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return itemToRepair.copy();
    }
}
