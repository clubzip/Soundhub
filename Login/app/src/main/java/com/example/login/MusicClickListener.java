package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

public class MusicClickListener implements View.OnClickListener {

    String projectID;
    String last_update;
    Context context;

    public MusicClickListener(Context context, String projectID, String last_up) {
        this.context = context;
        this.projectID = projectID;
        this.last_update = last_up;
    }

    public void onClick(View view) {

        Intent intent = new Intent(context, MusicActivity.class);
        intent.putExtra("last_update", last_update);
        intent.putExtra("projectID",projectID);

        context.startActivity(intent);
    }
}
