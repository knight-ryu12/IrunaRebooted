package ml.chromaryu.IrunaReloaded.api;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by chroma on 16/07/11.
 */
public class pastebinApi {
    private static JSONParser parser = new JSONParser();
    public static String paste(String w) {
        URLConnection connection = null;
        HttpURLConnection Hcon = null;
        URL url = null;
        String content;
        JSONObject Jobj = null;
        try {
            url = new URL("http://www.hastebin.com/documents");
            connection = url.openConnection();
            Hcon = (HttpURLConnection) connection;
            Hcon.setDoInput(true);
            Hcon.setDoOutput(true);
            Hcon.setRequestMethod("POST");
            DataOutputStream wr = new DataOutputStream(Hcon.getOutputStream());
            //wr.writeBytes(urlParameters);
            wr.writeBytes(w);
            wr.flush();
            wr.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Jobj = (JSONObject) parser.parse(rd);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            assert Hcon != null;
            Hcon.disconnect();
        }
        return Jobj.toString();
    }
}
