package com.example.dangxuanhung.alarmtraining;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Dang Xuan Hung on 27/02/2016.
 */
public class SelectRingTone extends AppCompatActivity {

    ListView lvListRing ;

    ArrayList<String> list_ring = new ArrayList();

    public static String request;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_media_song);

        Intent getIntent = getIntent();
        request = getIntent.getStringExtra("request");

        lvListRing = (ListView)findViewById(R.id.lvListRing);

        creatArray();
        ArrayAdapter<String> arrayAdapterRing =  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_ring );
        lvListRing.setAdapter(arrayAdapterRing);

        lvListRing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectRing = list_ring.get(position);

                if(request.equals("add")){
                    sentToAddAlarm(AddAlarm.REQUEST_CODE_INPUT,selectRing);
                }

                if(request.equals("edit")){
                    sentToAddAlarm(EditAlarm.REQUEST_CODE_EDIT,selectRing);
                }

                Toast.makeText(getApplicationContext(), "Ring Selected : " + selectRing, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void creatArray(){
        Field[] fields = R.raw.class.getFields();
        // loop for every file in raw folder
        for(int count=0; count < fields.length; count++){

            // Use that if you just need the file name
            String filename = fields[count].getName();
            list_ring.add(filename);
        }
    }

    public  void sentToAddAlarm(int resultcode, String selectRing ){

        // Add Alarm
        if(request.equals("add")){
            Intent i_ring = new Intent(SelectRingTone.this,AddAlarm.class);
            i_ring.putExtra("name_ring",selectRing);
            setResult(resultcode, i_ring);
            finish();
        }

        // Edit Alarm
        if(request.equals("edit")){
            Intent i_ring = new Intent(SelectRingTone.this,EditAlarm.class);
            i_ring.putExtra("name_ring",selectRing);
            setResult(resultcode, i_ring);
            finish();
        }
    }

}
