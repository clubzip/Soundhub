package com.example.login;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class ForcedTerminationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.d("test", "서비스의 onCreate");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) { //핸들링 하는 부분
        Log.e("Error","onTaskRemoved - 강제 종료 " + rootIntent);
        Toast.makeText(this, "서비스가 강제 종료 되었습니다", Toast.LENGTH_SHORT).show();

        LoginManager loginManager = ((MainActivity) MainActivity.context).loginManager;
        if (loginManager != null) {loginManager.logOut();}

        GoogleSignInClient googleSignInClient = ((MainActivity) MainActivity.context).mGoogleSignInClient;
        if (googleSignInClient != null) {googleSignInClient.signOut();}

        stopSelf(); //서비스 종료
    }




}
