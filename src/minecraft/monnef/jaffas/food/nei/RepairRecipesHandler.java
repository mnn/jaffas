/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import codechicken.nei.NEICompatibility;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import codechicken.nei.recipe.weakDependancy_Forge;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;

import java.util.List;

public class RepairRecipesHandler extends ShapelessRecipeHandler {
    @Override
    public String getRecipeName() {
        return "Jaffas Repair Recipes";
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if(outputId.equals("crafting") && getClass() == RepairRecipesHandler.class)
        {
            List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
            for(IRecipe irecipe : allrecipes)
            {
                CachedShapelessRecipe recipe = null;
                if(irecipe instanceof ShapelessRecipes)
                {
                    recipe = new CachedShapelessRecipe((ShapelessRecipes)irecipe);
                }

                if(recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for(IRecipe irecipe : allrecipes)
        {
            if(NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result))
            {
                CachedShapelessRecipe recipe = null;
                if(irecipe instanceof ShapelessRecipes)
                {
                    recipe = new CachedShapelessRecipe((ShapelessRecipes)irecipe);
                }
                else if(NEICompatibility.hasForge && weakDependancy_Forge.recipeInstanceShapeless(irecipe))
                {
                    recipe = weakDependancy_Forge.getShapelessRecipe(this, irecipe);
                }

                if(recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for(IRecipe irecipe : allrecipes)
        {
            CachedShapelessRecipe recipe = null;
            if(irecipe instanceof ShapelessRecipes)
            {
                recipe = new CachedShapelessRecipe((ShapelessRecipes)irecipe);
            }
            else if(NEICompatibility.hasForge && weakDependancy_Forge.recipeInstanceShapeless(irecipe))
            {
                recipe = weakDependancy_Forge.getShapelessRecipe(this, irecipe);
            }

            if(recipe == null)
                continue;

            if(recipe.contains(recipe.ingredients, ingredient))
            {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

}
