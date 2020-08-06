package com.example.login.myproject.commits;


import android.media.MediaPlayer;

import java.io.Serializable;

public class CommitsListDTO implements Serializable {

    private int mp3_id;
    private String Name;
    private int duration;
    private String duration_str;
    private boolean initial_play = true;
    private MediaPlayer dto_mp;
    private int progress;
    private String mp3_name;
    private boolean isPlaying;

    private String admin;
    private String inst;
    private String update;

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        this.Name = name;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int dr){this.duration = dr;}

    public String getDurationString() {
        return duration_str;
    }
    public void setDurationString(String dr){this.duration_str = dr;}

    public boolean checkInitialPlay(){return initial_play;}
    public void setInitial_play(){this.initial_play = false;}

    public int getMp3_id() {
        return mp3_id;
    }
    public void setMp3_id(int m_id) {
        this.mp3_id = m_id;
    }

    public MediaPlayer getMediaPlayer() {
        return dto_mp;
    }
    public void setMediaPlayer(MediaPlayer mp) {
        this.dto_mp = mp;
    }

    public int getProgress(){return progress;}
    public void setProgress(int pg){this.progress = pg;}

    public String getMp3_name(){return mp3_name;}
    public void setMp3_name(String m_name){this.mp3_name = m_name;}

    public boolean getIsPlaying(){return isPlaying;}
    public void setIsPlaying(boolean play){this.isPlaying = play;}

    public String getAdmin(){return admin;}
    public void setAdmin(String ad){this.admin = ad;}

    public String getInst(){return inst;}
    public void setInst(String ins){this.inst = ins;}

    public String getUpdate(){return update;}
    public void setUpdate(String upd){this.update = upd;}







}