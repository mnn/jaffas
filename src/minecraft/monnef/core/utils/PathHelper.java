package monnef.core.utils;

import monnef.core.Library;
import monnef.core.MonnefCorePlugin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathHelper {
    public static String getMyPath() {
        URL url = Library.class.getProtectionDomain().getCodeSource().getLocation();
        File file;
        URI uri = null;
        try {
            uri = url.toURI();
            String path = uri.toString();

            if (path.startsWith("jar")) {
                // [jar:file:/D:/games/Minecraft/instances/mc%201.4.7/minecraft/coremods/mod_monnef_core_0.4.12.jar!/monnef/core/Library.class]
                Pattern patty = Pattern.compile("^(?:jar:file:)*/(.*)!/.*?$");

                Matcher match = patty.matcher(path);
                if (match.find()) {
                    path = match.group(1);
                } else {
                    throw new RuntimeException("cannot parse path to my jar");
                }
            }

            MonnefCorePlugin.Log.printFine("my jar's path: [" + path + "]");
            file = new File(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("cannot detect my path");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("cannot detect my path, uri: [" + (uri == null ? "NULL" : uri.toString()) + "]");
        }

        return file.getAbsolutePath();
    }

    public static String getActualPath() {
        String path;
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("cannot detect current path");
        }

        return path;
    }
}
