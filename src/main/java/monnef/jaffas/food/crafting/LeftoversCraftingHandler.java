/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.crafting;

import monnef.core.common.MonnefCoreCraftingHandler;
import monnef.core.utils.CraftingHelper;
import monnef.core.utils.ItemHelper;
import monnef.core.utils.MathHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ConfigurationManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import scala.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LeftoversCraftingHandler extends MonnefCoreCraftingHandler {
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

        if (!JaffasCraftingHelper.doesCraftingTableSupportCraftingHandlers(craftMatrix.getClass().getName())) {
            // do nothing when dealing with broken crafter
            return;
        }

        Option<Integer> squareSizeOpt = MathHelper.getIntSquareRootJava(craftMatrix.getSizeInventory());
        if (!squareSizeOpt.isDefined()) {
            JaffasFood.Log.printFine(String.format("Ignoring non-square (%d) crafting matrix %s.", craftMatrix.getSizeInventory(), craftMatrix.getClass().getName()));
            return;
        }
        int squareSize = squareSizeOpt.get();

        int w = squareSize, h = squareSize;
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
            CraftingHelper.returnLeftovers(toGive, craftMatrix, player, ConfigurationManager.transferItemsFromCraftingMatrix);
        }
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {
    }
}
