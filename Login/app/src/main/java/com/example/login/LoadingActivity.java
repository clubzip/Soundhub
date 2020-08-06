package com.example.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class LoadingActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void splashAnimation() {
        Animation animation_text = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_splash_textview);
        TextView splashTextView = findViewById(R.id.splashTextView);
        splashTextView.startAnimation(animation_text);

        animation_text.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                overridePendingTransition(R.anim.anim_splash_out_top,R.anim.anim_splash_in_down);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView splashGif = (ImageView)findViewById(R.id.loading_view);
        Glide.with(this).load(R.raw.loading).into(splashGif);
        startLoading();
        splashAnimation();
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }


}
