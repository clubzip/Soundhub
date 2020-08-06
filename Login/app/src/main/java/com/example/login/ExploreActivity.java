package com.example.login;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.Connection.RequestHttpConnection;
import com.example.login.myproject.MyProjectActivity;

import org.json.JSONArray;
import org.json.JSONObject;

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
        url = "http://192.168.0.102:3001/api/all";

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
                JSONObject jObject = new JSONObject(s);

                ArrayList<Music> musics = new ArrayList<Music>();
                JSONArray jsonArray = jObject.getJSONArray("musics");

                for (int i=0; i < jsonArray.length(); i++) {

                    ArrayList<Commit> commits = new ArrayList<Commit>();
                    ArrayList<Request> requests = new ArrayList<Request>();

                    JSONObject item = jsonArray.getJSONObject(i);
                    JSONArray JsonCommits = item.getJSONArray("commits");
                    JSONArray JsonRequests = item.getJSONArray("requests");

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

                    System.out.println(item);

                    Music music = new Music(item.getString("create_date"), item.getString("projectID"), item.getString("description"), item.getString("like"), item.getString("last_update"), commits, requests, item.getString("admin"));
                    musics.add(music);
                }

                ListView listView = findViewById(R.id.lv_musics);
                MusicAdapter musicAdapter = new MusicAdapter(getApplicationContext(), R.layout.activity_explore, musics);
                listView.setAdapter(musicAdapter);

            }catch (Exception e) {e.printStackTrace();}
        }
    }

}
