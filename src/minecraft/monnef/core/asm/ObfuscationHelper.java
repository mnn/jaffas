package monnef.core.asm;

import monnef.core.utils.PathHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

import static monnef.core.MonnefCorePlugin.Log;

public class ObfuscationHelper {
    public static final String JAFFAS_MAPPINGS_CFG = "jaffas_mappings.ser";
    private static boolean runningInObfuscatedMode = System.getProperty("debugFlag") == null;

    private static HashMap<MappedObjectType, MappingDictionary> database;
    private static HashMap<MappedObjectType, HashSet<MappedObject>> usedFlags;

    private static boolean initialized = false;

    static {
        database = new HashMap<MappedObjectType, MappingDictionary>();
        usedFlags = new HashMap<MappedObjectType, HashSet<MappedObject>>();
        for (MappedObjectType type : MappedObjectType.values()) {
            database.put(type, new MappingDictionary());
            usedFlags.put(type, new HashSet<MappedObject>());
        }
    }

    public static boolean isRunningInObfuscatedMode() {
        return runningInObfuscatedMode;
    }

    public static String getRealNameSlashed(MappedObject toTranslate) {
        return getRealName(toTranslate).replace('.', '/');
    }

    public static String getRealName(MappedObject toTranslate) {
        if (!initialized) initialize();

        String translated = database.get(toTranslate.getType()).getFirst(toTranslate.getFullName());
        if (translated == null) {
            printAllDataToLog(database);
            throw new RuntimeException(String.format("Mapping \"%s\" not found!", toTranslate.getFullName()));
        }

        usedFlags.get(toTranslate.getType()).add(toTranslate);

        if (!runningInObfuscatedMode) {
            return toTranslate.getFullName();
        } else {
            return translated;
        }
    }

    private static void initialize() {
        if (runningInObfuscatedMode) {
            loadConfigFromJar();
        } else {
            McpParser.parse(database, PathHelper.getActualPath() + "/../conf/");
            Log.printInfo("After MCP parser we have " + formatDatabaseStats(database) + ".");
        }

        initialized = true;
    }

    private static void loadConfigFromJar() {
        String myJar = PathHelper.getMyPath();
        URL url;
        InputStream inputStream = null;
        try {
            String urlString = "jar:file:" + myJar + "!/" + JAFFAS_MAPPINGS_CFG;
            Log.printFine(String.format("url string: [%s], jar path: [%s]", urlString, myJar));
            url = new URL(urlString);
            inputStream = url.openStream();
            if (inputStream == null) {
                throw new RuntimeException("Unable to get data from my JAR.");
            }
            database = MappingsConfigManager.loadConfig(inputStream);
            Log.printFine("Loaded from mappings config: " + formatDatabaseStats(database) + ".");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static void printAllDataToLog() {
        printAllDataToLog(database);
    }

    public static void printAllDataToLog(HashMap<MappedObjectType, MappingDictionary> database) {
        for (MappedObjectType type : MappedObjectType.values()) {
            Log.printFine(type.toString() + ":");
            MappingDictionary collection = database.get(type);
            for (String key : collection.keySet()) {
                Log.printFine(String.format("[%s] -> [%s]", key, collection.getAll(key)));
            }
        }
    }

    private static String formatDatabaseStats(HashMap<MappedObjectType, MappingDictionary> database) {
        return database.get(MappedObjectType.METHOD).countKeys() + " methods, " +
                database.get(MappedObjectType.CLASS).countKeys() + " classes, " +
                database.get(MappedObjectType.FIELD).countKeys() + " fields";
    }

    public static void dumpUsedItemsToConfig() {
        String path = PathHelper.getActualPath() + "/../bin_data/" + JAFFAS_MAPPINGS_CFG;
        try {
            OutputStream output = new FileOutputStream(path);
            HashMap<MappedObjectType, MappingDictionary> usedOnlyDatabase = constructOnlyUsed(database, usedFlags);
            MappingsConfigManager.saveConfig(usedOnlyDatabase, output);

            Log.printFine(String.format("Saved items to \"%s\":", path));
            printAllDataToLog(usedOnlyDatabase);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<MappedObjectType, MappingDictionary> constructOnlyUsed(HashMap<MappedObjectType, MappingDictionary> database, HashMap<MappedObjectType, HashSet<MappedObject>> usedFlags) {
        HashMap<MappedObjectType, MappingDictionary> ret = new HashMap<MappedObjectType, MappingDictionary>();

        /*
        // construct based on usage flags (dangerous, when running server not all items are used...)
        for (MappedObjectType type : MappedObjectType.values()) {
            MappingDictionary entry = new MappingDictionary();
            MappingDictionary data = database.get(type);
            ret.put(type, entry);
            HashSet<MappedObject> flagged = usedFlags.get(type);
            for (MappedObject obj : flagged) {
                entry.put(obj.getFullName(), data.get(obj.getFullName()));
            }
        }
        */

        // construct from all enum items
        for (MappedObjectType type : MappedObjectType.values()) {
            MappingDictionary entry = new MappingDictionary();
            ret.put(type, entry);
        }

        for (MappedObject obj : MappedObject.values()) {
            MappedObjectType type = obj.getType();
            String fullName = obj.getFullName();

            MappingDictionary entry = ret.get(type);
            MappingDictionary data = database.get(type);
            entry.put(fullName, data.get(fullName));
        }

        return ret;
    }

    public static boolean namesAreEqual(String name, MappedObject toTranslate) {
        if (!initialized) initialize();

        usedFlags.get(toTranslate.getType()).add(toTranslate);

        return database.get(toTranslate.getType()).canBeTranslatedTo(toTranslate.getFullName(), name);
    }
}
