package com.senindia.detectoo.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.senindia.detectoo.R;
import com.senindia.detectoo.comman.CommanClass;
import com.senindia.detectoo.pinui.CustomPinActivity;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;


/**
 * Created by vishal on 2/4/18.
 */

public class SecurityService extends Service implements SensorEventListener {


    private SensorManager sensorMan;
    private Sensor accelerometer;

    private float[] mGravity;
    private double mAccel;
    private double mAccelCurrent;
    private double mAccelLast;

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

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);

        mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mp = MediaPlayer.create(this, R.raw.alert_siren);

        fl = new FlashlightProvider(getApplicationContext());

        Log.e("Service Started", "Service Started");

        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            // Shake detection
            double x = mGravity[0];
            double y = mGravity[1];
            double z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = Math.sqrt(x * x + y * y + z * z);;
            double delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect

            if(mAccel > 2.5){

                if (cc.loadPrefBoolean("t_pickup") == true){

                    playSiren();

                }else {

                }

            }
        }

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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service Destroyed", "Service Destroyed");

        sensorMan.unregisterListener(this);

        stopSiren();

    }

    private void stopSiren() {

        mp.stop();

        mVibrator.cancel();

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
    }

    private void showNotification() {
        /*final NotificationManager mgr = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder note = new NotificationCompat.Builder(this);
        note.setContentTitle("Device Accelerometer Notification");
        note.setTicker("New Message Alert!");
        note.setAutoCancel(true);
        // to set default sound/light/vibrate or all
        note.setDefaults(Notification.DEFAULT_ALL);
        // Icon to be set on Notification
        note.setSmallIcon(R.mipmap.ic_launcher);
        // This pending intent will open after notification click
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
                Home.class), 0);
        // set pending intent to notification builder
        note.setContentIntent(pi);
        mgr.notify(101, note.build());*/


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.btn_star);
        builder.setContentTitle("Detecto++ Caught You");
        builder.setContentText("To stop sound/vibration click here");
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("To stop sound/vibration click here"));
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));

       /* Intent intent = new Intent(SecurityService.this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 113,intent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        Intent intent = new Intent(SecurityService.this, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 113,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        builder.setFullScreenIntent(pendingIntent, true);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(115, builder.build());

    }


}


