/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import monnef.core.utils.StringsHelper;
import monnef.core.utils.WebHelper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionHelper {
    private static Pattern versionPattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)");
    //public static final String URL = "http://366.hopto.org/jaffas_version.txt";
    //public static final String URL = "http://jaffas.maweb.eu/jaffas_version.php";
    public static final String URL = monnef.core.Reference.URL_JAFFAS + "/jaffas_version.php";

    public static Integer[] GetVersionNumbers(String data) {
        Matcher out = versionPattern.matcher(data);

        if (!out.matches()) {
//            throw new Exception("group count != 3");
            return null;
        }

        Integer[] res;
        try {
            res = new Integer[]{
                    Integer.valueOf(out.group(1)),
                    Integer.valueOf(out.group(2)),
                    Integer.valueOf(out.group(3))
            };
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            return null;
        }

        return res;
    }

    static String getVersionText(String name, String version) {
        String data = null;

        ArrayList<String> lines = new ArrayList<String>();
        if (WebHelper.getLinesTillFooter(prepareURL(URL, StringsHelper.getMD5(name), version), StringsHelper.getMD5(chopName(name)), lines)) {
            if (lines.size() > 0) {
                data = lines.get(0).trim();
            }
        }

        return data;
    }

    private static String prepareURL(String url, String nameHashed, String version) {
        return String.format("%s?name=%s&version=%s", url, nameHashed, version);
    }

    private static String chopName(String name) {
        if (name.length() <= 4) return name;
        return name.substring(0, 2) + name.substring(name.length() - 2, name.length());
    }

    public static int compareVersions(Integer[] ver1, Integer[] ver2) {
        int ver1i = ver1[0] * 100 * 100 + ver1[1] * 100 + ver1[2];
        int ver2i = ver2[0] * 100 * 100 + ver2[1] * 100 + ver2[2];

        if (ver1i > ver2i) return 1;
        if (ver1i == ver2i) return 0;
        if (ver1i < ver2i) return -1;

        throw new RuntimeException("eh, what?");
    }

    public static String versionToString(Integer[] ver) {
        return ver[0] + "." + ver[1] + "." + ver[2];
    }
}
