package com.example.dangxuanhung.alarmtraining;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;


/**
 * Created by Dang Xuan Hung on 23/02/2016.
 */
public class StopAlarm extends AppCompatActivity {

    ImageView foundDevice;
    ImageView btnStop;

    private GoogleApiClient client;
    private CountDownTimer countDownTimer;
    public boolean timerStopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ring);

        // Count Time display activity
        startTimer();

        foundDevice=(ImageView)findViewById(R.id.foundDevice);
        btnStop=(ImageView)findViewById(R.id.centerImage);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        final Handler handler=new Handler();

        foundDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // creat dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(StopAlarm.this);

                builder.setMessage("DAY DI NAO")
                        .setCancelable(false)
                        .setPositiveButton("Image", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(getApplicationContext(), RingtonePlayingService.class);
                                intent.putExtra("extra", "off");
                                startService(intent);
                                finish();

                                Intent i_image = new Intent(StopAlarm.this,SDCardImagesActivity.class);
                                finish();
                                startActivity(i_image);
                            }
                        })
                        .setNegativeButton("Audio", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(StopAlarm.this, RingtonePlayingService.class);
                                intent.putExtra("extra", "off");
                                startService(intent);
                                finish();

                                Intent intent_audio = new Intent(StopAlarm.this,AudioActivity.class);
                                startActivity(intent_audio);
                            }
                        });
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialogExample");
                alert.show();
            }
        });

        // start RippleAnimation
        rippleBackground.startRippleAnimation();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                foundDevice();
            }
        }, 5000);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /** Starts the timer **/
    public void startTimer() {
        setTimerStartListener();
        timerStopped = false;
    }

    /** Stop the timer **/
    public void stopTimer() {
        countDownTimer.cancel();
        timerStopped = true;
    }

    /** Timer method: CountDownTimer **/
    private void setTimerStartListener() {
        // will be called at every 1500 milliseconds i.e. every 1.5 second.
        countDownTimer = new CountDownTimer(60000, 60000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("extra", "off");

                stopService(intent);
                sendBroadcast(intent);

                //finish();
            }
        }.start();
    }

    private void foundDevice(){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        foundDevice.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "StopAlarm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.dangxuanhung.alarmtraining/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "StopAlarm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.dangxuanhung.alarmtraining/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
