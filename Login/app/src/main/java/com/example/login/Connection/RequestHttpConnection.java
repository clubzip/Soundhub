package com.example.login.Connection;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RequestHttpConnection {

    public String request(String _url, ContentValues params) {

        HttpURLConnection urlconn = null;
        StringBuffer sbParams = new StringBuffer();

        if (params == null)
            sbParams.append("");

        else {
            boolean isAnd = false;
            String key;
            String value;

            for (Map.Entry<String, Object> parameter : params.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();

                if (isAnd)
                    sbParams.append("&");

                sbParams.append(key).append("=").append(value);

                if (!isAnd)
                    if (params.size() >= 2)
                        isAnd = true;
            }

        }

        try {

            URL url = new URL(_url);
            urlconn = (HttpURLConnection) url.openConnection();

            urlconn.setRequestMethod("POST");
            urlconn.setRequestProperty("Accept_Charset", "UTF-8");
            urlconn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

            urlconn.setDoOutput(true);
            urlconn.setDoInput(true);

            String strParams = sbParams.toString();
            OutputStream os = urlconn.getOutputStream();

            os.write(strParams.getBytes("UTF-8"));

            os.flush();
            os.close();

            if (urlconn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "UTF-8"));

            String line;
            String page = "";

            while ((line = reader.readLine()) != null) {
                page += line;
            }

            System.out.println("cccccccccccccccccccccccccccc");
            return page;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlconn != null)
                urlconn.disconnect();
        }

    return null;
    }
}