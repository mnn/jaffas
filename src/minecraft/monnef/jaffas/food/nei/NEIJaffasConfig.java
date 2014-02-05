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
import monnef.core.common.ContainerRegistry;
import monnef.core.external.javassist.CannotCompileException;
import monnef.core.external.javassist.ClassClassPath;
import monnef.core.external.javassist.ClassPool;
import monnef.core.external.javassist.CtClass;
import monnef.core.external.javassist.NotFoundException;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;

import static monnef.jaffas.food.JaffasFood.Log;

public class NEIJaffasConfig implements IConfigureNEI {
    private static HashSet<Class<?>> ignoredClasses = new HashSet<Class<?>>();

    static {
        ignoredClasses.add(TileBPMDummy.class);
        ignoredClasses.add(TileDBPMDummy.class);
    }

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

        printImportedPackages(pool);

        int wrapperCounter = 0;
        String className = ProcessingMachineRecipeHandlerWrapper.class.getName();

        CtClass classCopy;
        try {
            classCopy = pool.getAndRename(className, getNextWrapperClassName(wrapperCounter));
        } catch (NotFoundException e) {
            throw new RuntimeException("Problem in NEI processing machine handler construction. Cannot get the wrapper template class.", e);
        }

        for (Class<? extends TileEntity> clazz : ContainerRegistry.getTileClasses()) {
            boolean isAbstract = Modifier.isAbstract(clazz.getModifiers());
            boolean isAssignableFrom = TileEntityBasicProcessingMachine.class.isAssignableFrom(clazz);
            boolean isIgnored = ignoredClasses.contains(clazz);
            if (isAssignableFrom && !isAbstract && !isIgnored) {
                try {
                    Class wrapperClass = copyAndInit(classCopy, clazz);
                    registerHandlers(clazz, wrapperClass);
                    wrapperCounter = renameNewClass(wrapperCounter, classCopy);
                    Log.printFinest(String.format("[NEI] Created wrapper %s for %s.", classCopy.getSimpleName(), clazz.getSimpleName()));
                } catch (Throwable e) {
                    Log.printSevere(String.format("Problem in NEI processing machine handler construction. (Wrapper's class name: %s, current machine: %s)", className, clazz.getName()));
                    Log.printSevere(e.toString());
                }
            }
        }
    }

    private int renameNewClass(int wrapperCounter, CtClass classCopy) {
        wrapperCounter++;
        classCopy.defrost();
        classCopy.setName(getNextWrapperClassName(wrapperCounter));
        return wrapperCounter;
    }

    private void registerHandlers(Class<? extends TileEntity> clazz, Class wrapperClass) throws InstantiationException, IllegalAccessException {
        API.registerRecipeHandler((ICraftingHandler) wrapperClass.newInstance());
        API.registerUsageHandler((IUsageHandler) wrapperClass.newInstance());
    }

    @SuppressWarnings("unchecked")
    private Class copyAndInit(CtClass classCopy, Class<? extends TileEntity> clazz) throws CannotCompileException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        classCopy.defrost();
        Class wrapperClass = classCopy.toClass();
        Method initMethod = wrapperClass.getDeclaredMethod("init", Class.class);
        initMethod.invoke(null, clazz);
        return wrapperClass;
    }

    private void printImportedPackages(ClassPool pool) {
        Iterator iter = pool.getImportedPackages();
        Log.printFinest("Packages:");
        while (iter.hasNext()) Log.printFinest(iter.next().toString());
    }

    private String getNextWrapperClassName(int wrapperCounter) {
        return "monnef.jaffas.food.nei.wrapper.ProcessingMachineRecipeHandler" + wrapperCounter;
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
