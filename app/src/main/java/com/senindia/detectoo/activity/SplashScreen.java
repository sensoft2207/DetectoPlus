package com.senindia.detectoo.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.senindia.detectoo.comman.CommanClass;
import com.senindia.detectoo.pinui.CustomPinActivity;
import com.senindia.detectoo.reciever.DeviceAdminSample;
import com.senindia.detectoo.R;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;

/**
 * Created by vishal on 2/5/18.
 */

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;

    Button btn_allow_permission;

    ImageView iv_logo;

    CommanClass cc;

    RippleBackground rippleBackground;

    Handler handler;

    private static final int REQUEST_CODE_ENABLE = 11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        cc = new CommanClass(this);

        rippleBackground=(RippleBackground)findViewById(R.id.content);
        btn_allow_permission = (Button)findViewById(R.id.btn_allow_permission);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);

        handler = new Handler();

        rippleBackground.startRippleAnimation();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                foundDevice();
            }
        },3000);

        btn_allow_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ComponentName cn=new ComponentName(SplashScreen.this, DeviceAdminSample.class);
                DevicePolicyManager mgr=
                        (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);

                if (mgr.isAdminActive(cn)) {

                   /* Intent intentMain = new Intent(SplashScreen.this,Home.class);
                    startActivity(intentMain);
                    finish();*/

                    if (cc.loadPrefBoolean("isPinset") == true){

                        Intent intent = new Intent(SplashScreen.this, CustomPinActivity.class);
                        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
                        startActivity(intent);
                        finish();

                    }else {

                        Intent intent = new Intent(SplashScreen.this, CustomPinActivity.class);
                        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                        startActivityForResult(intent, REQUEST_CODE_ENABLE);
                        cc.savePrefBoolean("isPinset",true);


                    }
                }
                else {
                    Intent intent=
                            new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            getString(R.string.device_admin_explanation));
                    startActivity(intent);
                }

            }
        });

        //CountDown();
    }

    private void foundDevice(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(iv_logo, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(iv_logo, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        iv_logo.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ComponentName cn=new ComponentName(SplashScreen.this, DeviceAdminSample.class);
        DevicePolicyManager mgr=
                (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);

        if (mgr.isAdminActive(cn)) {

            btn_allow_permission.setText(getString(R.string.go));
        }
        else {

        }

    }

    private void CountDown() {

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                Intent intentMain = new Intent(SplashScreen.this,Home.class);
                startActivity(intentMain);
                finish();


            }
        }, SPLASH_TIME_OUT);

    }
}
