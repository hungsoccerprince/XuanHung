package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dangxuanhung.alarmtraining.model.DayAlarm;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.credentials.internal.SaveRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dang Xuan Hung on 05/03/2016.
 */
public class EditAlarm extends Activity {

    public static final int REQUEST_CODE_EDIT=2;
    private static final String TAG = EditAlarm.class.getSimpleName() ;

    Calendar calendar;
    TextView tvTime;
    Button btnBack;
    Button btnSave;
    Button btnDelete;
    CheckBox cbMon;
    CheckBox cbTue;
    CheckBox cbWen;
    CheckBox cbThu;
    CheckBox cbFri;
    CheckBox cbSat;
    CheckBox cbSun;

    CheckBox cbVibrate;

    Date timeSelect;
    TextView tvAlarmMode;
    TextView tvSelectRing;
    EditText edtNameAlarm;
    private static Context context;
    PendingIntent pending_intent;
    DatabaseHelper dbHelper;
    String array_day_string ="";
    private int id;

    final CharSequence alarmMode[] ={"Default","Play Game"};

    ArrayList<Integer> selList;
    ArrayList<Integer> arrDay;
    ArrayList<DayAlarm> arrDayAlarm ;

    boolean bl[] = new boolean[alarmMode.length];

    String msg ="";

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_alarm);
        getControl();
        selList=new ArrayList();
        arrDay = new ArrayList<>();
        arrDayAlarm = new ArrayList<>();

        this.context=getApplicationContext();
        calendar = Calendar.getInstance();
        dbHelper = new DatabaseHelper(this);

        final Intent intent_edit = getIntent();
        id = intent_edit.getExtras().getInt("id");
        Log.d(TAG, "id edit + " + id);
        String day_alarm = intent_edit.getStringExtra("arr_day_string");
        String vibrate_checked = intent_edit.getExtras().getString("vibrate");

        getDefaultInfor(intent_edit);
        getDayView(day_alarm);
        getVibrateView(vibrate_checked);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
// click textview Time chọn tgoi gian
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

