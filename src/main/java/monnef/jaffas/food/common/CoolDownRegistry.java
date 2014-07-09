/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import monnef.jaffas.food.JaffasFood;

import java.util.HashMap;
import java.util.UUID;

public class CoolDownRegistry {
    private static HashMap<CoolDownType, HashMap<UUID, CoolDownEntry>> table;
    private static boolean debug = false;

    static {
        table = new HashMap<CoolDownType, HashMap<UUID, CoolDownEntry>>();
        for (CoolDownType type : CoolDownType.values()) {
            table.put(type, new HashMap<UUID, CoolDownEntry>());
        }
        if (JaffasFood.debug) debug = true;
    }

    public static int getRemainingCoolDownInSeconds(UUID uuid, CoolDownType type) {
        CoolDownEntry entry = table.get(type).get(uuid);
        if (entry == null) return 0;
        return entry.getRemainingCoolDown();
    }

    public static void setCoolDown(UUID uuid, CoolDownType type, int coolDownInSeconds) {
        HashMap<UUID, CoolDownEntry> db = table.get(type);
        createEntry(uuid, type);

        CoolDownEntry entry = db.get(uuid);
        entry.setCoolDown(coolDownInSeconds);
    }

    private static void createEntry(UUID uuid, CoolDownType type) {
        if (!table.get(type).containsKey(uuid)) table.get(type).put(uuid, new CoolDownEntry());
    }

    public static boolean isCoolDownActive(UUID uuid, CoolDownType type) {
        return getRemainingCoolDownInSeconds(uuid, type) > 0;
    }

    public static void synchronizeCoolDown(UUID uuid, CoolDownType type, int coolDownEnd) {
        createEntry(uuid, type);

        CoolDownEntry entry = table.get(type).get(uuid);
        entry.synchronizeCoolDown(coolDownEnd);
    }
}
