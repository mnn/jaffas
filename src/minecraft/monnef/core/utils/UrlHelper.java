package monnef.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class UrlHelper {

    public static final int TIMEOUT = 1000;

    public static boolean pageExists(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setConnectTimeout(TIMEOUT);
            huc.setRequestMethod("HEAD");
            huc.setInstanceFollowRedirects(true);
            return huc.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getNames(String url, Set<String> out) {
        try {
            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setInstanceFollowRedirects(true);
            int responseCode = huc.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(huc.getInputStream()));
            String line;
            out.clear();
            while ((line = reader.readLine()) != null) {
                out.add(line.trim());
            }
            huc.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
