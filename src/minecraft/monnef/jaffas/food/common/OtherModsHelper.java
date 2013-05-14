package monnef.jaffas.food.common;

import cpw.mods.fml.common.Loader;
import extrabiomes.api.Api;
import monnef.core.MonnefCorePlugin;

import java.util.ArrayList;

public class OtherModsHelper {
    boolean forestryDetected;
    boolean extraBiomes;
    boolean MFRDetected;
    boolean TEDetected;

    public OtherModsHelper() {
        checkExtrabiomes();
        checkForestry();
        checkMFR();
        checkTE();
    }

    public boolean isForestryDetected() {
        return this.forestryDetected;
    }

    public boolean isExtraBiomesDetected() {
        return this.extraBiomes;
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
        if (isExtraBiomesDetected()) list.add("extrabiomesxl");
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

    private void checkExtrabiomes() {
        extraBiomes = false;
        try {
            if (Api.isExtrabiomesXLActive())
                extraBiomes = true;
        } catch (Exception e) {
        }
    }
}
