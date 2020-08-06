package com.example.login.myproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ProjectDetailActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    int pos;//재생 멈춘 시점
    boolean initial_play = true;
    private ImageButton play;
    private ImageButton pause;
    String duration;
    TextView show_time;
    int played_sec = 0;
    int played_minute = 0;
    SeekBar sb;//음악 재생위치를 나타내는 시크바
    boolean isPlaying = false; // 재생중?
    private ListView listView;
    private ListViewMyProjectAdapter adapter;
    private Intent intent;


    class MyThread extends Thread {
        @Override
        public void run(){//Thread 시작할때 콜백되는 메서드
            //시크바 막대기 조금씩 움직이기 (노래 끝날 때 까지 반복)
            while(isPlaying){
                sb.setProgress(mediaPlayer.getCurrentPosition());
                final int played = (mediaPlayer.getCurrentPosition()) / 1000;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(played_minute != played / 60 || played_sec != played % 60) {
                            if(played % 60 < 10) show_time.setText(played / 60 + ":0" + played % 60 + " / " + duration);
                            else show_time.setText(played / 60 + ":" + played % 60 + " / " + duration);

                        }
                    }
                });

                played_minute = played / 60;
                played_sec = played % 60;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_detail);


        intent = getIntent();

        String proj_name = intent.getStringExtra("projectID");

        show_time = (TextView) findViewById(R.id.show_play_time);
        sb = (SeekBar) findViewById(R.id.myproject_seekBar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(seekBar.getMax() == progress){//재생 끝났을 때
                    isPlaying = false;
                    mediaPlayer.stop();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isPlaying = true;
                int new_pos = seekBar.getProgress(); // 사용자가 움직여놓은 위치
                mediaPlayer.seekTo(new_pos);
                mediaPlayer.start();
                new MyThread().start();
            }
        });

        mediaPlayer = MediaPlayer.create(ProjectDetailActivity.this, R.raw.meteor);
        mediaPlayer.setLooping(false);//무한반복 x

        final int playtime = mediaPlayer.getDuration(); // 노래 재생시간(ms)
        int minute = (playtime / 1000) / 60;
        int sec = (playtime / 1000) % 60;
        duration = minute + ":" + sec;
        show_time.setText("0:00 / " + duration);

        play = (ImageButton) findViewById(R.id.myproject_play);
        pause = (ImageButton) findViewById(R.id.myproject_pause);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(initial_play) {//최초 play

                    mediaPlayer.start();


                    sb.setMax(playtime); // 시크바 최대 범위를 노래 재생시간으로 설정
                    new MyThread().start(); // 시크바 그려줄 thread 시작
                    isPlaying = true;
                    initial_play = false;
                }

                else{
                    mediaPlayer.seekTo(pos);//멈춘 시점부터 재시작
                    mediaPlayer.start();
                    isPlaying = true;
                    new MyThread().start();
                }

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                isPlaying = false;
            }
        });


        listView = (ListView) findViewById(R.id.lv_myproject_commit);
        adapter = new ListViewMyProjectAdapter();
        adapter.setActivity(ProjectDetailActivity.this);


        listView.setAdapter(adapter);

        MyProjectTask mytask = (MyProjectTask) new MyProjectTask();
        mytask.project_id = proj_name;
        mytask.execute("http://192.168.0.112:3001/api/project/detail");



    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }




    public class MyProjectTask extends AsyncTask<String, String, String> {

        String project_id;

        @Override
        protected String doInBackground(String... urls) {

            try {
                //JSONObject 만들고 key value 형식으로 값 저장
                JSONObject jsonObject = new JSONObject();

                if(urls[0].contains("/api/project/detail")){//project detail request
                    jsonObject.accumulate("projectID", project_id);
                }

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {

                    URL url = new URL(urls[0]);
                    //연결
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST 방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application jSON 형식으로 전송
                    con.setRequestProperty("Accept", "application/json");
                    //con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream 으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미

                    con.connect();

                    //서버로 보내기위해서 스트림 생성
                    OutputStream outStream = con.getOutputStream();

                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    System.out.println("@@@@@@@@@@@@@@@@@end of asynctask");

                    //서버로 부터 받은 값을 리턴
                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //System.out.println(result);
            try {
                JSONObject json = new JSONObject(result);
                JSONArray json_array = json.getJSONArray("commits");

                for(int i=0;i<json_array.length();i++){
                    JSONObject commitObject = json_array.getJSONObject(i);//commitObject includes date, artistID, commitID, category. We need commitID.

                    String mp3_name = commitObject.getString("commitID");

                    ListViewMyProjectDTO dto = new ListViewMyProjectDTO();
                    dto.setName(commitObject.getString("commitID"));
                    dto.setMp3_name(commitObject.getString("commitID"));



                    //TODO : Problem - when download dynamically, mp3 file ends earlier than expected.
                    //downLoadCommit2App d2app = (downLoadCommit2App) new downLoadCommit2App().execute(mp3_name + ".mp3");

                    adapter.addItem(dto);
                    adapter.notifyDataSetChanged();

                    System.out.println("mp3 file name : " + commitObject.getString("commitID"));
                }


                final String proj_id = json.getString("projectID");
                final String summary = json.getString("description");
                String admins = "";
                JSONArray admin_array = json.getJSONArray("admin");
                for(int i=0;i<admin_array.length();i++){
                    admins += admin_array.getString(i);
                    if(i < admin_array.length() - 1) admins += " & ";
                }
                final String update = json.getString("last_update");
                final String like = "★ " + json.getString("like");

                final String finalAdmins = admins;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView title = (TextView) findViewById(R.id.title_myproject);
                        TextView summ_ = (TextView) findViewById(R.id.project_summary);
                        TextView update_ = (TextView) findViewById(R.id.project_update);
                        TextView like_ = (TextView) findViewById(R.id.project_like);
                        TextView admins_ = (TextView) findViewById(R.id.project_admin);

                        title.setText(proj_id);
                        summ_.setText(summary);
                        update_.setText(update);
                        like_.setText(like);
                        admins_.setText(finalAdmins);
                    }
                });







            } catch (JSONException e) {
                e.printStackTrace();
            }

            //System.out.println(result);

        }

    }


    public class downLoadCommit2App extends AsyncTask<String, String, String> {

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
