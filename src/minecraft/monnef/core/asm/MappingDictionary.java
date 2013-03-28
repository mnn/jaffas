package monnef.core.asm;

import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static monnef.core.MonnefCorePlugin.Log;
import static monnef.core.asm.ObfuscationHelper.isRunningInObfuscatedMode;

public class MappingDictionary implements Serializable {
    private HashMap<String, HashSet<String>> data;

    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("data", HashMap.class)};

    public MappingDictionary() {
        data = new HashMap<String, HashSet<String>>();
    }

    public String getFirst(String fullName) {
        HashSet<String> set = data.get(fullName);
        return testSet(set).iterator().next();
    }

    public Set<String> keySet() {
        return data.keySet();
    }

    public Set<String> getAll(String key) {
        HashSet<String> set = data.get(key);
        if (set == null) return new HashSet<String>(Arrays.asList("<none>"));
        return new HashSet<String>(set);
    }

    public int countKeys() {
        return data.size();
    }

    public HashSet<String> get(String fullName) {
        HashSet<String> set = data.get(fullName);
        return new HashSet<String>(testSet(set));
    }

    public void put(String name, Set<String> translations) {
        if (data.containsKey(name)) {
            throw new RuntimeException(String.format("Name %s is already present.", name));
        }
        HashSet<String> newEntry = new HashSet<String>(translations);
        data.put(name, newEntry);
    }

    private HashSet<String> testSet(HashSet<String> set) {
        if (set == null) {
            throw new RuntimeException("Object not found.");
        }
        return set;
    }

    public void putQuietly(String name, String translation) {
        put(name, translation, true);
    }

    public void put(String name, String translation) {
        put(name, translation, false);
    }

    private void put(String name, String translation, boolean quiet) {
        HashSet<String> set;
        if (data.containsKey(name)) {
            set = data.get(name);
        } else {
            set = new HashSet<String>();
            data.put(name, set);
        }

        if (set.contains(translation)) {
            String message = String.format("Translation [%s] for [%s] already exists.", translation, name);
            if (quiet) {
                Log.printFinest(message);
            } else {
                Log.printWarning(message);
            }
        }
        set.add(translation);
    }

    public boolean canBeTranslatedTo(String toTranslateName, String testedTranslatedName) {
        if (isRunningInObfuscatedMode()) {
            return containsKey(toTranslateName) && get(toTranslateName).contains(testedTranslatedName);
        } else {
            return testedTranslatedName != null && testedTranslatedName.equals(toTranslateName);
        }
    }

    public boolean containsKey(String name) {
        return data.containsKey(name);
    }
}
