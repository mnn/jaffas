package monnef.core;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

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
}
