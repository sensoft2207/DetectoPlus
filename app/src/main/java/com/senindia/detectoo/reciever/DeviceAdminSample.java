package com.senindia.detectoo.reciever;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.senindia.detectoo.service.ServiceForPin;

/**
 * Created by vishal on 2/5/18.
 */

public class DeviceAdminSample extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context ctxt, Intent intent) {
        ComponentName cn=new ComponentName(ctxt, DeviceAdminSample.class);
        DevicePolicyManager mgr=
                (DevicePolicyManager)ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);

        mgr.setPasswordQuality(cn,
                DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC);

        onPasswordChanged(ctxt, intent);
    }

    @Override
    public void onPasswordChanged(Context ctxt, Intent intent) {
        DevicePolicyManager mgr=
                (DevicePolicyManager)ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int msgId;

        if (mgr.isActivePasswordSufficient()) {

            /*Intent intentMain = new Intent(ctxt,Home.class);
            ctxt.startActivity(intentMain);*/
        }
        else {

        }
    }

    @Override
    public void onPasswordFailed(Context ctxt, Intent intent) {
        Log.e("WrongPass","00000000000000");

        Intent startSecurityServicePin = new Intent(ctxt,ServiceForPin.class);
        ctxt.startService(startSecurityServicePin);

    }

    @Override
    public void onPasswordSucceeded(Context ctxt, Intent intent) {

    }



}