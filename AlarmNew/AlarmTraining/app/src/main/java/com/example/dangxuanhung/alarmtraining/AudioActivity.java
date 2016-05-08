package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        getPoint();

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

        if(audioCursor.getCount()==0){
            copyAssets();
            audioCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj, null, null, null);
        }
            // random 1 mp3 file
        Random rand = new Random();
        int rand_audio = rand.nextInt(audioCursor.getCount()-1);
        audioCursor.moveToPosition(rand_audio);
        name_audio = audioCursor.getString(2);

        try{
            audioPlay.setDataSource(audioCursor.getString(1));
            audioPlay.prepare();
            audioPlay.start();
            audioPlay.setLooping(true);
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
      /*  if(point==3){
            lnPoint.setBackgroundResource(R.drawable.point_3);
            Log.d(TAG, String.valueOf(point));
            check = true;
        }
        else if(point==2){
            lnPoint.setBackgroundResource(R.drawable.point_2);
            Log.d(TAG, String.valueOf(point));
        }
        else if(point==1 || point==0){
            lnPoint.setBackgroundResource(R.drawable.point_1);
            Log.d(TAG, String.valueOf(point));
        }*/
        switch (point){
            case 3:
                Log.d(TAG, String.valueOf(point));
                check = true;
                break;
            case 2:
                lnPoint.setBackgroundResource(R.drawable.point_3);
                Log.d(TAG, String.valueOf(point));
                break;
            case 1:
                lnPoint.setBackgroundResource(R.drawable.point_2);
                Log.d(TAG, String.valueOf(point));
                break;
            case 0:
                lnPoint.setBackgroundResource(R.drawable.point_1);
                break;
        }
        return check;
    }


    @Override
    public void onBackPressed() {
        return ;
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("ring");
            Log.d(TAG, String.valueOf(files.length));

        } catch (IOException e) {
            Log.e(TAG, "Failed to get asset file list.", e);
        }
        if (files != null) {
            for (String filename : files) {
                Log.d(TAG,filename);
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(filename);
                    File outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    Log.d(TAG, "copy file : "+ filename);
                } catch(IOException e) {
                    Log.e(TAG, "Failed to copy asset file: " + filename, e);
                }
                finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
            }
        }
        else Log.d(TAG,"null");

    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}
