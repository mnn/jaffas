package monnef.jaffas.food;

import cpw.mods.fml.common.FMLLog;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    public static Logger logger;

    static {
        logger = Logger.getLogger("Jaffas");
        logger.setParent(FMLLog.getLogger());
        logger.setUseParentHandlers(true);
    }

    public static void printInfo(String message) {
        print(Level.INFO, message);
    }

    public static void printWarning(String message) {
        print(Level.WARNING, message);
    }

    public static void printSevere(String message) {
        print(Level.SEVERE, message);
    }

    public static void print(Level level, String message) {
        logger.log(level, message);
    }

    public static void printDebug(String message) {
        if (mod_jaffas_food.debug) {
            print(Level.INFO, "[D] " + message);
        }
    }
}
