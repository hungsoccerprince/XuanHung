package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.dangxuanhung.alarmtraining.cache.ListAdapter;
import com.example.dangxuanhung.alarmtraining.model.Alarm;
import com.example.dangxuanhung.alarmtraining.model.DayAlarm;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Dang Xuan Hung on 07/03/2016.
 */
public class MainActivity extends Activity {

    public static int REQUEST_CODE_INPUT = 1;
    public static int REQUEST_CODE_EDIT = 2;

    DatabaseHelper dbHelper;
    public static final String TAG = MainActivity.class.getSimpleName();

    FloatingActionButton fabAddAlarm;
    ListView lvListAlarm;

    ArrayList<Alarm> arrAlarm;
    ArrayList<DayAlarm> arrDay;
    ListAlarmAdapter adapter;
    int max_id;
    private RecyclerView rvAlarm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY,1);
        c1.set(Calendar.MINUTE,2);
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.HOUR_OF_DAY,1);
        c2.set(Calendar.MINUTE,5);

        dbHelper = new DatabaseHelper(this);
       // arrDay = new ArrayList<DayAlarm>();
        arrAlarm = new ArrayList<Alarm>();
        /*lvListAlarm = (ListView) findViewById(R.id.lvListAlarm);
        adapterAlarm = new ListAdapter(this, R.layout.listview, arrAlarm);
        lvListAlarm.setAdapter(adapterAlarm);*/

        rvAlarm = (RecyclerView)findViewById(R.id.lvListAlarm);
        rvAlarm.setLayoutManager(new LinearLayoutManager(this));
        showList();

        adapter = new ListAlarmAdapter(getApplicationContext(), arrAlarm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = rvAlarm.getChildLayoutPosition(v);
                Alarm alarm = arrAlarm.get(position);
                Intent intent_edit = new Intent(MainActivity.this, EditAlarm.class);
                intent_edit.putExtra("id", alarm.getIdAlarm());
                intent_edit.putExtra("name_alarm", alarm.getNameAlarm());
                intent_edit.putExtra("ring_alarm", alarm.getRingAlarm());
                intent_edit.putExtra("hour", alarm.getHourAlarm());
                intent_edit.putExtra("minute", alarm.getMinuteAlarm());
                intent_edit.putExtra("arr_day_string",alarm.getArrDay());
                intent_edit.putExtra("vibrate",alarm.getVibrate());

                startActivityForResult(intent_edit, REQUEST_CODE_EDIT);
            }
        });
        rvAlarm.setAdapter(adapter);

        fabAddAlarm = (FloatingActionButton) findViewById(R.id.fabAddAlarm);
        fabAddAlarm.attachToRecyclerView(rvAlarm);


        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_add = new Intent(MainActivity.this, AddAlarm.class);
                intent_add.putExtra("max_id", max_id + 1);
                startActivityForResult(intent_add, REQUEST_CODE_INPUT);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        showList();
        adapter.notifyDataSetChanged();
    }

    // show list alarm from database to listview
    public void showList() {

        arrAlarm.clear();
        Cursor kq_alarm = dbHelper.getData("Select * from alarm_table");
        //Log.d(TAG, String.valueOf(kq_alarm.getCount()));
        while (kq_alarm.moveToNext()) {
            Alarm alarm = new Alarm();

            alarm.setIdAlarm(kq_alarm.getInt(0));
            alarm.setNameAlarm(kq_alarm.getString(kq_alarm.getColumnIndex("name_alarm")));
            alarm.setHourAlarm(kq_alarm.getInt(kq_alarm.getColumnIndex("hour")));
            alarm.setMinuteAlarm(kq_alarm.getInt(kq_alarm.getColumnIndex("minute")));
            alarm.setRingAlarm(kq_alarm.getString(kq_alarm.getColumnIndex("ring_alarm")));
            alarm.setVibrate(kq_alarm.getString(kq_alarm.getColumnIndex("vibrate")));
            alarm.setArrDay(kq_alarm.getString(kq_alarm.getColumnIndex("arr_day")));
            alarm.setState(kq_alarm.getString(kq_alarm.getColumnIndex("state")));

            Log.d(TAG,alarm.getState());

            arrAlarm.add(alarm);
        }
        // Log.d(TAG, "max_id : "+ arrAlarm.get(arrAlarm.size()-1).getIdAlarm());
        if (arrAlarm.size() > 0){
            Log.d(TAG, "max_id : "+ arrAlarm.get(arrAlarm.size()-1).getIdAlarm());
            max_id = arrAlarm.get(arrAlarm.size() - 1).getIdAlarm();
        }
        else {
            Log.d(TAG,"max_id = 0");
            max_id=0;
        }
    }

}
