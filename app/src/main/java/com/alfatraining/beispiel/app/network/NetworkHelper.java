package com.alfatraining.beispiel.app.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkHelper {

    public static InputStream getHttpResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Accept", "text/html");
        conn.connect();

        if (conn.getResponseCode() == 200) {
            return new BufferedInputStream(conn.getInputStream());
        } else {
            return null;
        }
    }
}
