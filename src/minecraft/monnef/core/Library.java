package monnef.core;

import cpw.mods.fml.relauncher.ILibrarySet;

public class Library implements ILibrarySet {
    public static final String DOWNLOAD_URL = Reference.URL + "/lib/%s";

    @Override
    public String[] getLibraries() {
        return new String[]{
                "jsoup-1.7.1.jar",
                "lombok.jar"
        };
    }

    @Override
    public String[] getHashes() {
        return new String[]{
                "99351550d1fa2e8147319dbafd3f3a79d4f4c6e5",
                "ba171d45e78f08ccca3cf531d285f13cfb4de2c7"
        };
    }

    @Override
    public String getRootURL() {
        return DOWNLOAD_URL;
    }
}
