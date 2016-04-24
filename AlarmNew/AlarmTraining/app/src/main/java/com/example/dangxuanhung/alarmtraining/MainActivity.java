package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dangxuanhung.alarmtraining.model.Alarm;
import com.example.dangxuanhung.alarmtraining.model.DayAlarm;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

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
    ListAdapter adapterAlarm;
    int max_id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        arrDay = new ArrayList<DayAlarm>();
        arrAlarm = new ArrayList<Alarm>();

        lvListAlarm = (ListView) findViewById(R.id.lvListAlarm);

        fabAddAlarm = (FloatingActionButton) findViewById(R.id.fabAddAlarm);
        fabAddAlarm.attachToListView(lvListAlarm);

        adapterAlarm = new ListAdapter(this, R.layout.listview, arrAlarm);
        lvListAlarm.setAdapter(adapterAlarm);
        showList();

        if (arrAlarm.size() > 0)
            max_id = arrAlarm.get(arrAlarm.size() - 1).getIdAlarm();

        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_add = new Intent(MainActivity.this, AddAlarm.class);
                intent_add.putExtra("max_id", max_id + 1);
                startActivityForResult(intent_add, REQUEST_CODE_INPUT);

            }
        });

        lvListAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               /* Alarm alarm = (Alarm) parent.getItemAtPosition(position);
                Intent intent_edit = new Intent(MainActivity.this, EditAlarm.class);

                String time = alarm.getTimeAlarm();

                intent_edit.putExtra("id", alarm.getIdAlarm());
                intent_edit.putExtra("pCode", alarm.getpCode());
                intent_edit.putExtra("name_alarm", alarm.getNameAlarm());
                intent_edit.putExtra("ring_alarm", alarm.getRingAlarm());
                intent_edit.putExtra("time_alarm", time);
                intent_edit.putExtra("day_alarm",alarm.getDayAlarm());
                intent_edit.putExtra("vibrate",alarm.getVibrate());

                startActivityForResult(intent_edit, REQUEST_CODE_EDIT);*/
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getExtras() != null && data.getExtras().getString("request").equals("add")) {
            showList();
        }
    }

    // show list alarm from database to listview
    public void showList() {
        arrAlarm.clear();
        Cursor kq_alarm = dbHelper.getData("Select * from alarm_table");
        Log.d(TAG, String.valueOf(kq_alarm.getCount()));
        while (kq_alarm.moveToNext()) {
            Alarm alarm = new Alarm();

            alarm.setIdAlarm(kq_alarm.getInt(0));
            alarm.setNameAlarm(kq_alarm.getString(kq_alarm.getColumnIndex("name_alarm")));
            alarm.setHourAlarm(kq_alarm.getInt(kq_alarm.getColumnIndex("hour")));
            alarm.setMinuteAlarm(kq_alarm.getInt(kq_alarm.getColumnIndex("minute")));
            alarm.setRingAlarm(kq_alarm.getString(kq_alarm.getColumnIndex("ring_alarm")));
            if (kq_alarm.getString(kq_alarm.getColumnIndex("vibrate")).equals("yes"))
                alarm.setVibrate(true);
            else alarm.setVibrate(false);
            alarm.setArrDay(kq_alarm.getString(kq_alarm.getColumnIndex("arr_day")));

            arrAlarm.add(alarm);
        }
        Log.d(TAG, "list alarm");

        adapterAlarm.notifyDataSetChanged();
    }

}
