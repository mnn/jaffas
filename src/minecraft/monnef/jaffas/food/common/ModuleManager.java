/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.common;

import java.util.HashSet;

public class ModuleManager {
    private static HashSet<ModulesEnum> ModulesEnabled;
    private static ModuleManager instance;

    public ModuleManager() {
        ModulesEnabled = new HashSet<ModulesEnum>();

        if (instance != null) {
            throw new RuntimeException("multiple instances of module manager");
        }
        instance = this;
    }

    public static boolean IsModuleEnabled(ModulesEnum module) {
        return ModulesEnabled.contains(module);
    }

    public static void Add(ModulesEnum module) {
        ModulesEnabled.add(module);
    }

    public static HashSet<ModulesEnum> CompileEnabledModules() {
        return (HashSet<ModulesEnum>) ModulesEnabled.clone();
    }
}
