package monnef.core;

import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import cpw.mods.fml.relauncher.ILibrarySet;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static argo.jdom.JsonNodeFactories.*;
import static monnef.core.MonnefCorePlugin.Log;

public class Library implements ILibrarySet {
    public static final String SKIP_LIBRARY_DOWNLOAD_TAG = "skipLibraryDownload";
    private static JdomParser parser = new JdomParser();

    public static final String DOWNLOAD_URL = Reference.URL + "/lib/%s";
    public static final String CONFIG_DIR = "config";

    private static final HashMap<String, LibraryInfo> Libraries;

    static {
        Libraries = new HashMap<String, LibraryInfo>();
        addLibrary("Jsoup", "99351550d1fa2e8147319dbafd3f3a79d4f4c6e5", "jsoup-1.7.1.jar");
        addLibrary("Lombok", "ba171d45e78f08ccca3cf531d285f13cfb4de2c7", "lombok_0_11_6.jar");
    }

    private static void addLibrary(String name, String hash, String fileName) {
        Libraries.put(name, new LibraryInfo(name, hash, fileName));
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

                JsonRootNode json = object(field(SKIP_LIBRARY_DOWNLOAD_TAG, array()));
                PrettyJsonFormatter formatter = new PrettyJsonFormatter();
                outputStream.write(formatter.format(json).getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.printInfo("Problem occurred in default config writing.");
            } catch (IOException e) {
                e.printStackTrace();
                Log.printInfo("Problem occurred in default config generating.");
            }
        }

        if (fileIsReady) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            try {
                JsonRootNode root = parser.parse(reader);
                List<JsonNode> list = root.getArrayNode(SKIP_LIBRARY_DOWNLOAD_TAG);
                for (JsonNode node : list) {
                    String name = node.getStringValue();
                    if (Libraries.containsKey(name)) {
                        Log.printInfo("Disabled download for library: " + name);
                        Libraries.remove(name);
                    } else {
                        Log.printWarning("Not found library named \"" + name + "\", skipping.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.printInfo("Problem occurred parsing config.");
            } catch (InvalidSyntaxException e) {
                e.printStackTrace();
                Log.printInfo("Problem occurred parsing config.");
            }

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

