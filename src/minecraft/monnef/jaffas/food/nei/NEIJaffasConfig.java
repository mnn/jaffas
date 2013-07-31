/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import monnef.core.external.javassist.ClassPool;
import monnef.core.external.javassist.CtClass;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.power.block.common.ProcessingMachineRegistry;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.technic.client.CompostTooltip;
import monnef.jaffas.technic.client.FermenterTooltip;

import java.lang.reflect.Method;

public class NEIJaffasConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new BoardRecipeHandler());
        API.registerUsageHandler(new BoardRecipeHandler());
        GuiContainerManager.addTooltipHandler(new CompostTooltip());
        GuiContainerManager.addTooltipHandler(new FermenterTooltip());
        constructProcessingMachineHandlers();
    }

    private void constructProcessingMachineHandlers() {
        ClassPool pool = ClassPool.getDefault();
        int wrapperCounter = 0;
        for (Class<? extends TileEntityBasicProcessingMachine> clazz : ProcessingMachineRegistry.getTileClasses()) {
            try {
                TileEntityBasicProcessingMachine.enableDummyCreationPhase();
                TileEntityBasicProcessingMachine dummyTile = clazz.newInstance();
                TileEntityBasicProcessingMachine.disableDummyCreationPhase();

                /*
                ProcessingMachineRecipeHandler handler = new ProcessingMachineRecipeHandler(dummyTile);
                API.registerRecipeHandler(handler);
                API.registerUsageHandler(handler);
                */

                CtClass classCopy = pool.getAndRename(ProcessingMachineRecipeHandlerWrapper.class.getCanonicalName(), "monnef.jaffas.food.nei.wrapper.PMRH" + (wrapperCounter++));
                Class wrapperClass = classCopy.toClass();
                Method initMethod = wrapperClass.getDeclaredMethod("init", TileEntityBasicProcessingMachine.class);
                initMethod.invoke(null, dummyTile);
                API.registerRecipeHandler((ICraftingHandler) wrapperClass.newInstance());
                API.registerUsageHandler((IUsageHandler) wrapperClass.newInstance());
            } catch (Throwable e) {
                throw new RuntimeException("Problem in NEI processing machine handler construction.", e);
            }
        }
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
