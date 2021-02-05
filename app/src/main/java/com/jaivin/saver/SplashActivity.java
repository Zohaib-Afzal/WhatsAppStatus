package com.jaivin.saver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by DS on 14/03/2017.
 */

public class SplashActivity extends AppCompatActivity {
    protected boolean _active = true;
    protected int _splashTime = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LinearLayout myImageView= findViewById(R.id.splashimage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent =new Intent(SplashActivity.this,MainWhatsActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },4000);



    }

    @Override
    public void onBackPressed() {

    }
}
