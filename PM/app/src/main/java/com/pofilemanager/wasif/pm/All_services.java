package com.pofilemanager.wasif.pm;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;


public class All_services extends Service implements SensorEventListener {
    int mStartMode;       // indicates how to behave if the service is killed

    boolean is_Binded ;

    Sensor Acc, Mag, Prox ;
    SensorManager Sm;
    AudioManager Am;
    float Accx, Accy , Accz , Mx, My , Mz , Pr ;

    Vibrator v;

    Uri ringtone;
    Ringtone r;

    @Override
    public void onCreate() {
        // The service is being created

    }
        class AllServiceBinder extends Binder {
            public All_services getService() {
                return All_services.this;
            }
        }

       IBinder mBinder = new AllServiceBinder();      // interface for clients that bind


        @Nullable
        @Override
        public IBinder onBind (Intent intent){
            return mBinder;
        }

    public void onDestroy() {
        Log.i(getString(R.string.service_demo_tag), "Service Destroyed");
        Sm.unregisterListener(this);
        super.onDestroy();

    }


    /*@Override
    public void onCreate() {

    }*/


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()

        is_Binded = true;
        v = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r= RingtoneManager.getRingtone(getApplicationContext(),ringtone);

        //
        //
        ///////////////// \creating a Sensor manager / ////////////////////
        Sm =(SensorManager)getSystemService(SENSOR_SERVICE);
        ////////////////-------------- O -------------/////////////////////
        //
        //
        ///////////////// \creating a Audio manager / ////////////////////
        Am = (AudioManager)getSystemService(AUDIO_SERVICE);
        ////////////////-------------- O -------------/////////////////////
        //
        //


        // Creating Sensors


        Acc = Sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Mag = Sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        Prox = Sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        if(Acc!=null)
        {
            Sm.registerListener(this, Acc, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(Prox!=null)
        {
           Sm.registerListener(this, Prox, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(Mag!=null)
        {
            Sm.registerListener(this, Mag, SensorManager.SENSOR_DELAY_NORMAL);
        }


        mStartMode = super.onStartCommand(intent,flags,startId);
        return mStartMode;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    ///////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.M)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onSensorChanged(SensorEvent snsor) {


        Sensor sn =  snsor.sensor;

        if(sn.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            Accx = snsor.values[0] ;
            Accy = snsor.values[1] ;
            Accz = snsor.values[2] ;

        }
        if(sn.getType()==Sensor.TYPE_MAGNETIC_FIELD)
        {
            Mx = snsor.values[0] ;
            My = snsor.values[1] ;
            Mz = snsor.values[2] ;
        }
        if(sn.getType()==Sensor.TYPE_PROXIMITY) {
            Pr = snsor.values[0] ;
        }

        //////////////////////////////////// \ MODE Settings / ////////////////////////////////////

        if( 2.0 > Accx && Accx > -2.0 &&   2.0 > Accy && Accy > -2.0 && Accz >= 0.0 )       // 2 > Ax, Ay > -2 ...  Az >= 0
        {
            Am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);   // Mode: R
            r.play();

        }else if( 2.0 > Accx && Accx > -2.0 &&   2.0 > Accy && Accy > -2.0 && Accz < 0.0)    // 2 > Ax, Ay > -2 ...  Az < 0
        {
            Am.setRingerMode(AudioManager.RINGER_MODE_SILENT);    // Mode: S
        }else if( 2.0 > Accx && Accx > -2.0 &&   2.0 > Accz && Accz > -2.0 && Accy < 0.0 && Pr == 0.0)          // 2 > Ax, Ay > -2 ...  Az < 0 .. P = 0.0 (=> ON)
        {
            Am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE); // Mode: V + R
            v.vibrate(500);
            Am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            r.play();
        }
        else if( 2.0 > Accx && Accx > -2.0 &&   2.0 > Accz && Accz > -2.0 && Accy >= 0.0 && Pr == 0.0)       // 2 > Ax, Az > -2 ...  Ay >= 0 .. P = 0.0 (=> ON)
        {
            Am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE); // Mode: V
            v.vibrate(500);
        }

        //////////////////////////////////// ==== 0 ==== //////////////////////////////////////////



    }

}
