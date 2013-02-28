package monnef.core;

import cpw.mods.fml.relauncher.ILibrarySet;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static monnef.core.MonnefCorePlugin.Log;

public class Library implements ILibrarySet {
    public static final String SKIP_LIBRARY_DOWNLOAD_TAG = "skipLibraryDownload";

    public static final String DOWNLOAD_URL = Reference.URL + "/lib/%s";
    public static final String CONFIG_DIR = "config";

    private static final HashMap<String, LibraryInfo> Libraries;

    static {
        Libraries = new HashMap<String, LibraryInfo>();
        addLibrary("Jsoup", "99351550d1fa2e8147319dbafd3f3a79d4f4c6e5", "jsoup-1.7.1.jar");
        addLibrary("Lombok", "ba171d45e78f08ccca3cf531d285f13cfb4de2c7", "lombok_0_11_6.jar");
    }

    private static void addLibrary(String name, String hash, String fileName) {
        Libraries.put(name.toLowerCase(), new LibraryInfo(name, hash, fileName));
    }

    public Library() {
        handleConfig();
    }

    private void handleConfig() {
        String pathname = getActualPath() + "/" + CONFIG_DIR + "/" + Reference.ModId + ".cfg";
        Log.printInfo("Trying to open config file: \"" + pathname + "\"");
        File config = new File(pathname);
        InputStream inputStream = null;

        boolean fileIsReady = false;
        if (config.exists()) {
            if (!config.isFile()) {
                throw new RuntimeException("\"" + config + "\" is not a file.");
            }

            try {
                inputStream = new FileInputStream(config);
                fileIsReady = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.printInfo("Config file not found. Creating one.");
            OutputStream outputStream;
            try {
                outputStream = new FileOutputStream(pathname);
                Properties prop = new Properties();
                prop.setProperty(SKIP_LIBRARY_DOWNLOAD_TAG, "");
                prop.store(outputStream, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.printInfo("Problem occurred in default config writing.");
            } catch (IOException e) {
                e.printStackTrace();
                Log.printInfo("Problem occurred in default config writing.");
            }
        }

        if (fileIsReady) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            try {
                Properties prop = new Properties();
                prop.load(inputStream);
                String skipString = prop.getProperty(SKIP_LIBRARY_DOWNLOAD_TAG, "");

                if (!skipString.isEmpty()) {
                    String[] skipList = skipString.split(", ?");
                    for (String toSkip : skipList) {
                        tryDisableLibrary(toSkip.toLowerCase());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.printInfo("Problem occurred parsing config.");
            }

        }
    }

    private void tryDisableLibrary(String name) {
        if (Libraries.containsKey(name)) {
            Log.printInfo("Disabled download for library: " + name);
            Libraries.remove(name);
        } else {
            Log.printWarning("Not found library named \"" + name + "\", skipping.");
        }
    }

    private String getMyPath() {
        URL url = Library.class.getProtectionDomain().getCodeSource().getLocation();
        File file;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("cannot detect my path");
        }

        return file.getAbsolutePath();
    }

    private String getActualPath() {
        String path;
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("cannot detect current path");
        }

        return path;
    }

    @Override
    public String[] getLibraries() {
        return getLibraryInfo(EnumLibraryInfoType.FILE_NAME);
    }

    @Override
    public String[] getHashes() {
        return getLibraryInfo(EnumLibraryInfoType.HASH);
    }

    private String[] getLibraryInfo(EnumLibraryInfoType type) {
        ArrayList<String> tmp = new ArrayList<String>();
        for (LibraryInfo info : Libraries.values()) {
            String toAdd;

            switch (type) {
                case FILE_NAME:
                    toAdd = info.getFileName();
                    break;

                case HASH:
                    toAdd = info.getSha1Hash();
                    break;

                default:
                    throw new RuntimeException();
            }

            tmp.add(toAdd);
        }

        return tmp.toArray(new String[tmp.size()]);
    }

    @Override
    public String getRootURL() {
        return DOWNLOAD_URL;
    }

    private enum EnumLibraryInfoType {
        FILE_NAME, HASH
    }
}

