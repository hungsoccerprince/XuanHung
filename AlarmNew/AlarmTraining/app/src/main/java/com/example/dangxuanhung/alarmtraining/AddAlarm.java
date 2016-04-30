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
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dang Xuan Hung on 07/03/2016.
 */
public class AddAlarm extends Activity {

    private static final String TAG = AddAlarm.class.getSimpleName();
    public static int REQUEST_CODE_INPUT =1;

    private int max_id;
    private Calendar calendar;
    private TextView tvTime,tvAlarmMode,tvSelectRing;
    private Button btnBack,btnSave;
    private CheckBox cbMon,cbTue,cbWen,cbThu,cbFri,cbSat,cbSun,cbVibrate;
    private EditText edtNameAlarm;
    private static Context context;
    private DatabaseHelper dbHelper;
    final CharSequence alarmMode[] ={"Default","Play Game"};
    private ArrayList<Integer> selList;
    private ArrayList<Integer> arrDay;
    private ArrayList<DayAlarm> arrDayAlarm ;
    private String array_day_string ="";
    private boolean bl[] = new boolean[alarmMode.length];
    private String msg ="";

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm);

        selList=new ArrayList();
        arrDay = new ArrayList<>();
        arrDayAlarm = new ArrayList<>();
        dbHelper = new DatabaseHelper(this);

        this.context=getApplicationContext();
        max_id =getIntent().getExtras().getInt("max_id");
        Log.d(TAG, "Max ID "+max_id);

        calendar = Calendar.getInstance();
        arrDayAlarm.clear();
        arrDay.clear();

        getViewLayout();
        getDefaultInfor();

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
                values_alarm.put("state", "on");
                values_alarm.put("vibrate", getVibrate());
                dbHelper.insert(values_alarm,"alarm_table");

                int i=0;
                for(i=0;i<arrDay.size();i++){
                    ContentValues values_day_alarm = new ContentValues();
                    values_day_alarm.put("id_alarm",max_id);
                    values_day_alarm.put("name_alarm",nameAlarm);
                    values_day_alarm.put("ring_alarm",ringAlarm);
                    values_day_alarm.put("hour_alarm",hourAlarm);
                    values_day_alarm.put("minute_alarm",minuteAlarm);
                    values_day_alarm.put("day_alarm", arrDay.get(i));
                    values_alarm.put("state", "on");
                    values_day_alarm.put("vibrate", getVibrate());
                    dbHelper.insert(values_day_alarm,"day_table");
                    Log.d(TAG,"insert day true");
                }

              /*  arrDayAlarm = getArrayDay();
                setAlarm(arrDayAlarm);*/

                Intent i_setAlarm = new Intent(AddAlarm.this,SetAlarmService.class);
                startService(i_setAlarm);
                sendToMain(MainActivity.REQUEST_CODE_INPUT);

            }
        });

        // click textView to select ring tone
        tvSelectRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent select_ring_intent = new Intent(AddAlarm.context, SelectRingTone.class);
                select_ring_intent.putExtra("request","add");
                startActivityForResult(select_ring_intent, REQUEST_CODE_INPUT);
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
        i.putExtra("request","add");

        setResult(resultcode, i);
        finish();
    }

    public static Context getAppContext(){
        return AddAlarm.context;
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

               // timeSelect = calendar.getTime();
            }
        };
        String s = tvTime.getTag() + "";
        String strArr[] = s.split(":");
        int gio = Integer.parseInt(strArr[0]);
        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time = new TimePickerDialog(AddAlarm.this,
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

    public String getVibrate(){
        if(cbVibrate.isChecked())
            return "yes";
        else
            return "no";
    }

    public void getDefaultInfor(){

        calendar = Calendar.getInstance();
        SimpleDateFormat dft = null;

        if(DateFormat.is24HourFormat(getApplication())){
            dft = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
        else {
            dft = new SimpleDateFormat("HH:mm:aa", Locale.getDefault());
        }

        String time = dft.format(calendar.getTime());

        tvTime.setTag(dft.format(calendar.getTime()));

        tvTime.setText(time);
        tvSelectRing.setText("bantinhca");

    }

    private void getViewLayout(){
        tvTime = (TextView) findViewById(R.id.tvTime);
        btnBack = (Button) findViewById(R.id.btnBackMain);
        tvAlarmMode = (TextView)findViewById(R.id.tvAlarmMode);
        btnSave = (Button)findViewById(R.id.btnSave);
        edtNameAlarm = (EditText)findViewById(R.id.edtNameAlarm);
        tvSelectRing = (TextView)findViewById(R.id.tvSelectRing);

        cbMon =(CheckBox)findViewById(R.id.cbMon);
        cbTue =(CheckBox)findViewById(R.id.cbTue);
        cbWen =(CheckBox)findViewById(R.id.cbWen);
        cbThu =(CheckBox)findViewById(R.id.cbThu);
        cbFri =(CheckBox)findViewById(R.id.cbFri);
        cbSat =(CheckBox)findViewById(R.id.cbSat);
        cbSun =(CheckBox)findViewById(R.id.cbSun);

        cbVibrate =(CheckBox)findViewById(R.id.cbVibrate);

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
