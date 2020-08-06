package com.example.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusicView extends LinearLayout {

    TextView tv_title, tv_admin, tv_contributors, tv_instruments, tv_recent, tv_like;
    ImageView play_stop;

    public MusicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music,this,true);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_instruments = (TextView) findViewById(R.id.tv_instruments);
        tv_admin = (TextView) findViewById(R.id.tv_admin);
        tv_recent = (TextView) findViewById(R.id.tv_recent);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }
    public void setAdmin(String admin) {
        tv_admin.setText(admin);
    }
    public void setContributors(String contributors) {
        tv_contributors.setText(contributors);
    }
    public void setInstruments(String instruments) {
        tv_instruments.setText(instruments);
    }
    public void setRecent(String recent) {
        tv_recent.setText(recent);
    }
    public void setPlayStop(Bitmap image) {
        play_stop.setImageBitmap(image);
    }
}

