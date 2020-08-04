package com.example.useretrofit2.myproject;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.useretrofit2.ListViewCustomAdapter;
import com.example.useretrofit2.R;

public class MyProjectActivity extends AppCompatActivity {

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


    class MyThread extends Thread {
        @Override
        public void run(){//Thread 시작할때 콜백되는 메서드
            //시크바 막대기 조금씩 움직이기 (노래 끝날 때 까지 반복)
            while(isPlaying){
                sb.setProgress(mediaPlayer.getCurrentPosition());
                int played = (mediaPlayer.getCurrentPosition()) / 1000;
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
        setContentView(R.layout.activity_myproject);


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

        mediaPlayer = MediaPlayer.create(MyProjectActivity.this, R.raw.wish);
        mediaPlayer.setLooping(false);//무한반복 x

        int playtime = mediaPlayer.getDuration(); // 노래 재생시간(ms)
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
        adapter.setActivity(MyProjectActivity.this);

        ListViewMyProjectDTO dto_1 = new ListViewMyProjectDTO();
        dto_1.setName("Vocal Added");
        dto_1.setMp3_id(R.raw.fiction);
        adapter.addItem(dto_1);

        ListViewMyProjectDTO dto_2 = new ListViewMyProjectDTO();
        dto_2.setName("Drum Added");
        dto_2.setMp3_id(R.raw.meteor);
        adapter.addItem(dto_2);

        ListViewMyProjectDTO dto_3 = new ListViewMyProjectDTO();
        dto_3.setName("Guitar Added");
        dto_3.setMp3_id(R.raw.home);
        adapter.addItem(dto_3);

        ListViewMyProjectDTO dto_4 = new ListViewMyProjectDTO();
        dto_4.setName("Keyboard Added");
        dto_4.setMp3_id(R.raw.empty);
        adapter.addItem(dto_4);

        ListViewMyProjectDTO dto_5 = new ListViewMyProjectDTO();
        dto_5.setName("Vocal2 Added");
        dto_5.setMp3_id(R.raw.missing);
        adapter.addItem(dto_5);

        ListViewMyProjectDTO dto_6 = new ListViewMyProjectDTO();
        dto_6.setName("Drum2 Added");
        dto_6.setMp3_id(R.raw.stupid);
        adapter.addItem(dto_6);

        ListViewMyProjectDTO dto_7 = new ListViewMyProjectDTO();
        dto_7.setName("Guitar2 Added");
        dto_7.setMp3_id(R.raw.ifyou);
        adapter.addItem(dto_7);

        ListViewMyProjectDTO dto_8 = new ListViewMyProjectDTO();
        dto_8.setName("Keyboard2 Added");
        dto_8.setMp3_id(R.raw.empty);
        adapter.addItem(dto_8);

        listView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
