/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.core.common.ContainerRegistry;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.power.api.IKitchenUnitAppliance;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.common.IProcessingRecipeHandler;
import monnef.jaffas.power.common.ProcessingRecipeHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

import static monnef.jaffas.food.JaffasFood.getItem;

@ContainerRegistry.ContainerTag(slotsCount = 2, containerClassName = "monnef.jaffas.power.block.common.ContainerBasicProcessingMachine", guiClassName = "monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine")
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
        addRecipe(level, getItem(input), getItem(output), duration);
    }

    public static void addRecipe(ToastLevel level, Item input, Item output, int duration) {
        IProcessingRecipeHandler sub = recipes.get(level);
        sub.addRecipe(new ItemStack[]{new ItemStack(input, 1, 0)}, new ItemStack[]{new ItemStack(output, 1, 0)}, duration);
    }

    public TileToaster() {
        slowingCoefficient = 3;
    }

    public static IProcessingRecipeHandler getRecipeHandler() {
        return recipes.get(ToastLevel.MEDIUM); // TODO: more levels
    }

    @Override
    public String getInventoryName() {
        return "jaffas.power.toaster";
    }
}
