package com.senindia.detectoo.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.senindia.detectoo.R;
import com.senindia.detectoo.comman.CommanClass;
import com.senindia.detectoo.pinui.CustomPinActivity;
import com.senindia.detectoo.service.SecurityService;

/**
 * Created by vishal on 3/5/18.
 */

public class SettingActivity extends AppCompatActivity {

    CommanClass cc;

    LinearLayout ln_set_back;

    ImageView iv_back,iv_pin_change,iv_change_back,iv_update;

    LabeledSwitch tg_pickup,tg_aunthentication,tg_charger,tg_siren,tg_vibration;

    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    private void init() {

        cc = new CommanClass(SettingActivity.this);

        tg_pickup = (LabeledSwitch)findViewById(R.id.tg_pickup);
        tg_aunthentication = (LabeledSwitch)findViewById(R.id.tg_aunthentication);
        tg_charger = (LabeledSwitch)findViewById(R.id.tg_charger);
        tg_siren = (LabeledSwitch)findViewById(R.id.tg_siren);
        tg_vibration = (LabeledSwitch)findViewById(R.id.tg_vibration);

        ln_set_back = (LinearLayout)findViewById(R.id.ln_set_back);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_pin_change = (ImageView) findViewById(R.id.iv_pin_change);
        iv_change_back = (ImageView) findViewById(R.id.iv_change_back);
        iv_update = (ImageView) findViewById(R.id.iv_update);

        ln_set_back.setBackground(getResources().getDrawable(R.drawable.setting_back));

        animationDrawable = (AnimationDrawable) ln_set_back.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        clickListner();
    }

    private void clickListner() {


//        For pickup
        if (cc.loadPrefBoolean("t_pickup") == true){

            tg_pickup.setOn(true);

        }else {

            tg_pickup.setOn(false);
        }

//        For aunthentication

        if (cc.loadPrefBoolean("t_aunth") == true){

            tg_aunthentication.setOn(true);

        }else {

            tg_aunthentication.setOn(false);
        }

//        For Charger

        if (cc.loadPrefBoolean("t_charger") == true){

            tg_charger.setOn(true);

        }else {

            tg_charger.setOn(false);
        }

//        For Siren

        if (cc.loadPrefBoolean("t_siren") == true){

            tg_siren.setOn(true);

        }else {

            tg_siren.setOn(false);
        }

//        For Vibration

        if (cc.loadPrefBoolean("t_vibration") == true){

            tg_vibration.setOn(true);

        }else {

            tg_vibration.setOn(false);
        }

        tg_pickup.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {

                if (isOn){

                    cc.savePrefBoolean("t_pickup",true);

                    Intent startSecurityService = new Intent(SettingActivity.this,SecurityService.class);
                    startService(startSecurityService);

                    Log.e("TPickup","Enable");

                }else {

                    cc.savePrefBoolean("t_pickup",false);

                    Log.e("TPickup","Disable");
                }

            }
        });

        tg_aunthentication.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {

                if (isOn){

                    cc.savePrefBoolean("t_aunth",true);

                    Log.e("TAunth","Enable");

                }else {

                    cc.savePrefBoolean("t_aunth",false);

                    Log.e("TAunth","Disable");
                }

            }
        });

        tg_charger.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {

                if (isOn){

                    cc.savePrefBoolean("t_charger",true);

                    Log.e("TCharger","Enable");

                }else {

                    cc.savePrefBoolean("t_charger",false);

                    Log.e("TCharger","Disable");
                }

            }
        });

        tg_siren.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {

                if (isOn){

                    cc.savePrefBoolean("t_siren",true);

                    Log.e("TSiren","Start");

                }else {

                    cc.savePrefBoolean("t_siren",false);

                    Log.e("TSiren","Stop");
                }

            }
        });

        tg_vibration.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {

                if (isOn){

                    cc.savePrefBoolean("t_vibration",true);

                    Log.e("TVibration","Start");

                }else {

                    cc.savePrefBoolean("t_vibration",false);

                    Log.e("TVibration","Stop");
                }

            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        iv_pin_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this, CustomPinActivity.class);
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
                startActivity(intent);
                cc.savePrefBoolean("isChangeP",true);
            }
        });

        iv_change_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareApp();
            }
        });

        iv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
    }

    private void shareApp() {

        final String appPackageName = getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download, Share and give good rating from this link: https://play.google.com/store/apps/details?id=" + appPackageName +"\n\nDetecto++ provide your smartphone phone security from theft.\n\nFeatures of Detecto++\n\n1) If someone try to pickup your mobile then siren and vibration will play for enabling this option Go to setting section and enable it.\n\n2) If your mobile phone is in charging mode and someone remove charging cable then siren will play for enabling this option Go to setting section and enable it.\n\n3) If someone try to Unlock your mobile phone with wrong password and pattern then siren and vibration will play for enabling this option Go to setting and enable it.\n\n4) Password protected Detecto++.\n\n5) Mute and Unmute option for siren.\n\n6) Disable Vibration from seeting section.\n\n7) To stop siren when it play then tap notification which is display on your mobile phone.");
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download and Check out All in one status at: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cc.loadPrefBoolean("t_pickup") == true){

            Intent startSecurityService = new Intent(SettingActivity.this,SecurityService.class);
            startService(startSecurityService);

        }else {


        }
    }
}
