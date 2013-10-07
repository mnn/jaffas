/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import monnef.core.MonnefCorePlugin;
import monnef.core.external.javassist.ClassClassPath;
import monnef.core.external.javassist.ClassPool;
import monnef.core.external.javassist.CtClass;
import monnef.core.external.javassist.NotFoundException;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.power.block.common.ProcessingMachineRegistry;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;

import java.lang.reflect.Method;
import java.util.Iterator;

import static monnef.jaffas.food.JaffasFood.Log;

public class NEIJaffasConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new BoardRecipeHandler());
        API.registerUsageHandler(new BoardRecipeHandler());
        constructProcessingMachineHandlers();
    }

    private void constructProcessingMachineHandlers() {
        ClassPool pool = ClassPool.getDefault();
        if (!MonnefCorePlugin.debugEnv) {
            pool.insertClassPath(new ClassClassPath(this.getClass()));
        }

        Iterator iter = pool.getImportedPackages();
        Log.printFinest("Packages:");
        while (iter.hasNext()) Log.printFinest(iter.next().toString());

        int wrapperCounter = 0;
        String className = ProcessingMachineRecipeHandlerWrapper.class.getName();

        CtClass classCopy = null;
        try {
            classCopy = pool.getAndRename(className, getNextWrapperClassName(wrapperCounter));
        } catch (NotFoundException e) {
            throw new RuntimeException("Problem in NEI processing machine handler construction. Cannot get the wrapper template class.", e);
        }

        for (Class<? extends TileEntityBasicProcessingMachine> clazz : ProcessingMachineRegistry.getTileClasses()) {
            try {
                TileEntityBasicProcessingMachine.enableDummyCreationPhase();
                TileEntityBasicProcessingMachine dummyTile = clazz.newInstance();
                TileEntityBasicProcessingMachine.disableDummyCreationPhase();

                classCopy.defrost();
                Class wrapperClass = classCopy.toClass();
                Method initMethod = wrapperClass.getDeclaredMethod("init", TileEntityBasicProcessingMachine.class);
                initMethod.invoke(null, dummyTile);

                API.registerRecipeHandler((ICraftingHandler) wrapperClass.newInstance());
                API.registerUsageHandler((IUsageHandler) wrapperClass.newInstance());
                Log.printFine("Successfully created wrapper for NEI integration for " + clazz.getSimpleName());

                wrapperCounter++;
                classCopy.defrost();
                classCopy.setName(getNextWrapperClassName(wrapperCounter));
            } catch (Throwable e) {
                Log.printSevere(String.format("Problem in NEI processing machine handler construction. (Wrapper's class name: %s, current machine: %s)", className, clazz.getName()));
                Log.printSevere(e.toString());
            }
        }
    }

    private String getNextWrapperClassName(int wrapperCounter) {
        return "monnef.jaffas.food.nei.wrapper.PMRH" + wrapperCounter;
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
