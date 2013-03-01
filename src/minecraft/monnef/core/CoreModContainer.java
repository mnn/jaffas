package monnef.core;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import monnef.core.asm.CoreTransformer;
import monnef.core.asm.ObfuscationHelper;

import static monnef.core.MonnefCorePlugin.Log;

public class CoreModContainer extends DummyModContainer {
    private static CoreModContainer instance;

    public CoreModContainer() {
        super(new ModMetadata());
        ModMetadata myMeta = super.getMetadata();
        myMeta.authorList = Reference.Authors;
        myMeta.description = "The core library used by " + Reference.MONNEF + "'s mods.";
        myMeta.modId = Reference.ModId;
        myMeta.version = Reference.Version;
        myMeta.name = Reference.ModName;
        myMeta.url = Reference.URL;

        if (instance != null) {
            System.out.println("multiple container creation?");
        }

        instance = this;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    public static CoreModContainer instance() {
        return instance;
    }

    // use google subscribe and FML events
    @Subscribe
    public void onStart(FMLPostInitializationEvent event) {
        if (!CoreTransformer.cloakHookApplied) {
            Log.printFine("Mapping database:");
            ObfuscationHelper.printAllDataToLog();
            throw new RuntimeException("Unable to install a cloak hook!");
        }

        if (MonnefCorePlugin.debugEnv) {
            ObfuscationHelper.dumpUsedItemsToConfig();
        }
    }
}
