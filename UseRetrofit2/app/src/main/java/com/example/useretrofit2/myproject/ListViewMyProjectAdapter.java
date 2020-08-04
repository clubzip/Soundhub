package com.example.useretrofit2.myproject;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.useretrofit2.ListViewCustomDTO;
import com.example.useretrofit2.R;

import java.util.ArrayList;

public class ListViewMyProjectAdapter extends BaseAdapter{

    //TODO : mediaplayer release

    private AppCompatActivity m_activity;
    private ArrayList<ListViewMyProjectDTO> listCustom = new ArrayList<>();

    public void setActivity(AppCompatActivity activity){this.m_activity = activity;}

    @Override
    public int getCount() {
        return listCustom.size();
    }

    @Override
    public Object getItem(int position) {
        return listCustom.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.commited_item, null, false);

            holder = new CustomViewHolder();

            holder.textName = (TextView) convertView.findViewById(R.id.commit_title);
            holder.play = (ImageButton) convertView.findViewById(R.id.myproject_commit_play);
            holder.stop = (ImageButton) convertView.findViewById(R.id.myproject_commit_pause);
            holder.seekBar = (SeekBar) convertView.findViewById(R.id.myproject_commit_seekBar);
            holder.show_time = (TextView) convertView.findViewById(R.id.show_commit_play_time);


            convertView.setTag(holder);


        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        ListViewMyProjectDTO dto = listCustom.get(position);


        holder.textName.setText(dto.getName());
        holder.mediaPlayer = dto.getMediaPlayer();
        holder.init_media(position);
        SynchSeekBar syn = new SynchSeekBar();
        syn.dto = dto;
        syn.sb = holder.seekBar;
        syn.start();

        return convertView;
    }





    class CustomViewHolder extends Activity {

        TextView textName;
        ImageButton play;
        ImageButton stop;
        ListViewMyProjectDTO dto;
        MediaPlayer mediaPlayer;


        int duration;
        String duration_str;
        TextView show_time;
        int played_sec = 0;
        int played_minute = 0;
        SeekBar seekBar;//음악 재생위치를 나타내는 시크바
        boolean isPlaying = false; // 재생중?



        class MyThread extends Thread {
            @Override
            public void run(){//Thread 시작할때 콜백되는 메서드
                //시크바 막대기 조금씩 움직이기 (노래 끝날 때 까지 반복)
                while(isPlaying){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());

                    int played = (mediaPlayer.getCurrentPosition()) / 1000;
                    if(played_minute != played / 60 || played_sec != played % 60) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(played % 60 < 10) show_time.setText(played / 60 + ":0" + played % 60 + " / " + duration_str);
                                else show_time.setText(played / 60 + ":" + played % 60 + " / " + duration_str);
                            }
                        });
                    }
                    played_minute = played / 60;
                    played_sec = played % 60;

                }
            }
        }


        public void init_media(int position){
            dto = listCustom.get(position);

            int mp3_id = dto.getMp3_id();
            if(mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(m_activity, mp3_id);
                dto.setProgress(0);
            }
            dto.setMediaPlayer(mediaPlayer);
            mediaPlayer.setLooping(false);//무한반복 x

            duration = mediaPlayer.getDuration(); // 노래 재생시간(ms)
            if ((duration / 1000) % 60 < 10)
                duration_str = (duration / 1000) / 60 + ":0" + (duration / 1000) % 60;
            else duration_str = (duration / 1000) / 60 + ":" + (duration / 1000) % 60;

            dto.setDuration(duration);
            dto.setDurationString(duration_str);

            if(dto.getProgress() != 0) {
                if((dto.getProgress() / 1000) % 60 > 10 ) show_time.setText((dto.getProgress() / 1000) / 60 + ":" + (dto.getProgress() / 1000) % 60 + " / " + dto.getDurationString());
                else show_time.setText((dto.getProgress() / 1000) / 60 + ":0" + (dto.getProgress() / 1000) % 60 + " / " + dto.getDurationString());
            }

            else show_time.setText("0:00 / " + dto.getDurationString());

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar sb, int progress, boolean b) {
                    if (sb.getMax() == progress && !dto.checkInitialPlay()) {//재생 끝났을 때
                        isPlaying = false;
                        mediaPlayer.pause();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (!dto.checkInitialPlay()) {
                        isPlaying = false;
                        mediaPlayer.pause();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (!dto.checkInitialPlay()) {
                        isPlaying = true;
                        int new_pos = seekBar.getProgress(); // 사용자가 움직여놓은 위치
                        mediaPlayer.seekTo(new_pos);
                        mediaPlayer.start();
                        new MyThread().start();
                    }
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dto.checkInitialPlay()) {//최초 play

                        seekBar.setMax(duration); // 시크바 최대 범위를 노래 재생시간으로 설정
                        seekBar.setProgress(0);
                        mediaPlayer.start();

                        new MyThread().start(); // 시크바 그려줄 thread 시작
                        isPlaying = true;
                        dto.setInitial_play();

                    } else {
                        mediaPlayer.seekTo(dto.getProgress());//멈춘 시점부터 재시작
                        mediaPlayer.start();
                        isPlaying = true;
                        new MyThread().start();
                    }

                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!dto.checkInitialPlay()) {
                        System.out.println("not initial play@@@@@@@@@@@@@@@@@");
                        dto.setProgress(mediaPlayer.getCurrentPosition());

                        mediaPlayer.pause();
                        isPlaying = false;
                    }
                }
            });



        }


    }


    public void addItem(ListViewMyProjectDTO dto) {

        listCustom.add(dto);
    }

    public void delItem(ListViewMyProjectDTO dto){
        listCustom.remove(dto);
    }

    public void modifyItem(int i, ListViewMyProjectDTO dto){
        listCustom.set(i, dto);
    }

    class SynchSeekBar extends Thread {
        SeekBar sb;
        ListViewMyProjectDTO dto;
        @Override
        public void run(){//Thread 시작할때 콜백되는 메서드
            sb.setProgress(dto.getProgress());
        }
    }


}