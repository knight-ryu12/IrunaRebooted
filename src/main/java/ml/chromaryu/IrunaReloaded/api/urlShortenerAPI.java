package ml.chromaryu.IrunaReloaded.api;

import jdk.nashorn.internal.parser.JSONParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by chroma on 16/07/02.
 */
public class urlShortenerAPI {

    public static String shorten(String u) {
        URL url = null;
        String content = null;
        try {
            url = new URL("https://is.gd/create.php?format=simple&url=" + u);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Errored!");
        }
        try {
            assert url != null;
            //URLConnection con = url.openConnection();
            Scanner scanner = new Scanner(url.openStream(), "UTF-8");
            content = scanner.useDelimiter("//A").next();
            scanner.close();
        } catch(IOException e) {
            throw new IllegalStateException("Errored!");
        }
        return content;
    }
}
