package com.example.login;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.Connection.RequestHttpConnection;
import com.example.login.myproject.MyProjectActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {

    String id, result, url;
    ContentValues exploreValues = new ContentValues();
    ImageButton btn_mypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        Intent receivedIntent = getIntent();
        id = receivedIntent.getStringExtra("userid");
        System.out.println("What is id? : " + id);
        url = "http://192.168.0.112:3001/api/all";

        exploreValues.put("userid",id);
        ExploreActivity.ExploreNetworkTask exploreTask = new ExploreActivity.ExploreNetworkTask(url,exploreValues);
        exploreTask.execute();
        btn_mypage = findViewById(R.id.btn_mypage);

        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExploreActivity.this , MyProjectActivity.class);
                intent.putExtra("userid",id);
                startActivity(intent);
            }
        });
    }

    private class ExploreNetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public ExploreNetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
            result = requestHttpConnection.request(url, values);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                ListView listView = findViewById(R.id.lv_musics);
                final MusicAdapter musicAdapter = new MusicAdapter();
                musicAdapter.setActivity(ExploreActivity.this);
                JSONObject jObject = new JSONObject(s);

                ArrayList<Music> musics = new ArrayList<Music>();
                JSONArray jsonArray = jObject.getJSONArray("musics");

                for (int i=0; i < jsonArray.length(); i++) {

                    ArrayList<Commit> commits = new ArrayList<Commit>();
                    ArrayList<Request> requests = new ArrayList<Request>();

                    JSONObject item = jsonArray.getJSONObject(i);
                    JSONArray JsonCommits = item.getJSONArray("commits");
                    JSONArray JsonRequests = item.getJSONArray("requests");
/*
                    for (int j=0; j < JsonCommits.length(); j++) {
                        JSONObject JsonCommit = JsonCommits.getJSONObject(j);
                        Commit commit = new Commit(JsonCommit.getString("date"), JsonCommit.getString("artist"), JsonCommit.getString("commitID"), JsonCommit.getString("category"));
                        commits.add(commit);
                    }

                    for (int j=0; j < JsonRequests.length(); j++) {
                        JSONObject JsonRequest = JsonRequests.getJSONObject(j);
                        Request request = new Request(JsonRequest.getString("date"), JsonRequest.getString("artist"), JsonRequest.getString("commitID"), JsonRequest.getString("category"));
                        requests.add(request);
                    }
*/
                    System.out.println(item.getString("last_update"));

                    MusicView dto = new MusicView();
                    dto.setName(item.getString("projectID"));
                    dto.setMp3_name(item.getString("projectID"));
                    dto.setAdmin(item.getString("admin"));
                    dto.setUpdate(item.getString("last_update"));


                    musicAdapter.addItem(dto);
                    musicAdapter.notifyDataSetChanged();

                    //Music music = new Music(item.getString("create_date"), item.getString("projectID"), item.getString("description"), item.getString("like"), item.getString("last_update"), commits, requests, item.getString("admin"));
                    //musics.add(music);

                    //Download master track

                    /*String mp3_name = json.getString("last_update");
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dat;
                    try {
                        dat = transFormat.parse(mp3_name);
                        dto.setMp3_name("master_" + dat.toString());
                        System.out.println("converted date : " + dat.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/

                    //downLoadExplore2App d2a = (downLoadExplore2App) new downLoadExplore2App().execute("master_" + item.getString("last_update") + ".mp3");
                    //downLoadExplore2App d2a = (downLoadExplore2App) new downLoadExplore2App().execute(item.getString("projectID") + ".mp3");
                    //System.out.println("master_" + item.getString("last_update") + ".mp3");



                }


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ExploreActivity.this, MusicActivity.class);
                        //intent.putExtra("last_update", last_update);
                        intent.putExtra("projectID", ((MusicView)musicAdapter.getItem(i)).getName());

                        startActivity(intent);
                    }
                });




                listView.setAdapter(musicAdapter);


            }catch (Exception e) {e.printStackTrace();}
        }
    }



    public class downLoadExplore2App extends AsyncTask<String, String, String> {

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
            //System.out.println("result");
            //adapter.notifyDataSetChanged();

        }

    }



}
