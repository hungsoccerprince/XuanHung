package com.example.dangxuanhung.alarmtraining;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Dang Xuan Hung on 27/02/2016.
 */
public class SelectRingTone extends AppCompatActivity {

    private static final String TAG = SelectRingTone.class.getSimpleName();
    ListView lvListRing ;
    RadioButton radioDefault, radioMyMusic;

    ArrayList<String> list_ring = new ArrayList();
    private Cursor audioCursor;
    String name_audio;
    public static String request;
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_media_song);

        Intent getIntent = getIntent();
        request = getIntent.getStringExtra("request");
        type = getIntent.getStringExtra("type");
        Log.d(TAG,type);

        lvListRing = (ListView)findViewById(R.id.lvListRing);
        radioDefault = (RadioButton)findViewById(R.id.radioDefault);
        radioMyMusic = (RadioButton)findViewById(R.id.radioMyMusic);

        if(type.equals(getString(R.string.list_default))){
            radioDefault.setChecked(true);
            creatArray();
        }

        if(type.equals(getString(R.string.mylist))){
            radioMyMusic.setChecked(true);
            creatMyList();
        }


        radioDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    creatArray();
                }
            }
        });

        radioMyMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    creatMyList();
                }
            }
        });
    }

    public void creatArray(){
        list_ring.clear();
        Field[] fields = R.raw.class.getFields();
        // loop for every file in raw folder
        for(int count=0; count < fields.length; count++){
            // Use that if you just need the file name
            String filename = fields[count].getName();
            list_ring.add(filename);
        }

        ArrayAdapter<String> arrayAdapterRing =  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_ring );
        lvListRing.setAdapter(arrayAdapterRing);
        arrayAdapterRing.notifyDataSetChanged();
        lvListRing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectRing = list_ring.get(position);

                if(request.equals("add")){
                    sentToAddAlarm(AddAlarm.REQUEST_CODE_INPUT,selectRing,getString(R.string.list_default));
                }

                if(request.equals("edit")){
                    sentToAddAlarm(EditAlarm.REQUEST_CODE_EDIT,selectRing,getString(R.string.list_default));
                }

                Toast.makeText(getApplicationContext(), "Ring Selected : " + selectRing, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void creatMyList(){
        list_ring.clear();
        final MediaPlayer audioPlay = new MediaPlayer();
        final String[] proj = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.SIZE,
        };
        audioCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj, null, null, null);
        Log.d("AudioSize",String.valueOf(audioCursor.getCount()));
        int i=0;
        for(i=0;i<audioCursor.getCount();i++){
            audioCursor.moveToPosition(i);
            name_audio = audioCursor.getString(2);
            list_ring.add(name_audio);
        }

        ArrayAdapter<String> arrayAdapterRing =  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_ring );
        lvListRing.setAdapter(arrayAdapterRing);
        arrayAdapterRing.notifyDataSetChanged();
        lvListRing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectRing = list_ring.get(position);

                if(request.equals("add")){
                    sentToAddAlarm(AddAlarm.REQUEST_CODE_INPUT,selectRing,getString(R.string.mylist));
                }

                if(request.equals("edit")){
                    sentToAddAlarm(EditAlarm.REQUEST_CODE_EDIT,selectRing,getString(R.string.mylist));
                }

                Toast.makeText(getApplicationContext(), "Ring Selected : " + selectRing, Toast.LENGTH_LONG).show();
            }
        });

    }

    public  void sentToAddAlarm(int resultcode, String selectRing, String type ){

        // Add Alarm
        if(request.equals("add")){
            Intent i_ring = new Intent(SelectRingTone.this,AddAlarm.class);
            i_ring.putExtra("name_ring",selectRing);
            i_ring.putExtra("type",type);
            setResult(resultcode, i_ring);
            finish();
        }

        // Edit Alarm
        if(request.equals("edit")){
            Intent i_ring = new Intent(SelectRingTone.this,EditAlarm.class);
            i_ring.putExtra("name_ring",selectRing);
            i_ring.putExtra("type",type);
            setResult(resultcode, i_ring);
            finish();
        }
    }

}
