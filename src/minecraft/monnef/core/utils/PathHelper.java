package monnef.core.utils;

import monnef.core.Library;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: moen
 * Date: 03/03/13
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class PathHelper {
    public static String getMyPath() {
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
