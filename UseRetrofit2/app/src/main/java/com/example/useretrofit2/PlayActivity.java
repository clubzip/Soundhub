package com.example.useretrofit2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {

    String AppName = "AppName";
    String fileDir = "/sdcard/" + AppName + "/";
    VideoView v_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        v_view = (VideoView) findViewById(R.id.videoView1);

        MediaController mecon = new MediaController(PlayActivity.this);
        v_view.setMediaController(mecon);

        Intent intent = getIntent();
        String f_name = intent.getStringExtra("filename");

        String path = fileDir + f_name;

        v_view.setVideoPath(path);
        v_view.requestFocus();
        v_view.start();


        v_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();
            }
        });



    }

    //액티비티가 메모리에서 사라질때
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(v_view != null) v_view.stopPlayback();
    }
}
