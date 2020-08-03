package com.example.useretrofit2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class downLoad2App extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... urls) {
        String upLoadServerUri = "http://192.168.0.112:8077/downloadvideo";
        String fileName = urls[0];

        HttpURLConnection conn = null;


        int bytesRead, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;


        try{ // open a URL connection to the Servlet

            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/html");


            OutputStream outStream = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));

            writer.write(fileName);
            writer.flush();
            writer.close();

            //File downloaded = new File("/sdcard/AppName/target.mp4");
            FileOutputStream fos = new FileOutputStream("/sdcard/AppName/" + fileName);

            InputStream is = conn.getInputStream();

            //bytesAvailable = is.available(); // create a buffer of maximum size

            bufferSize = Math.min(conn.getContentLength(), maxBufferSize);
            //bufferSize = conn.getContentLength();
            buffer = new byte[bufferSize];

            //int cnt = 0;

            while(true){

                //bytesAvailable = is.available();
                bufferSize = Math.min(conn.getContentLength(), maxBufferSize);
                //System.out.println("content length : " + conn.getContentLength() + " / maxBuffersize : " + maxBufferSize);
                bytesRead = is.read(buffer, 0, bufferSize);

                if(bytesRead <= 0) {
                    break;
                }
                fos.write(buffer, 0, bytesRead);
                //System.out.println("response count : " + cnt + " !!!!!!!!!!!!!!!!!!");
                //cnt += 1;
            }

            System.out.println("end streaming");

            fos.close();
            is.close();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //return serverResponseCode;
        return "";
    } // end upLoad2Server

    @SuppressLint("ResourceType")
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        System.out.println("result");

    }

}