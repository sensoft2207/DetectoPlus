package com.senindia.detectoo.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.senindia.detectoo.R;
import com.senindia.detectoo.comman.CommanClass;
import com.senindia.detectoo.pinui.CustomPinActivity;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by vishal on 3/5/18.
 */

public class ServiceForPower extends Service {




    FlashlightProvider fl;

    boolean isStarted = false;
    boolean isStop = false;

    Runnable periodicTask;

    ScheduledExecutorService executor;

    Future longRunningTaskFuture;

    private Vibrator mVibrator;

    MediaPlayer mp;

    AudioManager audioManager;

    CommanClass cc;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        cc = new CommanClass(this);

        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);



        mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mp = MediaPlayer.create(this, R.raw.alert_siren_three);

        fl = new FlashlightProvider(getApplicationContext());

        Log.e("Service Started", "Service Started");

        if (cc.loadPrefBoolean("t_charger") == true){

            playSiren();

        }else {

        }

        return START_STICKY;
    }



    private void playSiren() {

        showNotification();

        long pattern[] = {50,100,100,250,150,350};

        if (cc.loadPrefBoolean("t_vibration") == true){

            mVibrator.vibrate(pattern,3);

        }else {

        }

        if (cc.loadPrefBoolean("t_siren") == true){

            mp.start();
            mp.setLooping(true);

        }else {

        }


        audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service Destroyed", "Service Destroyed");

        stopSiren();

    }

    private void stopSiren() {

        mp.stop();

        mVibrator.cancel();

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
    }

    private void showNotification() {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.btn_star);
        builder.setContentTitle("Detecto++ Charger unplugged");
        builder.setContentText("To stop sound/vibration connect USB or Click here");
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("To stop sound/vibration connect USB or Click here"));
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));

        /*Intent intent = new Intent(ServiceForPower.this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 113,intent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        Intent intent = new Intent(ServiceForPower.this, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 113,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        builder.setFullScreenIntent(pendingIntent, true);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(115, builder.build());
    }


}




