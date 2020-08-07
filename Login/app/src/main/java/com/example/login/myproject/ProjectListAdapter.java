package com.example.login.myproject;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;

import java.io.IOException;
import java.util.ArrayList;

public class ProjectListAdapter extends BaseAdapter{

    //TODO : mediaplayer release

    private AppCompatActivity m_activity;
    private ArrayList<ProjectListDTO> listCustom = new ArrayList<>();

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
        final CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_list_item, null, false);

            holder = new CustomViewHolder();

            holder.textName = (TextView) convertView.findViewById(R.id.project_list_title);
            holder.textAdmin = (TextView) convertView.findViewById(R.id.tv_admin);
            //holder.textInst = (TextView) convertView.findViewById(R.id.tv_instruments);
            holder.textUpdate = (TextView) convertView.findViewById(R.id.tv_recent);

            holder.play = (ImageButton) convertView.findViewById(R.id.myproject_list_play);
            holder.stop = (ImageButton) convertView.findViewById(R.id.myproject_list_pause);
            holder.seekBar = (SeekBar) convertView.findViewById(R.id.myproject_list_seekBar);
            holder.show_time = (TextView) convertView.findViewById(R.id.show_list_play_time);

            convertView.setTag(holder);

        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        final ProjectListDTO dto = listCustom.get(position);
        if(dto.getMediaPlayer() == null){

            MediaPlayer mp = new MediaPlayer();
            try {
                System.out.println(dto.getMp3_name());

                mp.setDataSource("/sdcard/AppName/" + dto.getMp3_name() + ".mp3");
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            dto.setMediaPlayer(mp);

        }

        holder.textName.setText(dto.getName());
        holder.textAdmin.setText(dto.getAdmin());
        //holder.textInst.setText(dto.getInst());
        holder.textUpdate.setText(dto.getUpdate());

        holder.mediaPlayer = dto.getMediaPlayer();
        int dur = holder.mediaPlayer.getDuration();
        if((dur/1000) % 60 < 10) holder.duration_str = (dur/1000)/60 + ":0" + (dur/1000)%60;
        else holder.duration_str = (dur/1000)/60 + ":" + (dur/1000)%60;


        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean b) {
                if (!(sb.getMax() != progress || dto.checkInitialPlay())) {//재생 끝났을 때
                    dto.setIsPlaying(false);
                    holder.isPlaying = false;
                    holder.mediaPlayer.pause();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (!dto.checkInitialPlay()) {
                    dto.setIsPlaying(false);
                    holder.isPlaying = false;
                    holder.mediaPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb) {
                if (!dto.checkInitialPlay()) {

                    dto.setIsPlaying(true);
                    holder.isPlaying = true;
                    int new_pos = sb.getProgress(); // 사용자가 움직여놓은 위치
                    dto.setProgress(new_pos);


                    holder.mediaPlayer.seekTo(new_pos);
                    System.out.println("Try mediaplayer start at " + new_pos);
                    holder.mediaPlayer.start();

                    System.out.println("mediaplayer started@@");

                    MyThread myThread = new MyThread();
                    myThread.c_holder = holder;

                    myThread.start();
                }
            }
        });

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dto.checkInitialPlay()) {//최초 play

                    holder.seekBar.setMax(holder.mediaPlayer.getDuration()); // 시크바 최대 범위를 노래 재생시간으로 설정

                    holder.seekBar.setProgress(0);
                    holder.mediaPlayer.start();

                    dto.setIsPlaying(true);
                    dto.setInitial_play();
                    holder.isPlaying = true;

                    MyThread myThread = new MyThread();
                    myThread.c_holder = holder;
                    myThread.start();

                } else {


                    holder.mediaPlayer.seekTo(dto.getProgress());//멈춘 시점부터 재시작
                    holder.mediaPlayer.start();

                    dto.setIsPlaying(true);
                    holder.isPlaying = true;

                    MyThread myThread = new MyThread();
                    myThread.c_holder = holder;
                    myThread.start();
                }

            }
        });

        holder.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dto.checkInitialPlay()) {
                    System.out.println("not initial play@@@@@@@@@@@@@@@@@");
                    dto.setProgress(holder.mediaPlayer.getCurrentPosition());

                    holder.mediaPlayer.pause();
                    dto.setIsPlaying(false);
                    holder.isPlaying = false;
                }
            }
        });


        holder.seekBar.setFocusable(false);
        holder.seekBar.setFocusableInTouchMode(false);

        holder.play.setFocusable(false);
        holder.play.setFocusableInTouchMode(false);

        holder.stop.setFocusable(false);
        holder.stop.setFocusableInTouchMode(false);

        SynchSeekBar synch = new SynchSeekBar();
        synch.s_holder = holder;
        synch.start();


        return convertView;
    }





    class CustomViewHolder {

        TextView textName;
        TextView textAdmin;
        TextView textInst;
        TextView textUpdate;
        ImageButton play;
        ImageButton stop;
        MediaPlayer mediaPlayer;

        int duration;
        String duration_str;
        TextView show_time;
        int played_sec = 0;
        int played_minute = 0;
        SeekBar seekBar;//음악 재생위치를 나타내는 시크바
        boolean isPlaying = false; // 재생중?

    }

    class MyThread extends Thread {
        CustomViewHolder c_holder;

        @Override
        public void run(){//Thread 시작할때 콜백되는 메서드
            //시크바 막대기 조금씩 움직이기 (노래 끝날 때 까지 반복)
            while(c_holder.isPlaying){

                int curr_pos = c_holder.mediaPlayer.getCurrentPosition();
                c_holder.seekBar.setProgress(curr_pos);


                int played = curr_pos / 1000;
                final int played_minute = played / 60;
                final int played_sec = played % 60;
                if(played_minute != c_holder.played_minute || played_sec != c_holder.played_sec) {
                    m_activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(played_sec % 60 < 10) c_holder.show_time.setText(played_minute + ":0" + played_sec + " / " + c_holder.duration_str);
                            else c_holder.show_time.setText(played_minute + ":" + played_sec + " / " + c_holder.duration_str);
                        }
                    });
                }
                c_holder.played_minute = played_minute;
                c_holder.played_sec = played_sec;

            }

        }
    }


    public void addItem(ProjectListDTO dto) {

        listCustom.add(dto);
    }

    public void delItem(ProjectListDTO dto){
        listCustom.remove(dto);
    }

    public void modifyItem(int i, ProjectListDTO dto){
        listCustom.set(i, dto);
    }

    class SynchSeekBar extends Thread {
        CustomViewHolder s_holder;
        @Override
        public void run(){//Thread 시작할때 콜백되는 메서드
            int curr_pos = s_holder.mediaPlayer.getCurrentPosition();
            System.out.println("SynchSeekbar ###");

            s_holder.seekBar.setProgress(curr_pos);

            int played = curr_pos / 1000;
            final int played_minute = played / 60;
            final int played_sec = played % 60;

            m_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(played_sec % 60 < 10) s_holder.show_time.setText(played_minute + ":0" + played_sec + " / " + s_holder.duration_str);
                    else s_holder.show_time.setText(played_minute + ":" + played_sec + " / " + s_holder.duration_str);
                }
            });


        }
    }


}