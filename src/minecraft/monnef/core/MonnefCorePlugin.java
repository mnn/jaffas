package monnef.core;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

public class MonnefCorePlugin implements IFMLLoadingPlugin {
    public static final String CORE_NAMESPACE = "monnef.core.";

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
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }
}
