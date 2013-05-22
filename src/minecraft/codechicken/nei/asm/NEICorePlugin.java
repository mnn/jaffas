package codechicken.nei.asm;

import java.io.File;
import java.util.Map;

import codechicken.core.asm.CodeChickenAccessTransformer;
import codechicken.core.asm.CodeChickenCorePlugin;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions(value={"codechicken.nei.asm"})
public class NEICorePlugin implements IFMLLoadingPlugin, IFMLCallHook
{
    @Override
    public String[] getLibraryRequestClass()
    {
        return null;
    }

    @Override
    public String[] getASMTransformerClass()
    {
        CodeChickenCorePlugin.versionCheck(CodeChickenCorePlugin.mcVersion, "NotEnoughItems");
        return new String[]{"codechicken.nei.asm.NEITransformer"};
    }

    @Override
    public String getModContainerClass()
    {
        return "codechicken.nei.asm.NEIModContainer";
    }

    @Override
    public String getSetupClass()
    {
        return "codechicken.nei.asm.NEICorePlugin";
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        if(data.containsKey("coremodLocation"))
            location = (File) data.get("coremodLocation");
    }

    @Override
    public Void call()
    {
        CodeChickenAccessTransformer.addTransformerMap("nei_at.cfg");
        return null;
    }

    public static File location;
}
