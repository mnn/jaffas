package monnef.jaffas.food.common;

import cpw.mods.fml.common.Loader;
import forestry.api.farming.Farmables;
import forestry.api.farming.IFarmable;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.ClassHelper;
import monnef.jaffas.food.JaffasFood;

import java.util.ArrayList;

import static monnef.jaffas.food.JaffasFood.Log;

public class OtherModsHelper {
    boolean IDResolverDetected;
    boolean forestryDetected;
    boolean MFRDetected;
    boolean TEDetected;

    public OtherModsHelper() {
        checkForestry();
        checkMFR();
        checkTE();
        checkIDResolver();
    }


    public static void dumpForestryRegister(String category) {
        String msg = "Registered items for " + category + ": ";
        for (IFarmable item : Farmables.farmables.get(category)) {
            msg += (item == null ? "NULL" : item.getClass().getSimpleName()) + ", ";
        }
        if (msg.length() > 2) msg = msg.substring(0, msg.length() - 2);
        JaffasFood.Log.printFinest(msg);
    }

    public boolean isForestryDetected() {
        return this.forestryDetected;
    }

    public boolean isMineFactoryReloadedDetected() {
        return this.MFRDetected;
    }

    public boolean isTEDetected() {
        return TEDetected;
    }

    public boolean isIDResolverDetected() {
        return IDResolverDetected;
    }

    public Iterable<String> compileDetectedMods() {
        ArrayList<String> list = new ArrayList<String>();
        if (isForestryDetected()) list.add("forestry");
        if (isMineFactoryReloadedDetected()) list.add("MFR");
        if (isTEDetected()) list.add("TE");
        if (list.size() == 0) list.add("none");
        return list;
    }

    public static void checkCore() {
        if (!MonnefCorePlugin.isInitialized()) {
            throw new RuntimeException("Core is not properly initialized!");
        }
    }

    private void checkIDResolver() {
        IDResolverDetected = ClassHelper.isClassPresent("sharose.mods.idresolver.IDResolverMod");
    }

    private void checkForestry() {
        forestryDetected = ClassHelper.isClassPresent("forestry.Forestry");
    }

    private void checkMFR() {
        MFRDetected = Loader.isModLoaded("MineFactoryReloaded");
    }

    private void checkTE() {
        TEDetected = Loader.isModLoaded("ThermalExpansion");
    }

    public boolean insertFarmable(String category, IFarmable farmable) {
        if (!Farmables.farmables.containsKey(category)) {
            Log.printWarning("Forestry integration is broken.");
            return false;
        }

        Farmables.farmables.get(category).add(farmable);
        return true;
    }
}
