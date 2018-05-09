package com.senindia.detectoo.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.senindia.detectoo.service.FlashlightProvider;
import com.senindia.detectoo.R;
import com.senindia.detectoo.service.SecurityService;
import com.senindia.detectoo.service.ServiceForPin;
import com.senindia.detectoo.comman.CommanClass;
import com.senindia.detectoo.service.ServiceForPower;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class Home extends AppCompatActivity  {

    boolean doubleBackToExitPressedOnce = false;

    TextView btn_start,btn_stop,btn_help;

    FlashlightProvider fl;

    boolean result;

    RippleBackground rippleBackground;

    Handler handler;

    LinearLayout ln_ripple,ln_home_back;

    CommanClass cc;

    ImageView iv_setting,iv_rate;

    private static final String SHOWCASE_ID = "sequence example";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        rippleBackground=(RippleBackground)findViewById(R.id.content);
        ln_ripple = (LinearLayout) findViewById(R.id.ln_ripple);
        ln_home_back = (LinearLayout) findViewById(R.id.ln_home_back);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        iv_rate = (ImageView) findViewById(R.id.iv_rate);

        handler = new Handler();

        fl = new FlashlightProvider(getApplicationContext());

        btn_start = (TextView) findViewById(R.id.btn_start);
        btn_stop = (TextView) findViewById(R.id.btn_stop);
        btn_help = (TextView) findViewById(R.id.btn_help);

        AnimationDrawable animationDrawable = (AnimationDrawable)ln_home_back.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        rotate.setDuration(900);
        rotate.setRepeatCount(Animation.INFINITE);
        iv_setting.startAnimation(rotate);


        presentShowcaseSequence();


        clickListner();

    }

    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {

            }
        });

        sequence.setConfig(config);

        sequence.addSequenceItem(iv_setting, "Click here to enable or disable particular detector", "GOT IT");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(btn_start)
                        .setDismissText("GOT IT")
                        .setContentText("Click here to enable all detector")
                        .withRectangleShape(true)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(btn_help)
                        .setDismissText("GOT IT")
                        .setContentText("Click here to disable all detector")
                        .withRectangleShape(true)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(btn_stop)
                        .setDismissText("GOT IT")
                        .setContentText("Click here to stop siren or vibration")
                        .withRectangleShape()
                        .build()
        );

        sequence.start();

    }

    private void clickListner() {


        if (cc.loadPrefBoolean("isStart") == true){

            rippleBackground.startRippleAnimation();

        }else {

            rippleBackground.stopRippleAnimation();
        }



        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rippleBackground.startRippleAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        foundDevice();
                    }
                },3000);

                cc.savePrefBoolean("isStart",true);

                cc.savePrefBoolean("t_pickup",true);
                cc.savePrefBoolean("t_aunth",true);
                cc.savePrefBoolean("t_charger",true);
                cc.savePrefBoolean("t_siren",true);
                cc.savePrefBoolean("t_vibration",true);

                Intent startSecurityService = new Intent(Home.this,SecurityService.class);
                startService(startSecurityService);

            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent stopSecurityService = new Intent(Home.this,SecurityService.class);
                stopService(stopSecurityService);

                Intent stopSecurityServicePin = new Intent(Home.this,ServiceForPin.class);
                stopService(stopSecurityServicePin);

                Intent stopSecurityServicePower = new Intent(Home.this,ServiceForPower.class);
                stopService(stopSecurityServicePower);

                if (cc.loadPrefBoolean("t_pickup") == true){

                    Intent startSecurityService = new Intent(Home.this,SecurityService.class);
                    startService(startSecurityService);

                }else {


                }

                rippleBackground.stopRippleAnimation();

                cc.savePrefBoolean("isStart",false);
            }
        });

        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rippleBackground.stopRippleAnimation();

                cc.savePrefBoolean("t_pickup",false);
                cc.savePrefBoolean("t_aunth",false);
                cc.savePrefBoolean("t_charger",false);
                cc.savePrefBoolean("t_siren",false);
                cc.savePrefBoolean("t_vibration",false);
            }
        });

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSetting = new Intent(Home.this,SettingActivity.class);
                startActivity(intentSetting);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                rippleBackground.stopRippleAnimation();
            }
        });

        iv_rate.setOnClickListener(new View.OnClickListener() {
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

    private void foundDevice(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(ln_ripple, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(ln_ripple, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        ln_ripple.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

   /* @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }*/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        exitDialog();
        //super.onBackPressed();
    }

    private void exitDialog() {

        final Dialog dialog = new Dialog(Home.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        TextView tv_dialog_yes = (TextView) dialog.findViewById(R.id.tv_exit_yes);
        TextView tv_dialog_no = (TextView) dialog.findViewById(R.id.tv_exit_no);


        tv_dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                dialog.dismiss();
            }
        });

        tv_dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cc.loadPrefBoolean("t_pickup") == true){

            Intent startSecurityService = new Intent(Home.this,SecurityService.class);
            startService(startSecurityService);

        }else {


        }

        if (cc.loadPrefBoolean("t_pickup") == true && cc.loadPrefBoolean("t_aunth") == true && cc.loadPrefBoolean("t_charger") == true){

            rippleBackground.startRippleAnimation();

        }else {


        }
    }
}
