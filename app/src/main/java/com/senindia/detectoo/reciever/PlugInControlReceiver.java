package com.senindia.detectoo.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.senindia.detectoo.service.ServiceForPower;

/**
 * Created by vishal on 3/5/18.
 */

public class PlugInControlReceiver extends BroadcastReceiver {

    public void onReceive(Context context , Intent intent) {
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {

            Log.e("@@POWER","......Connected");

            Intent stopSecurityService = new Intent(context,ServiceForPower.class);
            context.stopService(stopSecurityService);

        }
        else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {

            Log.e("@@POWER","......Not Connected");

            Intent startSecurityService = new Intent(context,ServiceForPower.class);
            context.startService(startSecurityService);

        }
    }
}
