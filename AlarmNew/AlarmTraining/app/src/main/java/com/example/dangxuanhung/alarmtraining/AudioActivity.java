package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dang Xuan Hung on 11/04/2016.
 */
public class AudioActivity extends Activity {

    private static final String TAG = AudioActivity.class.getSimpleName();
    private Button btn1,btn2, btn3, btn4 ;
    private TextView tvReply;
    private LinearLayout lnPoint;
    private Cursor audioCursor;
    private String name_audio = null;
    private int point=0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_layout);

        btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        btn3=(Button)findViewById(R.id.btn3);
        btn4=(Button)findViewById(R.id.btn4);
        tvReply =(TextView)findViewById(R.id.tvReply);
        lnPoint = (LinearLayout)findViewById(R.id.lnPoint);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        final Handler handler=new Handler();
        final MediaPlayer audioPlay = new MediaPlayer();

        final String[] proj = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.SIZE,
        };

        audioCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj, null, null, null);
        Log.d("AudioSize",String.valueOf(audioCursor.getCount()));

            // random 1 mp3 file
        Random rand = new Random();
        int rand_audio = rand.nextInt(audioCursor.getCount()-1);
        audioCursor.moveToPosition(rand_audio);
        name_audio = audioCursor.getString(2);

        try{
            audioPlay.setDataSource(audioCursor.getString(1));
            audioPlay.prepare();
            audioPlay.start();
            }
        catch (Exception e){
            Log.e(getLocalClassName(), String.valueOf(e));
        }

        getAnswer(audioCursor,name_audio);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn1.getText().equals(name_audio))
                {
                    point += 1;
                    getPoint();
                    if(getPoint()){
                        audioPlay.stop();
                        tvReply.setText("Chính Xác");
                        tvReply.setTextColor(Color.BLUE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                    }
                    else {
                        audioPlay.stop();
                        onCreate(savedInstanceState );
                    }
                }
                else {
                    audioPlay.stop();
                    onCreate(savedInstanceState );
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn2.getText().equals(name_audio))
                {
                    point += 1;
                    getPoint();
                    if(getPoint()){
                        audioPlay.stop();
                        tvReply.setText("Chính Xác");
                        tvReply.setTextColor(Color.BLUE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                    }
                    else {
                        audioPlay.stop();
                        onCreate(savedInstanceState );
                    }
                }
                else {
                    audioPlay.stop();
                    onCreate(savedInstanceState );
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn3.getText().equals(name_audio))
                {
                    point += 1;
                    getPoint();
                    if(getPoint()){
                        audioPlay.stop();
                        tvReply.setText("Chính Xác");
                        tvReply.setTextColor(Color.BLUE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                    }
                    else {
                        audioPlay.stop();
                        onCreate(savedInstanceState );
                    }
                }
                else {
                    audioPlay.stop();
                    onCreate(savedInstanceState );
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn4.getText().equals(name_audio))
                {
                    point += 1;
                    getPoint();
                    if(getPoint()){
                        audioPlay.stop();
                        tvReply.setText("Chính Xác");
                        tvReply.setTextColor(Color.BLUE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                    }
                    else {
                        audioPlay.stop();
                        onCreate(savedInstanceState );
                    }
                }
                else {
                    audioPlay.stop();
                    onCreate(savedInstanceState );
                }
            }
        });


        rippleBackground.startRippleAnimation();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //foundDevice();
            }
        },0);
    }

    private void getAnswer(Cursor audioCursor, String name_audio){
        Random rand = new Random();
        ArrayList<String> arr_title = new ArrayList<>();
        int i=0;
        audioCursor.moveToFirst();
        for(i=0;i<audioCursor.getCount();i++){
            arr_title.add(audioCursor.getString(2));
            audioCursor.moveToNext();
        }

        int rand_title1 = rand.nextInt(audioCursor.getCount());
        int rand_title2 = rand.nextInt(audioCursor.getCount());
        int rand_title3 = rand.nextInt(audioCursor.getCount());

        int rand_button = rand.nextInt(3)+1;

        if(rand_button==1){
            btn1.setText(name_audio);
            btn2.setText(arr_title.get(rand_title1));
            btn3.setText(arr_title.get(rand_title2));
            btn4.setText(arr_title.get(rand_title3));
        }

        if(rand_button==2){
            btn2.setText(name_audio);
            btn3.setText(arr_title.get(rand_title1));
            btn4.setText(arr_title.get(rand_title2));
            btn1.setText(arr_title.get(rand_title3));
        }

        if(rand_button==3){
            btn3.setText(name_audio);
            btn4.setText(arr_title.get(rand_title1));
            btn1.setText(arr_title.get(rand_title2));
            btn2.setText(arr_title.get(rand_title3));
        }

        if(rand_button==4){
            btn4.setText(name_audio);
            btn1.setText(arr_title.get(rand_title1));
            btn2.setText(arr_title.get(rand_title2));
            btn3.setText(arr_title.get(rand_title3));
        }
    }

    private boolean getPoint(){
        Boolean check = false;
        switch (point){
            case 3:
                lnPoint.setBackgroundResource(R.drawable.point_3);
                Log.d(TAG, String.valueOf(point));
                check = true;
                break;
            case 2:
                lnPoint.setBackgroundResource(R.drawable.point_3);
                Log.d(TAG, String.valueOf(point));
                check = false;
                break;
            case 1:
                lnPoint.setBackgroundResource(R.drawable.point_3);
                Log.d(TAG, String.valueOf(point));
                check = false;
                break;

        }
        return check;
    }

}
