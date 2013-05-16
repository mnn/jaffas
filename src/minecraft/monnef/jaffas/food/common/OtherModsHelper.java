package monnef.jaffas.food.common;

import cpw.mods.fml.common.Loader;
import monnef.core.MonnefCorePlugin;

import java.util.ArrayList;

public class OtherModsHelper {
    boolean forestryDetected;
    boolean MFRDetected;
    boolean TEDetected;

    public OtherModsHelper() {
        checkForestry();
        checkMFR();
        checkTE();
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

    public Iterable<String> compileDetectedMods() {
        ArrayList<String> list = new ArrayList<String>();
        if (isForestryDetected()) list.add("forestry");
        if (isMineFactoryReloadedDetected()) list.add("MFR");
        if (isTEDetected()) list.add("TE");
        if (list.size() == 0) list.add("none");
        return list;
    }

    public void checkCore() {
        if (!MonnefCorePlugin.isInitialized()) {
            throw new RuntimeException("Core is not properly initialized!");
        }
    }

    private void checkForestry() {
        try {
            Class c = Class.forName("forestry.Forestry");
            this.forestryDetected = true;
        } catch (ClassNotFoundException e) {
            this.forestryDetected = false;
        }
    }

    private void checkMFR() {
        MFRDetected = Loader.isModLoaded("MineFactoryReloaded");
    }

    private void checkTE() {
        TEDetected = Loader.isModLoaded("ThermalExpansion");
    }
}
