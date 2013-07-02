/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.common.IProcessingRecipeHandler;
import monnef.jaffas.power.common.SimpleProcessingRecipeHandler;
import net.minecraft.item.ItemStack;

public class TileEntityGrinder extends TileEntityBasicProcessingMachine {
    private static SimpleProcessingRecipeHandler recipes = new SimpleProcessingRecipeHandler();

    public static void addRecipe(ItemStack input, ItemStack output, int duration) {
        recipes.addRecipe(new ItemStack[]{input}, new ItemStack[]{output}, duration);
    }

    @Override
    public String getInvName() {
        return "jaffas.power.grinder";
    }

    @Override
    public String getMachineTitle() {
        return "Grinder";
    }

    @Override
    public IProcessingRecipeHandler getRecipeHandler() {
        return recipes;
    }
}
