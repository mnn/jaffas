/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import monnef.core.common.ContainerRegistry;
import monnef.core.external.javassist.ClassPool;
import monnef.core.external.javassist.CtClass;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import net.minecraft.tileentity.TileEntity;

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
        for (Class<? extends TileEntity> clazz : ContainerRegistry.getTileClasses()) {
            try {
                if (TileEntityBasicProcessingMachine.class.isAssignableFrom(clazz)) {
                    TileEntityBasicProcessingMachine.enableDummyCreationPhase();
                    TileEntityBasicProcessingMachine dummyTile = (TileEntityBasicProcessingMachine) clazz.newInstance();
                    TileEntityBasicProcessingMachine.disableDummyCreationPhase();

                    CtClass classCopy = pool.getAndRename(ProcessingMachineRecipeHandlerWrapper.class.getCanonicalName(), "monnef.jaffas.food.nei.wrapper.PMRH" + (wrapperCounter++));
                    Class wrapperClass = classCopy.toClass();
                    Method initMethod = wrapperClass.getDeclaredMethod("init", TileEntityBasicProcessingMachine.class);
                    initMethod.invoke(null, dummyTile);
                    API.registerRecipeHandler((ICraftingHandler) wrapperClass.newInstance());
                    API.registerUsageHandler((IUsageHandler) wrapperClass.newInstance());
                }
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
