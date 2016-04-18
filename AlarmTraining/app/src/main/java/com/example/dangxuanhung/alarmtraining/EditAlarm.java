package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dang Xuan Hung on 05/03/2016.
 */
public class EditAlarm extends Activity {

    public static final int REQUEST_CODE_EDIT=2;

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

    final CharSequence alarmMode[] ={"Default","Play Game"};

    ArrayList<Integer> selList=new ArrayList();
    ArrayList<Integer> arrDay = new ArrayList<>();

    boolean bl[] = new boolean[alarmMode.length];

    String msg ="";

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_alarm);

        getControl();

        this.context=getApplicationContext();
        calendar = Calendar.getInstance();

        final Intent intent_edit = getIntent();
        final int id = intent_edit.getExtras().getInt("id");
        final int pCode = intent_edit.getExtras().getInt("pCode");
        String day_alarm = intent_edit.getStringExtra("day_alarm");
        Boolean vibrate_checked = intent_edit.getExtras().getBoolean("vibrate");

        getDefaultInfor(intent_edit);
        getDayView(day_alarm);
        getVibrateView(vibrate_checked);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i= new Intent(EditAlarm.this,MainActivity.class);
               // setResult(MainActivity.REQUEST_CODE_EDIT, i);
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

                my_intent.putExtra("extra", "on");
                my_intent.putExtra("ring_alarm",tvSelectRing.getText().toString());
                my_intent.putExtra("pCode", pCode);
                my_intent.putExtra("arrDay",arrDay);
                my_intent.putExtra("hour",calendar.get(Calendar.HOUR_OF_DAY));
                my_intent.putExtra("minute",calendar.get(Calendar.MINUTE));

                // create pendingIntent setDay();

                pending_intent = PendingIntent.getBroadcast(EditAlarm.this,pCode,
                        my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

                // set alarm manager
                AlarmManager alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                alarm_manager.set(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), pending_intent);

                sendToMain(MainActivity.REQUEST_CODE_EDIT, edtNameAlarm.getText().toString(), id);

                /*alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 30000, pending_intent);*/

            }
        });

        // click Delete button to Delete Alarm
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_dellete = new Intent(EditAlarm.this,MainActivity.class);
                intent_dellete.putExtra("request","delete");
                intent_dellete.putExtra("id", id);
                setResult(MainActivity.REQUEST_CODE_EDIT, intent_dellete);

                Intent i_del = new Intent(EditAlarm.this,AlarmReceiver.class);
                PendingIntent p_intent_del = PendingIntent.getBroadcast(EditAlarm.getAppContext(), pCode, i_del, PendingIntent.FLAG_UPDATE_CURRENT);

                Log.e("PCode Del", String.valueOf(pCode));
                AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarm.cancel(p_intent_del);
                finish();
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
    public void sendToMain(int resultcode, String name,int id)
    {
        Intent i=getIntent();
        i.putExtra("request","edit");
        i.putExtra("id",id);
      //  i.putExtra("pCode",pCode);
        i.putExtra("time_alarm",calendar.getTimeInMillis());
        i.putExtra("name_alarm",name);
        i.putExtra("ring_alarm",tvSelectRing.getText());
        i.putExtra("day_alarm",arrDay);
        if(cbVibrate.isChecked()){
            i.putExtra("vibrate","yes");
        }
        else
            i.putExtra("vibrate","no");
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
        }
        if(cbTue.isChecked()){
            arrDay.add(3);
        }
        if(cbWen.isChecked()){
            arrDay.add(4);
        }
        if(cbThu.isChecked()){
            arrDay.add(5);
        }
        if(cbFri.isChecked()){
            arrDay.add(6);
        }
        if(cbSat.isChecked()){
            arrDay.add(7);
        }
        if(cbSun.isChecked()){
            arrDay.add(1);
        }
    }

    public void getDefaultInfor(Intent i){

        /*
        calendar = Calendar.getInstance();
        SimpleDateFormat dft = null;

        if(DateFormat.is24HourFormat(getApplication())){
            dft = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
        else {
            dft = new SimpleDateFormat("HH:mm:aa", Locale.getDefault());
        }
        */

        tvTime.setTag(i.getStringExtra("time_alarm"));

        tvTime.setText(i.getStringExtra("time_alarm"));
        tvSelectRing.setText(i.getStringExtra("ring_alarm"));
        edtNameAlarm.setText(i.getStringExtra("name_alarm"));
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

    public void getVibrateView(Boolean v){
        if(v){
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
