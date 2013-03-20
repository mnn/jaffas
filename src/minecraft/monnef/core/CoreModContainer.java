package monnef.core;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
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
    public void onStart(FMLInitializationEvent event) {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side == Side.CLIENT) {
            CloakHookHandler.registerCloakHandler(new CustomCloaksHandler());

            if (!CoreTransformer.cloakHookApplied) {
                printDebugDataAndCrash("Unable to install a cloak hook!");
            }
        } else {
            if (!CoreTransformer.lightningHookApplied) {
                printDebugDataAndCrash("Unable to install a lightning hook!");
            }
        }

        if (MonnefCorePlugin.debugEnv) {
            ObfuscationHelper.dumpUsedItemsToConfig();
        }

        MonnefCorePlugin.initialized = true;
    }

    private void printDebugDataAndCrash(String msg) {
        Log.printSevere(msg);
        Log.printFine("Mapping database:");
        ObfuscationHelper.printAllDataToLog();
        throw new RuntimeException(msg);
    }
}