// chọn cách báo thức
        tvAlarmMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();

            }

        });

        final Intent my_intent = new Intent(EditAlarm.getAppContext(),AlarmReceiver.class);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDay();
                String nameAlarm = edtNameAlarm.getText().toString();
                int hourAlarm = calendar.get(Calendar.HOUR_OF_DAY);
                int minuteAlarm = calendar.get(Calendar.MINUTE);
                String ringAlarm = tvSelectRing.getText().toString();

                ContentValues values_alarm = new ContentValues();
                values_alarm.put("name_alarm",nameAlarm);
                values_alarm.put("ring_alarm",ringAlarm);
                values_alarm.put("hour",hourAlarm);
                values_alarm.put("minute",minuteAlarm);
                values_alarm.put("arr_day", array_day_string);
                values_alarm.put("state","on");
                values_alarm.put("vibrate", getVibrate());
                values_alarm.put("mode", tvAlarmMode.getText().toString());
                dbHelper.update(values_alarm,"_id_alarm="+id,"alarm_table");
                dbHelper.delete("day_table","id_alarm="+id);

                int i=0;
                for(i=0;i<arrDay.size();i++) {
                    ContentValues values_day_alarm = new ContentValues();
                    values_day_alarm.put("id_alarm", id);
                    values_day_alarm.put("name_alarm", nameAlarm);
                    values_day_alarm.put("ring_alarm", ringAlarm);
                    values_day_alarm.put("hour_alarm", hourAlarm);
                    values_day_alarm.put("minute_alarm", minuteAlarm);
                    values_day_alarm.put("day_alarm", arrDay.get(i));
                    values_day_alarm.put("state", "on");
                    values_day_alarm.put("vibrate", getVibrate());
                    values_day_alarm.put("mode", tvAlarmMode.getText().toString());
                    dbHelper.insert(values_day_alarm, "day_table");
                }

                Log.d(TAG, "mode add : "+ tvAlarmMode.getText().toString());
                Intent i_setAlarm = new Intent(EditAlarm.this,SetAlarmService.class);
                startService(i_setAlarm);

                sendToMain(MainActivity.REQUEST_CODE_EDIT);

            }
        });

        // click Delete button to Delete Alarm
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // xóa csdl
                dbHelper.delete("alarm_table","_id_alarm="+id);
                dbHelper.delete("day_table","id_alarm="+id);

                // set alarm mới
                Intent i_setAlarm = new Intent(EditAlarm.this,SetAlarmService.class);
                startService(i_setAlarm);
                sendToMain(MainActivity.REQUEST_CODE_EDIT);
            }
        });

        // click textView to select ring tone
        tvSelectRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent select_ring_intent = new Intent(EditAlarm.context,SelectRingTone.class);
                select_ring_intent.putExtra("request","edit");
                startActivityForResult(select_ring_intent, REQUEST_CODE_EDIT);
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null){
            String selectRing = data.getExtras().getString("name_ring");
            tvSelectRing.setText(selectRing);
        }
    }

    // gửi du liệu về mainactivity
    public void sendToMain(int resultcode)
    {
        Intent i=getIntent();
        i.putExtra("request","edit");
        setResult(resultcode, i);
        finish();
    }

    public static Context getAppContext(){
        return EditAlarm.context;
    }

    public void showTimePickerDialog() {
        final TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String s = hourOfDay + ":" + minute;
                int hourTam = hourOfDay;

                if(DateFormat.is24HourFormat(getApplication())){
                    tvTime.setText(hourTam + ":" + minute);
                }
                else {
                    if(hourTam>12){
                        hourTam = hourTam - 12;
                    }
                    tvTime.setText(hourTam + ":" + minute
                            + (hourOfDay > 12 ? " PM" : " AM"));
                }

                tvTime.setTag(s);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                timeSelect = calendar.getTime();
            }
        };
        String s = tvTime.getTag() + "";
        String strArr[] = s.split(":");
        int gio = Integer.parseInt(strArr[0]);
        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time = new TimePickerDialog(EditAlarm.this,
                callback, gio, phut, true);
        time.setTitle("Chọn giờ");
        time.show();
    }

    public void alertDialog(){
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Choose a mode !");

        ad.setMultiChoiceItems(alarmMode, bl, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                if(arg2)
                {
                    msg=alarmMode[arg1].toString();
                }
                else if (selList.contains(arg1))
                {
                }
            }
        });
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvAlarmMode.setText(msg);
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),
                        "You Have Cancel the Dialog box", Toast.LENGTH_LONG)
                        .show();
            }
        });

        tvAlarmMode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                msg = "";
                ad.show();
            }
        });
    }

    public void setDay(){
        arrDay.clear();
        if(cbMon.isChecked()){
            arrDay.add(2);
            array_day_string += "2";
        }
        if(cbTue.isChecked()){
            arrDay.add(3);
            array_day_string += "3";
        }
        if(cbWen.isChecked()){
            arrDay.add(4);
            array_day_string += "4";
        }
        if(cbThu.isChecked()){
            arrDay.add(5);
            array_day_string += "5";
        }
        if(cbFri.isChecked()){
            arrDay.add(6);
            array_day_string += "6";
        }
        if(cbSat.isChecked()){
            arrDay.add(7);
            array_day_string += "7";
        }
        if(cbSun.isChecked()){
            arrDay.add(1);
            array_day_string += "1";
        }
    }

    public String getVibrate() {
        if (cbVibrate.isChecked())
            return "yes";
        else
            return "no";
    }

    public void getDefaultInfor(Intent i){
        String s_hour = "";
        String s_minute = "";
        int hour = i.getExtras().getInt("hour");
        int minute = i.getExtras().getInt("minute");
        if(hour<10)
            s_hour = "0"+String.valueOf(hour);
        else
            s_hour = String.valueOf(hour);
        if(minute<10)
            s_minute = "0"+String.valueOf(minute);
        else
            s_minute = String.valueOf(minute);

        String time = s_hour + ":" + s_minute;
        tvTime.setTag(time);
        tvTime.setText(time);
        tvSelectRing.setText(i.getStringExtra("ring_alarm"));
        edtNameAlarm.setText(i.getStringExtra("name_alarm"));
        tvAlarmMode.setText(i.getStringExtra("mode"));

        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
    }

    public void getControl(){
        tvTime = (TextView) findViewById(R.id.tvTime);
        btnBack = (Button) findViewById(R.id.btnBackMain);
        tvAlarmMode = (TextView)findViewById(R.id.tvAlarmMode);
        btnSave = (Button)findViewById(R.id.btnSave);
        edtNameAlarm = (EditText)findViewById(R.id.edtNameAlarm);
        tvSelectRing = (TextView)findViewById(R.id.tvSelectRing);
        btnDelete = (Button)findViewById(R.id.btnDelete);

        cbMon =(CheckBox)findViewById(R.id.cbMon);
        cbTue =(CheckBox)findViewById(R.id.cbTue);
        cbWen =(CheckBox)findViewById(R.id.cbWen);
        cbThu =(CheckBox)findViewById(R.id.cbThu);
        cbFri =(CheckBox)findViewById(R.id.cbFri);
        cbSat =(CheckBox)findViewById(R.id.cbSat);
        cbSun =(CheckBox)findViewById(R.id.cbSun);

        cbVibrate =(CheckBox)findViewById(R.id.cbVibrate);
    }

    public void getDayView(String day_alarm){
        if(day_alarm.contains("1")){
            cbSun.setChecked(true);
        }
        if(day_alarm.contains("2")){
            cbMon.setChecked(true);
        }
        if(day_alarm.contains("3")){
            cbTue.setChecked(true);
        }
        if(day_alarm.contains("4")){
            cbWen.setChecked(true);
        }
        if(day_alarm.contains("5")){
            cbThu.setChecked(true);
        }
        if(day_alarm.contains("6")){
            cbFri.setChecked(true);
        }
        if(day_alarm.contains("7")){
            cbSat.setChecked(true);
        }

    }

    public void getVibrateView(String v){
        if(v.equals("yes")){
            cbVibrate.setChecked(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditAlarm Page", // TODO: Define a title for the content shown.
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
                "EditAlarm Page", // TODO: Define a title for the content shown.
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
