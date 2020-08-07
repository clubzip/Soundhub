package com.example.login.myproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.example.login.RecordActivity;
import com.example.login.SavePopupActivity;
import com.example.login.myproject.requests.RequestsFragment;
import com.example.login.upLoad2Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class MyProjectFragment extends Fragment {

    private ListView listView;
    private ProjectListAdapter adapter;
    private String userid = "myuserid";
    MediaRecorder recorder;
    private final int requestCodeSaveFile = 0;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_first, container, false);


        listView = (ListView) root.findViewById(R.id.lv_myproject_list);
        adapter = new ProjectListAdapter();

        adapter.setActivity((AppCompatActivity) getActivity());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                System.out.println("Item clicked!");
                Intent intent = new Intent(root.getContext(), ProjectDetailActivity.class);

                intent.putExtra("projectID", ((ProjectListDTO) adapter.getItem(i)).getName());

                startActivity(intent);

            }
        });

        final Button start_rec = (Button) root.findViewById(R.id.start_record);
        final Button stop_rec = (Button) root.findViewById(R.id.stop_record);

        start_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_rec.setVisibility(View.GONE);
                stop_rec.setVisibility(View.VISIBLE);
                recordAudio();
            }
        });

        stop_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop_rec.setVisibility(View.GONE);
                start_rec.setVisibility(View.VISIBLE);
                stopRecording();
            }
        });


        MyProjectListTask mytask = (MyProjectListTask) new MyProjectListTask().execute("http://192.168.0.112:3001/api/user/detail");

        listView.setAdapter(adapter);

        return root;
    }

    private void recordAudio() {
        recorder = new MediaRecorder();

        /* 그대로 저장하면 용량이 크다.
         * 프레임 : 한 순간의 음성이 들어오면, 음성을 바이트 단위로 전부 저장하는 것
         * 초당 15프레임 이라면 보통 8K(8000바이트) 정도가 한순간에 저장됨
         * 따라서 용량이 크므로, 압축할 필요가 있음 */
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 어디에서 음성 데이터를 받을 것인지
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 압축 형식 설정
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        recorder.setOutputFile("/sdcard/AppName/record.mp3");

        try {
            recorder.prepare();
            recorder.start();

            Toast.makeText(getContext(), "Record Started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Toast.makeText(getContext(), "Record Stopped", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), SavePopupActivity.class);
            startActivityForResult(intent, requestCodeSaveFile);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0://Save new file(Save popup)
                String newfilename = data.getStringExtra("filename");
                File filePre = new File("/sdcard/AppName/record.mp3");
                File fileNow = new File("/sdcard/AppName/" + newfilename + ".mp3");

                if(filePre.renameTo(fileNow)){

                    upLoad2Server u2s = (upLoad2Server) new upLoad2Server().execute("/sdcard/AppName/" + newfilename + ".mp3");

                    soundupload su = (soundupload) new soundupload().execute("/sdcard/AppName/" + newfilename + ".mp3");

                    Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Fail to save", Toast.LENGTH_SHORT).show();
                }


                break;
        }



    }



    public class MyProjectListTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {

            try {
                //JSONObject 만들고 key value 형식으로 값 저장
                JSONObject jsonObject = new JSONObject();

                if(urls[0].contains("/api/user/detail")){//project detail request
                    jsonObject.accumulate("userid", userid);
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
                JSONArray json_array = json.getJSONArray("projects");
                System.out.println(json_array);

                for(int i=0;i<json_array.length();i++){
                    String proj_name = json_array.getString(i);//commitObject includes date, artistID, commitID, category. We need commitID.


                    MyProjectDetailTask getProjDetail = (MyProjectDetailTask) new MyProjectDetailTask();
                    getProjDetail.reqProjectID = proj_name;
                    getProjDetail.execute("http://192.168.0.112:3001/api/project/detail");

                    //TODO : Problem - when download dynamically, mp3 file ends earlier than expected.
                    //downLoadProjList2App d2app = (downLoadProjList2App) new downLoadProjList2App().execute(proj_name + ".mp3");

                    System.out.println("mp3 file name : " + proj_name);
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

            //System.out.println(result);

        }

    }


    public class MyProjectDetailTask extends AsyncTask<String, String, String> {

        String reqProjectID;

        @Override
        protected String doInBackground(String... urls) {

            try {
                //JSONObject 만들고 key value 형식으로 값 저장
                JSONObject jsonObject = new JSONObject();

                if(urls[0].contains("/api/project/detail")){//project detail request
                    jsonObject.accumulate("projectID", reqProjectID);
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
                String update = json.getString("last_update");
                JSONArray admins = json.getJSONArray("admin");
                String admin_name = "";

                ProjectListDTO dto = new ProjectListDTO();
                dto.setName(reqProjectID);
                dto.setUpdate(update);

                for(int i=0;i<admins.length();i++){
                    admin_name += admins.getString(i);
                    if(i < admins.length() - 1) admin_name += " & ";
                }
                dto.setAdmin(admin_name);

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


                System.out.println(reqProjectID);
                adapter.addItem(dto);
                adapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }

            //System.out.println(result);

        }

    }


    public class downLoadProjList2App extends AsyncTask<String, String, String> {

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



    public class soundupload extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... urls) {

            try {
                //JSONObject 만들고 key value 형식으로 값 저장
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("projectID", "testgroup");
                jsonObject.accumulate("userid", "myuserid");
                jsonObject.accumulate("commitID", "New Record Commit");
                jsonObject.accumulate("category", "pop");
                jsonObject.accumulate("files", new File(urls[0]));

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {

                    URL url = new URL("http://192.168.0.112:3001/api/upload");
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
            System.out.println(result);


        }

    }



}
