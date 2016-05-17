package com.example.dangxuanhung.alarmtraining;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by Dang Xuan Hung on 22/02/2016.
 */
public class RingtonePlayingService extends Service {

    private static final String TAG = RingtonePlayingService.class.getSimpleName() ;
    MediaPlayer media_song;
    Boolean isRunning=false;
    private int startID;
    private Cursor audioCursor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startID){

        String state = intent.getExtras().getString("extra");
        Log.d(TAG, "extra : "+ state);
        String ring_alarm = intent.getExtras().getString("ring_alarm");
       // Log.d(TAG,ring_alarm);
        String type = intent.getExtras().getString("type");
       // Log.d(TAG, "type : "+ type);

        switch (state){
            case "on":
                startID=1;
                break;
            case "off":
                startID=0;
                break;
            default:
                startID=0;
        }

        if(type!=null){
            if(type.equals(getString(R.string.list_default)))
                createPlaymedia(ring_alarm);
            if(type.equals(getString(R.string.mylist)))
                createPlaymediaMyList(ring_alarm);
        }

        // running and click on
        if(!this.isRunning && startID==1){
            Log.e("you want start?", "Yes!");
            media_song.start();
            media_song.setLooping(true);
            isRunning=true;
            startID=0;
        }
        //running and click off
        else if(this.isRunning && startID==0){
            Log.e("you want stop?","Yes!");
            media_song.stop();
            media_song.reset();
            isRunning=false;
            startID=0;
        }
        //no run and click on
        else if(!this.isRunning && startID==0){
            Log.e("The media is'nt running","OK!");
            isRunning=false;
            startID=1;
        }
        //no run and click off
        else if(this.isRunning && startID==1){

            Log.e("The media is running","OK!");
            isRunning=true;
            startID=1;
        }

        return START_NOT_STICKY;

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(this,"Stop Service",Toast.LENGTH_SHORT).show();
    }

    public void createPlaymedia(String name){

        Field[] fields = R.raw.class.getFields();
        // loop for every file in raw folder
        for(int count=0; count < fields.length; count++){
            int rid = 0;
            try {
                rid = fields[count].getInt(fields[count]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e1){
                e1.printStackTrace();
            }
            // Use that if you just need the file name
            String filename = fields[count].getName();
            if(filename.equals(name)){
                media_song = MediaPlayer.create(this,rid);
                break;
            }
        }
    }

    public void createPlaymediaMyList(String name){
        String name_audio;

        final String[] proj = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.SIZE,
        };
        audioCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj, null, null, null);
        int i=0;
        boolean check = false;
        for(i=0;i<audioCursor.getCount();i++){
            audioCursor.moveToPosition(i);
            name_audio = audioCursor.getString(2);
            if(name_audio.equals(name)){
                try {
                    check=true;
                    media_song = new MediaPlayer();
                    media_song.setDataSource(audioCursor.getString(1));
                    media_song.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if(check==false){
            media_song = MediaPlayer.create(this,R.raw.alarm);
        }

    }




}
