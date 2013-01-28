package monnef.jaffas.food.common;

import monnef.jaffas.food.mod_jaffas;

import java.util.HashMap;

public class CoolDownRegistry {
    private static HashMap<CoolDownType, HashMap<String, CoolDownEntry>> table;
    private static boolean debug = false;

    static {
        table = new HashMap<CoolDownType, HashMap<String, CoolDownEntry>>();
        for (CoolDownType type : CoolDownType.values()) {
            table.put(type, new HashMap<String, CoolDownEntry>());
        }
        if (mod_jaffas.debug) debug = true;
    }

    public int getRemainingCoolDownInSeconds(String name, CoolDownType type) {
        CoolDownEntry entry = table.get(type).get(name);
        if (entry == null) return 0;
        return entry.getRemainingCoolDown();
    }

    public void setCoolDown(String name, CoolDownType type, int coolDownInSeconds) {
        HashMap<String, CoolDownEntry> db = table.get(type);
        createEntry(name, type);

        CoolDownEntry entry = db.get(name);
        entry.setCoolDown(coolDownInSeconds);
    }

    private void createEntry(String name, CoolDownType type) {
        if (!table.get(type).containsKey(name)) table.get(type).put(name, new CoolDownEntry());
    }

    public boolean isCoolDownActive(String name, CoolDownType type) {
        return getRemainingCoolDownInSeconds(name, type) > 0;
    }

    public void synchronizeCoolDown(String name, CoolDownType type, int coolDownEnd) {
        createEntry(name, type);

        CoolDownEntry entry = table.get(type).get(name);
        entry.synchronizeCoolDown(coolDownEnd);
    }
}
