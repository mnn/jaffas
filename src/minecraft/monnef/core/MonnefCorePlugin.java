package monnef.core;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.RelaunchClassLoader;
import monnef.core.asm.ObfuscationHelper;
import monnef.core.utils.CustomLogger;

import java.util.Map;

import static cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import static cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@MCVersion
@TransformerExclusions({MonnefCorePlugin.CORE_NAMESPACE + ".asm", MonnefCorePlugin.CORE_NAMESPACE + ".asm.cloakHook"})
public class MonnefCorePlugin implements IFMLLoadingPlugin, IFMLCallHook {
    public static final String CORE_NAMESPACE = "monnef.core";
    public static final String CLASS_LOADER_TAG = "classLoader";
    public static CustomLogger Log = new CustomLogger("mC");
    public static boolean debugEnv = !ObfuscationHelper.isRunningInObfuscatedMode();
    static boolean initialized = false;

    public static boolean isInitialized() {
        return initialized;
    }

    public static RelaunchClassLoader classLoader;

    public MonnefCorePlugin() {
    }

    @Override
    public String[] getLibraryRequestClass() {
        return new String[]{CORE_NAMESPACE + ".Library"};
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{CORE_NAMESPACE + ".asm.CoreTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return CORE_NAMESPACE + ".CoreModContainer";
    }

    @Override
    public String getSetupClass() {
        return CORE_NAMESPACE + ".MonnefCorePlugin";
    }

    @Override
    public void injectData(Map<String, Object> data) {
        if (data.containsKey(CLASS_LOADER_TAG)) {
            MonnefCorePlugin.classLoader = (RelaunchClassLoader) data.get(CLASS_LOADER_TAG);
        }
    }

    @Override
    public Void call() throws Exception {
        MonnefCorePlugin.Log.printInfo("monnef's Core initialized, version: " + Reference.Version);
        return null;
    }
}
