/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import monnef.core.utils.IDProvider;
import monnef.jaffas.food.common.JaffaCreativeTab;
import monnef.jaffas.food.common.Reference;
import net.minecraftforge.common.Configuration;

import java.util.Arrays;

public abstract class jaffasMod {
    public JaffaCreativeTab creativeTab;
    protected IDProvider idProvider;
    public Configuration config;
    protected boolean thisIsMainModule = false;

    protected jaffasMod() {
    }

    private void handleMetadata() {
        ModContainer container = FMLCommonHandler.instance().findContainerFor(this);
        ModMetadata metaData = container.getMetadata();
        fillCommonMetadata(metaData);
        fillModuleSpecificMetadata(metaData);
    }

    protected void fillModuleSpecificMetadata(ModMetadata data) {
    }

    protected void fillCommonMetadata(ModMetadata data) {
        data.autogenerated = false;
        data.authorList = Arrays.asList(monnef.core.Reference.MONNEF, monnef.core.Reference.TIARTYOS);
        data.logoFile = "/jaffas_logo.png";
        data.url = monnef.core.Reference.URL_JAFFAS;

        if (!thisIsMainModule) {
            setAsChildOfFoodModule(data);
        }
    }

    protected void setAsChildOfFoodModule(ModMetadata data) {
        data.parent = Reference.ModId;
    }

    public void load(FMLInitializationEvent event) {
        handleMetadata();
    }

    public void PreLoad(FMLPreInitializationEvent event) {
        idProvider = new IDProvider(getStartOfBlocksIdInterval(), getStartOfItemsIdInterval());
        config = new Configuration(
                event.getSuggestedConfigurationFile());
    }

    protected abstract int getStartOfItemsIdInterval();

    protected abstract int getStartOfBlocksIdInterval();
}
