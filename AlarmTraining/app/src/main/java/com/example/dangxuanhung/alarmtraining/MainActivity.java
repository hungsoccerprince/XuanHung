package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dang Xuan Hung on 07/03/2016.
 */
public class MainActivity extends Activity  {

    public static int REQUEST_CODE_INPUT=1;
    public static int REQUEST_CODE_EDIT=2;
    public static int pCode =0;

    DatabaseHelper dbHelper = new DatabaseHelper(this);

    FloatingActionButton fabAddAlarm;
    ListView lvListAlarm;

    ArrayList<Alarm> arrAlarm;
    ListAdapter adapterAlarm;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tvTime = (TextView)findViewById(R.id.tvTimeMain);
        //getDefaultInfor();

        lvListAlarm = (ListView)findViewById(R.id.lvListAlarm);

        fabAddAlarm = (FloatingActionButton) findViewById(R.id.fabAddAlarm);
        fabAddAlarm.attachToListView(lvListAlarm);

        arrAlarm = new ArrayList<Alarm>();
        adapterAlarm = new ListAdapter(this,R.layout.listview,arrAlarm);

        lvListAlarm.setAdapter(adapterAlarm);

        showList();

        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_add = new Intent(MainActivity.this, AddAlarm.class);
                intent_add.putExtra("pCode", pCode);

                startActivityForResult(intent_add, REQUEST_CODE_INPUT);

            }
        });

        lvListAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Alarm alarm = (Alarm) parent.getItemAtPosition(position);
                Intent intent_edit = new Intent(MainActivity.this, EditAlarm.class);

                String time = alarm.getTimeAlarm();

                intent_edit.putExtra("id", alarm.getIdAlarm());
                intent_edit.putExtra("pCode", alarm.getpCode());
                intent_edit.putExtra("name_alarm", alarm.getNameAlarm());
                intent_edit.putExtra("ring_alarm", alarm.getRingAlarm());
                intent_edit.putExtra("time_alarm", time);
                intent_edit.putExtra("day_alarm",alarm.getDayAlarm());
                intent_edit.putExtra("vibrate",alarm.getVibrate());

                startActivityForResult(intent_edit, REQUEST_CODE_EDIT);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null){
            if(data.getStringExtra("request").equals("add")){
                int pCode = data.getExtras().getInt("pCode");
                String name_alarm = data.getStringExtra("name_alarm");
                Date time_alarm = new Date(data.getLongExtra("time_alarm",-1));
                String ring_alarm = data.getStringExtra("ring_alarm");
                String vibrate = data.getStringExtra("vibrate");

                ArrayList<Integer> arrDay = data.getIntegerArrayListExtra("day_alarm");
                String day_alarm = getString(arrDay);

                ContentValues values = new ContentValues();
                values.put("name_alarm",name_alarm);
                values.put("ring_alarm",ring_alarm);
                values.put("time_alarm",formatTime(time_alarm));
                values.put("day_alarm",day_alarm);
                values.put("vibrate", vibrate);
                values.put("pCode_alarm",pCode);

                dbHelper.insert(values);

                ContentValues values_pcode = new ContentValues();
                values_pcode.put("pcode",pCode+1);

                dbHelper.update(values_pcode,"pcode="+ pCode,"pcode_table");
                showList();
            }

            if(data.getStringExtra("request").equals("edit")){

                int id = data.getExtras().getInt("id");

                String name_alarm = data.getStringExtra("name_alarm");
                Date time_alarm = new Date(data.getLongExtra("time_alarm",-1));
                String ring_alarm = data.getStringExtra("ring_alarm");
                String vibrate = data.getStringExtra("vibrate");

                ArrayList<Integer> arrDay = data.getIntegerArrayListExtra("day_alarm");
                String day_alarm = getString(arrDay);

                ContentValues values = new ContentValues();
                values.put("name_alarm",name_alarm);
                values.put("ring_alarm",ring_alarm);
                values.put("time_alarm",formatTime(time_alarm));
                values.put("day_alarm",day_alarm);
                values.put("vibrate", vibrate);

                dbHelper.update(values, "_id=" + id, "alarm_table");
                showList();
            }

            if(data.getStringExtra("request").equals("delete")){
                int id = data.getExtras().getInt("id");
                dbHelper.delete("_id=" + id);
                showList();
            }

        }
    }


    public String formatTime(Date time){
        DateFormat df = new SimpleDateFormat("HH:mm:aa", Locale.getDefault());
        String timeFormat = df.format(time);
        return timeFormat;
    }

    // show list alarm from database to listview
    public void showList(){
        arrAlarm.clear();

        Cursor kq = dbHelper.getData("Select * from alarm_table");
        while (kq.moveToNext()) {
            Alarm alarm = new Alarm();

            alarm.setIdAlarm(kq.getInt(0));
            alarm.setpCode(kq.getInt(0));
            alarm.setNameAlarm(kq.getString(1));
            alarm.setRingAlarm(kq.getString(2));
            alarm.setTimeAlarm(kq.getString(3));
            alarm.setDayAlarm(kq.getString(5));
            alarm.setpCode(kq.getInt(6));

            if(kq.getString(4).equals("yes")){
                alarm.setVibrate(true);
            }
            if(kq.getString(4).equals("no")){
                alarm.setVibrate(false);
            }

            Log.e("name", kq.getString(1));

            arrAlarm.add(alarm);
        }
        pCode = getPcode();
        adapterAlarm.notifyDataSetChanged();
    }


    // get code for PendingIntent
    public int getPcode(){
        int p=0;
        Cursor kq = dbHelper.getPcode();
        while (kq.moveToNext()) {
            p= kq.getInt(0);
        }
        return p;
    }

    // get arrDay to String
    public String getString(ArrayList<Integer> arrDay){
        String day="";
        int i;
        for(i=0;i<arrDay.size();i++){
            day = day + arrDay.get(i).toString() + " " ;
        }
        return day;
    }


}
