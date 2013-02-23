package monnef.core;

import cpw.mods.fml.relauncher.IFMLCallHook;

import java.util.Map;

public class CoreInitializer implements IFMLCallHook {
    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public Void call() throws Exception {
        MonnefCorePlugin.Log.printInfo("monnef's Core initialized, version: " + Reference.Version);
        return null;
    }
}
