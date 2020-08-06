package com.example.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.login.Commit;
import com.example.login.Music;
import com.example.login.MusicClickListener;
import com.example.login.MusicView;
import com.example.login.Request;

import java.util.ArrayList;

public class MusicAdapter extends BaseAdapter {

    private LayoutInflater inflate;
    private ArrayList<Music> musics;
    private int layout;
    public static Context context;

    ArrayList<Commit> commits;

    public MusicAdapter(Context context, int layout, ArrayList<Music> musics) {
        this.inflate = LayoutInflater.from(context);
        this.layout = layout;
        this.musics = musics;
        this.context = context;
    }

    @Override
    public int getCount() {
        return musics.size();//array size
    }

    @Override
    public Music getItem(int pos) {
        Music item = (Music) musics.get(pos);
        return item;
    }

    @Override
    public long getItemId(int pos) { return pos; }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        MusicView view = null;

        if(convertView == null) {view = new MusicView(context,null);}
        else { view = (MusicView) convertView;}

        Music item = (Music) musics.get(pos);

        view.setTitle(item.getProjectID());
        view.setAdmin(item.getAdmin());
        view.setRecent(item.getLast_update());

        commits = item.getCommits();
        String instruments ="";

        for (int i=0; i<commits.size(); i++) {

            Commit commit = commits.get(i);

            if (i == commits.size()-1) {
                instruments += "* "+commit.getCategory();
            } else {
                instruments += "* "+commit.getCategory() + " ";
            }
        }

        view.setInstruments(instruments);

        /**
         * 노래 시작버튼 누르면 이미 바뀌면서 노래 재생되게 만들기!
         */

        MusicClickListener musicClickListener = new MusicClickListener(context,item.getProjectID());
        view.setOnClickListener(musicClickListener);

        return view;
    }
}
