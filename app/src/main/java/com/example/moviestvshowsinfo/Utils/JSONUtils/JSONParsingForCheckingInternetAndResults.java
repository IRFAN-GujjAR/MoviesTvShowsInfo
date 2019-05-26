package com.example.moviestvshowsinfo.Utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This is final class, so will use methods of this class to check the internet connection
 * and results found or not.
 */
public final class JSONParsingForCheckingInternetAndResults {


    private static URL createUrl(String string) {
        URL url = null;
        try {
            url = new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    private static Boolean[] makeHttpRequestForResponse(URL url) {

        Boolean[] connectionAndResults = new Boolean[2];
        /**
         * {@connectionAndResults[0]}  Index 0=Connected to internet or not
         * {@connectionAndResults[1]}  Index 1=Results found or not
         */
        connectionAndResults[0] = false;
        connectionAndResults[1] = false;

        String jsonResponse = "";
        if (url == null) {
            return connectionAndResults;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {

                /**
                 * Setting internet connection value to true.
                 */
                connectionAndResults[0] = true;

                inputStream = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");

                while (scanner.hasNext()) {
                    jsonResponse = scanner.next();
                }
                scanner.close();
            } else {
                return connectionAndResults;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray result = jsonObject.optJSONArray("results");
            if (result.length() == 0) {
                /**
                 * Setting results found to true
                 */
                connectionAndResults[1] = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return connectionAndResults;
    }

    public static Boolean[] getConnectionAndResults(String s) {
        URL url = createUrl(s);
        Boolean[] connectionAndResults = makeHttpRequestForResponse(url);
        return connectionAndResults;
    }
}
