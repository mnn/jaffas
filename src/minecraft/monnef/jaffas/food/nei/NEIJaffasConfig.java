/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.forge.GuiContainerManager;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.technic.client.CompostTooltip;
import monnef.jaffas.technic.client.FermenterTooltip;

public class NEIJaffasConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new BoardRecipeHandler());
        API.registerUsageHandler(new BoardRecipeHandler());
        GuiContainerManager.addTooltipHandler(new CompostTooltip());
        GuiContainerManager.addTooltipHandler(new FermenterTooltip());
    }

    @Override
    public String getName() {
        return Reference.ModName;
    }

    @Override
    public String getVersion() {
        return Reference.Version;
    }
}
