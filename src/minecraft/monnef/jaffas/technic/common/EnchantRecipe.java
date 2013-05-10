/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class EnchantRecipe extends ShapelessRecipes {
    private static final String ENCH_TAG = "ench";

    private final ItemStack toEnchantItem;
    private final ItemStack enchantBy;
    private final List<ItemStack> moreResources;

    public EnchantRecipe(ItemStack toEnchant, ItemStack enchantBy, int jaffarrolDustCount, int limsewDustCount) {
        this(toEnchant, enchantBy, createResourceList(jaffarrolDustCount, limsewDustCount));
    }

    public EnchantRecipe(ItemStack toEnchant, ItemStack enchantBy, List<ItemStack> moreResources) {
        super(toEnchant, appendList(moreResources, enchantBy, toEnchant));

        this.toEnchantItem = toEnchant;
        this.enchantBy = enchantBy;
        this.moreResources = moreResources;
    }

    private static List<ItemStack> createResourceList(int jaffarrolDustCount, int limsewDustCount) {
        ArrayList<ItemStack> res = new ArrayList<ItemStack>();
        for (int i = 0; i < jaffarrolDustCount; i++) res.add(new ItemStack(JaffasTechnic.jaffarrolDust));
        for (int i = 0; i < limsewDustCount; i++) res.add(new ItemStack(JaffasTechnic.limsew));
        return res;
    }

    private static List appendList(List list, Object... stuff) {
        ArrayList res = new ArrayList(list);
        for (int i = 0; i < stuff.length; i++) res.add(stuff[i]);
        return res;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack enchantedBy = null;
        ItemStack inputItem = null;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                if (itemstack != null) {
                    if (itemstack.itemID == enchantBy.itemID) {
                        enchantedBy = itemstack;
                    }
                    if (itemstack.itemID == toEnchantItem.itemID) {
                        inputItem = itemstack;
                    }
                }
            }
        }

        ItemStack output = inputItem.copy();
        if (output.getTagCompound() == null) output.setTagCompound(new NBTTagCompound());
        output.getTagCompound().setTag(ENCH_TAG, enchantedBy.getEnchantmentTagList().copy());
        return output;
    }
}
