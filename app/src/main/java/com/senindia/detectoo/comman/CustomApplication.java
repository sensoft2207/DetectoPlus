package com.senindia.detectoo.comman;

import android.app.Application;

import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.senindia.detectoo.R;
import com.senindia.detectoo.pinui.CustomPinActivity;


/**
 * Created by oliviergoutay on 1/14/15.
 */
public class CustomApplication extends Application {

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate() {
        super.onCreate();

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.logo);
    }
}
