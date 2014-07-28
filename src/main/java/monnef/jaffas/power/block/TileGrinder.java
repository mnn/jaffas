/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.core.common.ContainerRegistry;
import monnef.jaffas.power.api.IKitchenUnitAppliance;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.common.IProcessingRecipeHandler;
import monnef.jaffas.power.common.ProcessingRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

@ContainerRegistry.ContainerTag(slotsCount = 2, containerClassName = "monnef.jaffas.power.block.common.ContainerBasicProcessingMachine", guiClassName = "monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine")
public class TileGrinder extends TileEntityBasicProcessingMachine implements IKitchenUnitAppliance {
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
    public String getInventoryName() {
        return "jaffas.power.grinder";
    }

    public static IProcessingRecipeHandler getRecipeHandler() {
        return recipes;
    }
}
