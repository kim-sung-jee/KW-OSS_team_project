package org.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RestartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            Intent in=new Intent(context, org.example.myapplication.RestartService.class);
            context.startForegroundService(in);
        }else{
            Intent in=new Intent(context, org.example.myapplication.RestartService.class);
            context.startService(in);
        }
    }
}
