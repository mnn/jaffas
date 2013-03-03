package monnef.core.utils;

import cpw.mods.fml.common.FMLLog;
import monnef.jaffas.food.mod_jaffas_food;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomLogger {
    private Logger logger;

    public CustomLogger(String logTitle) {
        logger = Logger.getLogger(logTitle);
        Logger parent = FMLLog.getLogger();
        logger.setParent(parent);
        logger.setUseParentHandlers(true);
    }

    public void printInfo(String message) {
        print(Level.INFO, message);
    }

    public void printWarning(String message) {
        print(Level.WARNING, message);
    }

    public void printSevere(String message) {
        print(Level.SEVERE, message);
    }

    public void print(Level level, String message) {
        logger.log(level, message);
    }

    public void printDebug(String message) {
        if (mod_jaffas_food.debug) {
            print(Level.INFO, "[D] " + message);
        }
    }

    public void printFine(String message) {
        print(Level.FINE, message);
    }
}
