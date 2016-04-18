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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dang Xuan Hung on 07/03/2016.
 */
public class AddAlarm extends Activity {

    public static int REQUEST_CODE_INPUT =1;

    Calendar calendar ;
    TextView tvTime;
    Button btnBack;
    Button btnSave;
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
    //AlarmManager alarm_manager;

    final CharSequence alarmMode[] ={"Default","Play Game"};

    ArrayList<Integer> selList=new ArrayList();
    ArrayList<Integer> arrDay = new ArrayList<>();

    boolean bl[] = new boolean[alarmMode.length];

    String msg ="";

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm);

        this.context=getApplicationContext();

        getViewLayout();
        getDefaultInfor();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i= new Intent(AddAlarm.this,MainActivity.class);
                //setResult(MainActivity.REQUEST_CODE_INPUT, i);
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

        final Intent my_intent = new Intent(AddAlarm.getAppContext(),AlarmReceiver.class);
        Intent i = getIntent();

        final int pCode = i.getExtras().getInt("pCode");
        Log.e("P_CODE", String.valueOf(pCode));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDay();

                my_intent.putExtra("extra", "on");
                my_intent.putExtra("ring_alarm", tvSelectRing.getText().toString());
                my_intent.putExtra("pCode", pCode);
                my_intent.putExtra("arrDay", arrDay);
                my_intent.putExtra("hour",calendar.get(Calendar.HOUR_OF_DAY));
                my_intent.putExtra("minute",calendar.get(Calendar.MINUTE));


                // create pendingIntent
                pending_intent = PendingIntent.getBroadcast(getApplicationContext(), pCode,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Log.e("PCode Add", String.valueOf(pCode));

                // set alarm manager

                 AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

                 alarm_manager.set(AlarmManager.RTC_WAKEUP,
                         calendar.getTimeInMillis(), pending_intent);

                 sendToMain(MainActivity.REQUEST_CODE_INPUT, edtNameAlarm.getText().toString(), pCode);

                //alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),30000,pending_intent);
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
    public void sendToMain(int resultcode, String name,int pCode)
    {
        Intent i=getIntent();
        i.putExtra("request","add");
        i.putExtra("pCode",pCode);
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

                timeSelect = calendar.getTime();
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
