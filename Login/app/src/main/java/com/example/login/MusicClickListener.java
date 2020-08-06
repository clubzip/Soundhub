package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

public class MusicClickListener implements View.OnClickListener {

    String projectID;
    Context context;

    public MusicClickListener(Context context, String projectID) {
        this.context = context;
        this.projectID = projectID;
    }

    public void onClick(View view) {

        Intent intent = new Intent(context, MusicActivity.class);
        intent.putExtra("projectID",projectID);
        context.startActivity(intent);
    }
}
