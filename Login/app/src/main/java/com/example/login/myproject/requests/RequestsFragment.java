package com.example.login.myproject.requests;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

public class RequestsFragment extends Fragment {

    private ListView listView;
    private RequestsListAdapter adapter;
    private String userid = "myuserid";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_third, container, false);

        listView = (ListView) root.findViewById(R.id.lv_myproject_request_list);
        adapter = new RequestsListAdapter();

        adapter.setActivity((AppCompatActivity) getActivity());

        listView.setAdapter(adapter);

        MyProjectRequestsListTask mytask = (MyProjectRequestsListTask) new MyProjectRequestsListTask().execute("http://192.168.0.112:3001/api/user/detail");

        return root;
    }

    public class MyProjectRequestsListTask extends AsyncTask<String, String, String> {

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
                JSONArray json_array = json.getJSONArray("request_list");
                System.out.println(json_array);

                for(int i=0;i<json_array.length();i++){
                    JSONObject commit_elem = json_array.getJSONObject(i);
                    String proj_name = commit_elem.getString("projectID");//commitObject includes date, artistID, commitID, category. We need commitID.


                    MyProjectRequestsDetailTask getProjDetail = (MyProjectRequestsDetailTask) new MyProjectRequestsDetailTask();
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


    public class MyProjectRequestsDetailTask extends AsyncTask<String, String, String> {

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

                RequestsListDTO dto = new RequestsListDTO();
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

}
