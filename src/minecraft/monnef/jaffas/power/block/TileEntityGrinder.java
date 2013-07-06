/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.common.IProcessingRecipeHandler;
import monnef.jaffas.power.common.ProcessingRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class TileEntityGrinder extends TileEntityBasicProcessingMachine {
    private static ProcessingRecipeHandler recipes = new ProcessingRecipeHandler();

    public static void addRecipe(ItemStack input, ItemStack output, int duration) {
        recipes.addRecipe(new ItemStack[]{input}, new ItemStack[]{output}, duration);
    }

    public static void addOreDictRecipe(String oreDictId, ItemStack output, int duration) {
        ArrayList<ItemStack> found = OreDictionary.getOres(oreDictId);
        for (ItemStack stack : found) {
            addRecipe(stack, output.copy(), duration);
        }
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
