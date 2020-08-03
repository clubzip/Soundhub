package com.example.useretrofit2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class ServerFileListActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewCustomAdapter adapter;
    String filePath;
    String fileDir;
    String AppName = "AppName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_file_list);

        listView = (ListView) findViewById(R.id.lv_server_file);
        adapter = new ListViewCustomAdapter();

        downLoadServerFileList serverFileList = (downLoadServerFileList) new downLoadServerFileList().execute("meaningless text^^");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                PopupMenu popup = new PopupMenu(ServerFileListActivity.this, view);
                ServerFileListActivity.this.getMenuInflater().inflate(R.menu.menu_serverfile, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.downloadServerFile:

                                ListViewCustomDTO dto = (ListViewCustomDTO) adapter.getItem(position);
                                String f_name = dto.getName();
                                downLoad2App down2app = (downLoad2App) new downLoad2App().execute(f_name);
                                Toast.makeText(ServerFileListActivity.this, "다운로드 되었습니다." + f_name, Toast.LENGTH_SHORT).show();

                                break;

                        }

                        return false;
                    }
                });

                popup.show();
                return;
            }
        });








        listView.setAdapter(adapter);

    }


    public class downLoadServerFileList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            String upLoadServerUri = "http://192.168.0.112:8077/downloadServerFileList";

            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try{ // open a URL connection to the Servlet

                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "text/html");
                conn.setRequestProperty("Accept", "text/html");


                OutputStream outStream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));

                writer.write("sample_text");
                writer.flush();
                writer.close();


                InputStream is = conn.getInputStream();

                reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();


            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try{
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            //return serverResponseCode;
            return "";
        } // end upLoad2Server

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //System.out.println("result");

            String[] response = result.split("/");

            for(int i=0;i<response.length; i++){
                ListViewCustomDTO dto = new ListViewCustomDTO();
                dto.setName(response[i]);
                adapter.addItem(dto);
            }

            adapter.notifyDataSetChanged();

        }

    }







}


