package com.example.anna.ketoapp2;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Anna on 24/02/2016.
 */
public class AppNotifications extends CountDownTimer {

    Context context;
    MediaPlayer mediaPlayer;
    AlertDialog dialog;
    boolean canceled=false;

    public AppNotifications(int time, int interval,Context context, AlertDialog dialog,MediaPlayer mediaPlayer){
        super(time,interval);
        this.context=context;
        this.dialog=dialog;
        this.start();
        this.mediaPlayer=mediaPlayer;

    }
    @Override
    public void onTick(long millisUntilFinished) {
        //Method not used however it is mandatory to override it to extend CountDownTimer functionality
    }



    @Override
    public void onFinish() {
        if(canceled==false) {
            //When time is finished play sound and open bring the application alarm screen to the top on the mobile phone
            playSound();
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    private void playSound()  {
        dialog.show();
        mediaPlayer.start();
    }
    public void stopSound(){
        if( mediaPlayer != null ) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
