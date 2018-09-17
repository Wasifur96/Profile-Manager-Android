package com.pofilemanager.wasif.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    Button UnBind;
    boolean isServiceBind;
    All_services A_S;
    ServiceConnection srvCon ;  // flag to check connection 
    Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        UnBind = findViewById(R.id.btn_Unbind);
    }

    @Override
    protected void onResume(){
            super.onResume();

        serviceIntent = new Intent(getApplicationContext(),All_services.class);
        startService(serviceIntent);
        bindService();


        UnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                stopService(serviceIntent);
                unbindService(srvCon);
                Toast.makeText(getApplicationContext(),"Service UnBind",Toast.LENGTH_LONG).show();

            }
        });



        try {
            if(A_S.is_Binded){
                Toast.makeText(getApplicationContext(),"Service is ON",Toast.LENGTH_LONG).show();
            }

        }
        catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(),"Service is Bound now !!",Toast.LENGTH_LONG).show();
        }


    }

    private void bindService(){
        if(srvCon==null){
            srvCon = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    isServiceBind=true;
                    All_services.AllServiceBinder all_ServiceBinder = (All_services.AllServiceBinder)iBinder;
                    A_S = all_ServiceBinder.getService();

                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBind=false;
                }
            };
        }
        bindService(serviceIntent,srvCon,BIND_AUTO_CREATE);
    }

}
