package com.example.useretrofit2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RecordActivity extends AppCompatActivity implements SurfaceHolder.Callback{


    String AppName = "AppName";
    private String filePath;
    private String fileDir;
    private Camera cam;
    private MediaRecorder mediaRecorder;
    private SurfaceView sv;
    private SurfaceHolder sh;
    private boolean recording = false;
    private final int requestCodeSaveFile = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Button UploadButton = (Button) findViewById(R.id.uploadBtn);
        Button RecordButton = (Button) findViewById(R.id.recordBtn);

        Button StopButton = (Button) findViewById(R.id.stopBtn);

        fileDir = "/sdcard/" + AppName + "/";
        filePath = fileDir + AppName + "_record.mp4";

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                upLoad2Server uls = (upLoad2Server) new upLoad2Server().execute(filePath);
            }
        });


        RecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RecordActivity.this, "Record Started", Toast.LENGTH_LONG).show();

                        try{

                            mediaRecorder = new MediaRecorder();
                            cam.unlock();
                            mediaRecorder.setCamera(cam);
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                            //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                            //mediaRecorder.setAudioEncoder(3);
                            //mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                            mediaRecorder.setOrientationHint(90);
                            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                            mediaRecorder.setOutputFile(filePath);
                            mediaRecorder.setPreviewDisplay(sh.getSurface());
                            mediaRecorder.prepare();
                            mediaRecorder.start();

                            recording = true;



                        } catch (final Exception ex){
                            ex.printStackTrace();
                            mediaRecorder.release();
                            return;
                        }

                    }
                });

            }
        });



        StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recording){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    cam.lock();
                    recording = false;
                    Toast.makeText(RecordActivity.this, "Record Stopped", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(view.getContext(), SavePopupActivity.class);
                    startActivityForResult(intent, requestCodeSaveFile);


                }
            }
        });

        setting();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0://Save new file(Save popup)
                String newfilename = data.getStringExtra("filename");
                File filePre = new File(filePath);
                File fileNow = new File(fileDir + newfilename + ".mp4");

                if(filePre.renameTo(fileNow)){
                    Toast.makeText(RecordActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RecordActivity.this, "저장 실패", Toast.LENGTH_SHORT).show();
                }


                break;
        }



    }



    private void setting(){
        cam = Camera.open();
        cam.setDisplayOrientation(90);
        sv = (SurfaceView)findViewById(R.id.sv);
        sh = sv.getHolder();
        sh.addCallback(this);
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }


    public void surfaceCreated(SurfaceHolder holder){
        try{
            if(cam == null){
                cam.setPreviewDisplay(holder);
                cam.startPreview();
            }
        } catch (IOException e){

        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    public void refreshCamera(Camera camera){
        if(sh.getSurface() == null){
            //preview surface does not exist
            return;
        }

        try{
            cam.stopPreview();
        } catch (Exception e) {

        }

        setCamera(camera);
        try{
            cam.setPreviewDisplay(sh);
            cam.startPreview();
        } catch (Exception e){

        }

    }

    public void setCamera(Camera camera){
        cam = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }


    public class upLoad2Server extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            String upLoadServerUri = "http://192.168.0.112:8077/uploadvideo";
            String fileName = urls[0];

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream inStream = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            String responseFromServer = "";
            int serverResponseCode = 0;

            File sourceFile = new File(urls[0]);
            if(!sourceFile.isFile()){
                Log.e("Huzza", "Source File Does not exist");
                return "";
            }


            try{ // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);


                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);


                bytesAvailable = fileInputStream.available(); // create a buffer of maximum size
                Log.i("Huzza", "Initial .available : " + bytesAvailable);

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while(bytesRead > 0){
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necessary after fill data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("Upload file to server", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                // close streams
                Log.i("Upload file to server", fileName + " File is written");
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //this block will give the response of upload link
            try{

                InputStream is = conn.getInputStream();

                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader rd = new BufferedReader(isr);
                String line;
                while ((line = rd.readLine()) != null){
                    Log.i("Huzza", "RES Message: " + line);
                }
                rd.close();

            } catch (IOException ioex){
                Log.e("Huzza", "error: " + ioex.getMessage(), ioex);
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


}
