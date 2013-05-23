/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import monnef.jaffas.food.client.GuiBoard;
import monnef.jaffas.food.crafting.BoardRecipe;
import monnef.jaffas.food.crafting.Recipes;
import monnef.jaffas.food.crafting.RecipesBoard;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.item.ItemStack;

public class BoardRecipeHandler extends TemplateRecipeHandler {
    private static final int GLOBAL_Y = 24;

    public final int RESULT_X = 110;
    public final int RESULT_Y = GLOBAL_Y;

    public final int INPUT_X = 51;
    public final int INPUT_Y = GLOBAL_Y;

    public final int TOOL_X = 17;
    public final int TOOL_Y = GLOBAL_Y;

    @Override
    public String getGuiTexture() {
        return GuiBoard.GUIBOARD_TEXTURE;
    }

    @Override
    public String getRecipeName() {
        return "Kitchen Board";
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (BoardRecipe recipe : RecipesBoard.recipes.values()) {
            if (recipe.getOutput().itemID == result.itemID) {
                arecipes.add(new CachedBoardRecipe(recipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (BoardRecipe recipe : RecipesBoard.recipes.values()) {
            if (recipe.getInput().itemID == ingredient.itemID) {
                arecipes.add(new CachedBoardRecipe(recipe));
            }
        }
    }

    public class CachedBoardRecipe extends CachedRecipe {
        private final ItemStack input;
        private final ItemStack result;
        private final ItemStack toolSlot;

        public CachedBoardRecipe(ItemStack input, ItemStack result) {
            this.input = input;
            this.result = result;
            this.toolSlot = Recipes.getItemStackAnyDamage(JaffaItem.knifeKitchen);
        }

        public CachedBoardRecipe(BoardRecipe recipe) {
            this(recipe.getInput().copy(), recipe.getOutput().copy());
        }

        @Override
        public PositionedStack getResult() {
            return new PositionedStack(result, RESULT_X, RESULT_Y);
        }

        @Override
        public PositionedStack getIngredient() {
            return new PositionedStack(input, INPUT_X, INPUT_Y);
        }

        @Override
        public PositionedStack getOtherStack() {
            return new PositionedStack(toolSlot, TOOL_X, TOOL_Y);
        }
    }
}
