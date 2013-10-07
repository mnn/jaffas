/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import monnef.core.external.javassist.ClassPool;
import monnef.core.external.javassist.CtClass;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.power.block.common.ProcessingMachineRegistry;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;

import java.lang.reflect.Method;

public class NEIJaffasConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new BoardRecipeHandler());
        API.registerUsageHandler(new BoardRecipeHandler());
        constructProcessingMachineHandlers();
    }

    private void constructProcessingMachineHandlers() {
        ClassPool pool = ClassPool.getDefault();
        int wrapperCounter = 0;
        String className = ProcessingMachineRecipeHandlerWrapper.class.getName();
        for (Class<? extends TileEntityBasicProcessingMachine> clazz : ProcessingMachineRegistry.getTileClasses()) {
            try {
                TileEntityBasicProcessingMachine.enableDummyCreationPhase();
                TileEntityBasicProcessingMachine dummyTile = clazz.newInstance();
                TileEntityBasicProcessingMachine.disableDummyCreationPhase();

                CtClass classCopy = pool.getAndRename(className, "monnef.jaffas.food.nei.wrapper.PMRH" + (wrapperCounter++));
                Class wrapperClass = classCopy.toClass();
                Method initMethod = wrapperClass.getDeclaredMethod("init", TileEntityBasicProcessingMachine.class);
                initMethod.invoke(null, dummyTile);
                API.registerRecipeHandler((ICraftingHandler) wrapperClass.newInstance());
                API.registerUsageHandler((IUsageHandler) wrapperClass.newInstance());
            } catch (Throwable e) {
                throw new RuntimeException(String.format("Problem in NEI processing machine handler construction. (Wrapper's class name: %s)", className), e);
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
