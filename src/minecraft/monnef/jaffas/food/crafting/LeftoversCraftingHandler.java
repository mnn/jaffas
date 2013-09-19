/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;

import cpw.mods.fml.common.ICraftingHandler;
import monnef.core.utils.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LeftoversCraftingHandler implements ICraftingHandler {
    public static boolean disabled = false;

    private static Map<IRecipe, List<ItemStack>> db = new LinkedHashMap<IRecipe, List<ItemStack>>();

    public static void registerLeftovers(ItemStack recipeOutput, ItemStack... items) {
        if (disabled) return;
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        List<IRecipe> matching = new ArrayList<IRecipe>();
        for (IRecipe recipe : recipes) {
            if (ItemHelper.haveStacksSameIdAndDamage(recipe.getRecipeOutput(), recipeOutput))
                matching.add(recipe);
        }
        if (matching.size() > 1) throw new RuntimeException("Multiple matching recipes.");
        if (matching.size() < 1) throw new RuntimeException("No recipe found.");
        registerLeftovers(matching.get(0), items);
    }

    public static void registerLeftovers(IRecipe recipe, ItemStack... items) {
        if (disabled) return;
        if (recipe == null) throw new RuntimeException("Null recipe!");
        if (items.length > 9) throw new RuntimeException("Too many items to return for any recipe.");
        int inputItemsCount = -1;
        if (recipe instanceof ShapelessRecipes) {
            inputItemsCount = ((ShapelessRecipes) recipe).getRecipeSize();
        } else if (recipe instanceof ShapedRecipes) {
            inputItemsCount = 0;
            for (ItemStack i : ((ShapedRecipes) recipe).recipeItems)
                if (i == null) inputItemsCount++;
        }
        if (inputItemsCount != -1 && 9 - inputItemsCount < items.length)
            throw new RuntimeException("Too many output items. It can never fit the crafting matrix.");

        db.put(recipe, Arrays.asList(items));
    }

    @Override
    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
        if (disabled) return;
        Container c = new HelperCraftingContainer(craftMatrix);
        int w = 3, h = 3;
        if (craftMatrix instanceof ContainerPlayer) {
            w = h = 2;
        }
        InventoryCrafting invCraft = new InventoryCrafting(c, w, h);
        for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
            ItemStack stack = craftMatrix.getStackInSlot(i);
            if (stack != null) invCraft.setInventorySlotContents(i, stack.copy());
        }

        ArrayList<IRecipe> matchingRecipes = new ArrayList<IRecipe>();
        for (IRecipe recipe : db.keySet())
            if (recipe.matches(invCraft, player.worldObj))
                matchingRecipes.add(recipe);

        if (matchingRecipes.size() > 1) throw new RuntimeException("Multiple matching recipes.");
        if (matchingRecipes.size() == 1) {
            List<ItemStack> toGive = db.get(matchingRecipes.get(0));
            for (ItemStack stack : toGive) {
                int freeSlot = PersistentItemsCraftingHandler.getFreeSlot(craftMatrix);
                if (freeSlot == -1) throw new RuntimeException("No free slot for leftovers.");
                ItemStack newStack = stack.copy();
                newStack.stackSize++;
                craftMatrix.setInventorySlotContents(freeSlot, newStack);
            }
        }
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {
    }
}
