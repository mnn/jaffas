package codechicken.nei.recipe;

import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.forge.IContainerInputHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public class RecipeItemInputHandler implements IContainerInputHandler
{
    @Override
    public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyCode)
    {
        ItemStack stackover = gui.manager.getStackMouseOver();
        if(stackover == null)
            return false;
                
        if(keyCode == NEIClientConfig.getKeyBinding("usage") || (keyCode == NEIClientConfig.getKeyBinding("recipe") && NEIClientUtils.shiftKey()))
            return GuiUsageRecipe.openRecipeGui("item", stackover.copy());
        
        if(keyCode == NEIClientConfig.getKeyBinding("recipe"))
            return GuiCraftingRecipe.openRecipeGui("item", stackover.copy());
        
        return false;
    }

    @Override
    public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button)
    {
        ItemStack stackover = gui.manager.getStackMouseOver();
        if(stackover == null || !(gui instanceof GuiRecipe))
            return false;
        
        if(button == 0)
            return GuiCraftingRecipe.openRecipeGui("item", stackover.copy());

        if(button == 1)
            return GuiUsageRecipe.openRecipeGui("item", stackover.copy());
        
        return false;
    }

    @Override
    public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) 
    {
    }

    @Override
    public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) 
    {
    }

    @Override
    public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) 
    {
    }

    @Override
    public boolean keyTyped(GuiContainer gui, char keyChar, int keyID)
    {
        return false;
    }
    
    @Override
    public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled)
    {
        return false;
    }
    
    @Override
    public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled)
    {
    }
    
    @Override
    public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime)
    {
    }
}
