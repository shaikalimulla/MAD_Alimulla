package com.example.alimu.restaurantsearch;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alimu on 12/3/2016.
 */

public class ExtractUrlData {
    public String processUrl(String strUrl) throws IOException {
        String data = "";
        InputStream inStream = null;
        HttpURLConnection urlConn = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConn = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConn.connect();

            // Reading data from url
            inStream = urlConn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            inStream.close();
            urlConn.disconnect();
        }
        return data;
    }
}
