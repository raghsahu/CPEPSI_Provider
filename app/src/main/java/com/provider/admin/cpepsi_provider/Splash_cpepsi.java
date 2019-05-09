package com.provider.admin.cpepsi_provider;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_cpepsi extends AppCompatActivity {

    private int SPLASH_TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_cpepsi);


        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent intent = new Intent(Splash_cpepsi.this , LOG_in_Service_provider.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}