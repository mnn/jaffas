package monnef.core;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

import static cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({MonnefCorePlugin.CORE_NAMESPACE})
public class MonnefCorePlugin implements IFMLLoadingPlugin {
    public static final String CORE_NAMESPACE = "monnef.core.";
    public static CustomLogger Log = new CustomLogger("mC");
    public static boolean debugEnv;

    static {
        debugEnv = System.getProperty("debugFlag") != null;
    }

    public MonnefCorePlugin() {
        CloakHookHandler.registerCloakHandler(new CustomCloaksHandler());
    }

    @Override
    public String[] getLibraryRequestClass() {
        return new String[]{CORE_NAMESPACE + "Library"};
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{CORE_NAMESPACE + "asm.CoreTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return CORE_NAMESPACE + "CoreModContainer";
    }

    @Override
    public String getSetupClass() {
        return CORE_NAMESPACE + "CoreInitializer";
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }
}
