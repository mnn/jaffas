package monnef.core;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

import static cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({MonnefCorePlugin.CORE_NAMESPACE})
public class MonnefCorePlugin implements IFMLLoadingPlugin {
    public static final String CORE_NAMESPACE = "monnef.core.";
    public static CustomLogger Log = new CustomLogger("mC");

    public MonnefCorePlugin() {

    }

    @Override
    public String[] getLibraryRequestClass() {
        return new String[]{CORE_NAMESPACE + "Library"};
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
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
