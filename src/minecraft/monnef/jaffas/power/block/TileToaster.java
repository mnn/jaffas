/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.power.api.IKitchenUnitAppliance;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.common.IProcessingRecipeHandler;
import monnef.jaffas.power.common.ProcessingRecipeHandler;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

import static monnef.jaffas.food.JaffasFood.getItem;

public class TileToaster extends TileEntityBasicProcessingMachine implements IKitchenUnitAppliance {
    public enum ToastLevel {
        MEDIUM
    }

    private static HashMap<ToastLevel, IProcessingRecipeHandler> recipes;

    static {
        recipes = new HashMap<ToastLevel, IProcessingRecipeHandler>();

        for (ToastLevel level : ToastLevel.values()) {
            recipes.put(level, new ProcessingRecipeHandler());
        }
    }

    private ToastLevel currentLevel = ToastLevel.MEDIUM;

    public static void addRecipe(ToastLevel level, JaffaItem input, JaffaItem output, int duration) {
        addRecipe(level, getItem(input).itemID, getItem(output).itemID, duration);
    }

    public static void addRecipe(ToastLevel level, int inputId, int outputId, int duration) {
        IProcessingRecipeHandler sub = recipes.get(level);
        sub.addRecipe(new ItemStack[]{new ItemStack(inputId, 1, 0)}, new ItemStack[]{new ItemStack(outputId, 1, 0)}, duration);
    }

    public TileToaster() {
        slowingCoefficient = 3;
    }

    @Override
    public IProcessingRecipeHandler getRecipeHandler() {
        return recipes.get(currentLevel);
    }

    @Override
    public String getInvName() {
        return "jaffas.power.toaster";
    }

    @Override
    public String getMachineTitle() {
        return "Toaster";
    }
}
