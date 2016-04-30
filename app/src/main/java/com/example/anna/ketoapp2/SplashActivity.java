package com.example.anna.ketoapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


//this activity was created for the splash screen however is never used
public class SplashActivity extends AppCompatActivity {

    private static final int SLEEP_TIME = 4 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

   Thread splash =new Thread() {
       public void run() {
           try {
               Thread.sleep(SLEEP_TIME * 1000);
           } catch (Exception e) {

           }

           // Start main activity
           Intent intent = new Intent(SplashActivity.this, MainActivity.class);
           SplashActivity.this.startActivity(intent);
           SplashActivity.this.finish();
       }
   };
        splash.start();
}
}
