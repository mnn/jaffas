/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import monnef.jaffas.food.JaffasFood;

import java.util.HashMap;

public class CoolDownRegistry {
    private static HashMap<CoolDownType, HashMap<String, CoolDownEntry>> table;
    private static boolean debug = false;

    static {
        table = new HashMap<CoolDownType, HashMap<String, CoolDownEntry>>();
        for (CoolDownType type : CoolDownType.values()) {
            table.put(type, new HashMap<String, CoolDownEntry>());
        }
        if (JaffasFood.debug) debug = true;
    }

    public static int getRemainingCoolDownInSeconds(String name, CoolDownType type) {
        CoolDownEntry entry = table.get(type).get(name);
        if (entry == null) return 0;
        return entry.getRemainingCoolDown();
    }

    public static void setCoolDown(String name, CoolDownType type, int coolDownInSeconds) {
        HashMap<String, CoolDownEntry> db = table.get(type);
        createEntry(name, type);

        CoolDownEntry entry = db.get(name);
        entry.setCoolDown(coolDownInSeconds);
    }

    private static void createEntry(String name, CoolDownType type) {
        if (!table.get(type).containsKey(name)) table.get(type).put(name, new CoolDownEntry());
    }

    public static boolean isCoolDownActive(String name, CoolDownType type) {
        return getRemainingCoolDownInSeconds(name, type) > 0;
    }

    public static void synchronizeCoolDown(String name, CoolDownType type, int coolDownEnd) {
        createEntry(name, type);

        CoolDownEntry entry = table.get(type).get(name);
        entry.synchronizeCoolDown(coolDownEnd);
    }
}
