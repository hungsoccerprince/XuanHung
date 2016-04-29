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
    Calendar calendar;
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
    DatabaseHelper dbHelper;
    final CharSequence alarmMode[] ={"Default","Play Game"};

    ArrayList<Integer> selList;
    ArrayList<Integer> arrDay;
    ArrayList<DayAlarm> arrDayAlarm ;
    String array_day_string ="";

    boolean bl[] = new boolean[alarmMode.length];

    String msg ="";

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
                /*Intent i= new Intent(AddAlarm.this,MainActivity.class);
                setResult(REQUEST_CODE_INPUT, i);*/
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
                String nameAlarm = edtNameAlarm.getText().toString();
                int hourAlarm = calendar.get(Calendar.HOUR_OF_DAY);
                int minuteAlarm = calendar.get(Calendar.MINUTE);
                String ringAlarm = tvSelectRing.getText().toString();
                String vibrate = "no";

                ContentValues values_alarm = new ContentValues();
                values_alarm.put("name_alarm",nameAlarm);
                values_alarm.put("ring_alarm",ringAlarm);
                values_alarm.put("hour",hourAlarm);
                values_alarm.put("minute",minuteAlarm);
                values_alarm.put("arr_day", array_day_string);
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
                    values_day_alarm.put("vibrate", getVibrate());
                    dbHelper.insert(values_day_alarm,"day_table");
                    Log.d(TAG,"insert day true");
                }

                arrDayAlarm = getArrayDay();
                setAlarm(arrDayAlarm);
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

    // get anh sách Day from database
    public ArrayList<DayAlarm> getArrayDay() {
        ArrayList<DayAlarm> arrDayAlarm = new ArrayList<>();
        Cursor kq_day = dbHelper.getData("Select * from day_table");
        while (kq_day.moveToNext()) {
            DayAlarm dayAlarm = new DayAlarm();

            dayAlarm.setIdDay(kq_day.getInt(0));
            Log.d(TAG,"ID DAY : "+dayAlarm.getIdDay());

            dayAlarm.setIdAlarm(kq_day.getInt(kq_day.getColumnIndex("id_alarm")));
            dayAlarm.setDay(kq_day.getInt(kq_day.getColumnIndex("day_alarm")));
            dayAlarm.setHour(kq_day.getInt(kq_day.getColumnIndex("hour_alarm")));
            dayAlarm.setMinute(kq_day.getInt(kq_day.getColumnIndex("minute_alarm")));
            dayAlarm.setNameAlarm(kq_day.getString(kq_day.getColumnIndex("name_alarm")));
            dayAlarm.setRingAlarm(kq_day.getString(kq_day.getColumnIndex("ring_alarm")));
            dayAlarm.setVibrate(kq_day.getString(kq_day.getColumnIndex("vibrate")));

            arrDayAlarm.add(dayAlarm);
        }
        return arrDayAlarm;
    }

    // Đặt báo thức gần nhất
    public void setAlarm(ArrayList<DayAlarm> arr){
        DayAlarm dayAlarmSelect = new DayAlarm();
        int i=0;
        ArrayList<DayAlarm> list0 = new ArrayList<>(); // list báo thức tuần tiếp theo
        ArrayList<DayAlarm> list1 = new ArrayList<>(); // list báo thức trong ngày
        ArrayList<DayAlarm> list2 = new ArrayList<>(); // list báo thức sắp tới trong tuần
        Calendar calendar_now = Calendar.getInstance();
        int nowDay = calendar_now.get(Calendar.DAY_OF_WEEK);
        int nowTime = (int) calendar_now.getTimeInMillis();
        Log.d(TAG,"minute now"+calendar_now.get(Calendar.MINUTE));

        for(i=0;i<arr.size();i++){
            if(arr.get(i).getDay() > nowDay){
                list2.add(arr.get(i));
            }
            if(arr.get(i).getDay()== nowDay){
                list1.add(arr.get(i));
            }
            if(arr.get(i).getDay()<nowDay){
                list0.add(arr.get(i));
            }
        }
        Log.d(TAG, "lis0 : "+list0.size());
        Log.d(TAG, "lis1 : "+list1.size());
        Log.d(TAG, "lis2 : "+list2.size());

        if (list1.size()>=1) {
            ArrayList<DayAlarm> mlist = new ArrayList<>();// list alarm gần nhất trong ngày
            int j=0;
            for(j=0;j<list1.size();j++){
                Calendar c = Calendar.getInstance() ;
                c.set(Calendar.HOUR_OF_DAY,list1.get(j).getHour());
                c.set(Calendar.MINUTE,list1.get(j).getMinute());

                if(c.getTimeInMillis()>=Calendar.getInstance().getTimeInMillis()){
                    mlist.add(list1.get(j));
                }
            }
            Log.d(TAG,"mlist : "+String.valueOf(mlist.size()));
            if(mlist.size()==1){
                Log.d(TAG,"1");
                dayAlarmSelect.setIdDay(mlist.get(0).getIdDay());
                dayAlarmSelect.setIdAlarm(mlist.get(0).getIdAlarm());
                dayAlarmSelect.setDay(mlist.get(0).getDay());
                dayAlarmSelect.setHour(mlist.get(0).getHour());
                dayAlarmSelect.setMinute(mlist.get(0).getMinute());
                dayAlarmSelect.setNameAlarm(mlist.get(0).getNameAlarm());
                dayAlarmSelect.setRingAlarm(mlist.get(0).getRingAlarm());
                dayAlarmSelect.setVibrate(mlist.get(0).getVibrate());

                Log.d(TAG,"hour today"+ String.valueOf(dayAlarmSelect.getHour()));
                Log.d(TAG,"minute today"+ String.valueOf(dayAlarmSelect.getMinute()));

                Intent my_intent = new Intent(AddAlarm.this,AlarmReceiver.class);
                my_intent.putExtra("name",dayAlarmSelect.getNameAlarm());
                my_intent.putExtra("ring",dayAlarmSelect.getRingAlarm());
                my_intent.putExtra("vibrate",dayAlarmSelect.getVibrate());
                my_intent.putExtra("day",dayAlarmSelect.getDay());
                my_intent.putExtra("hour",dayAlarmSelect.getHour());
                my_intent.putExtra("minute",dayAlarmSelect.getMinute());
                my_intent.putExtra("extra", "on");

                Calendar calendar_alarm = Calendar.getInstance();
                calendar_alarm.set(Calendar.HOUR_OF_DAY,dayAlarmSelect.getHour());
                calendar_alarm.set(Calendar.MINUTE,dayAlarmSelect.getMinute());

                pending_intent = PendingIntent.getBroadcast(AddAlarm.this,1,
                        my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                alarm_manager.set(AlarmManager.RTC_WAKEUP,
                        calendar_alarm.getTimeInMillis(), pending_intent);
            } else if(mlist.size()>1){
                int choose=0;
                int k=1;
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                for(k=1;k<mlist.size();k++){

                    c1.set(Calendar.HOUR_OF_DAY,mlist.get(choose).getHour());
                    c1.set(Calendar.MINUTE,mlist.get(choose).getMinute());

                    c2.set(Calendar.HOUR_OF_DAY,mlist.get(k).getHour());
                    c2.set(Calendar.MINUTE,mlist.get(k).getMinute());
                    if(c2.getTimeInMillis()<c1.getTimeInMillis()){
                        choose=k;
                    }
                }
                dayAlarmSelect.setIdDay(mlist.get(choose).getIdDay());
                dayAlarmSelect.setIdAlarm(mlist.get(choose).getIdAlarm());
                dayAlarmSelect.setDay(mlist.get(choose).getDay());
                dayAlarmSelect.setHour(mlist.get(choose).getHour());
                dayAlarmSelect.setMinute(mlist.get(choose).getMinute());
                dayAlarmSelect.setNameAlarm(mlist.get(choose).getNameAlarm());
                dayAlarmSelect.setRingAlarm(mlist.get(choose).getRingAlarm());
                dayAlarmSelect.setVibrate(mlist.get(choose).getVibrate());

                Log.d(TAG,"today day : "+ dayAlarmSelect.getDay() );
                Log.d(TAG,"today hour : "+ dayAlarmSelect.getHour() );
                Log.d(TAG,"today minute : "+ dayAlarmSelect.getMinute() );

                Intent my_intent = new Intent(AddAlarm.this,AlarmReceiver.class);
                my_intent.putExtra("name",dayAlarmSelect.getNameAlarm());
                my_intent.putExtra("ring",dayAlarmSelect.getRingAlarm());
                my_intent.putExtra("vibrate",dayAlarmSelect.getVibrate());
                my_intent.putExtra("day",dayAlarmSelect.getDay());
                my_intent.putExtra("hour",dayAlarmSelect.getHour());
                my_intent.putExtra("minute",dayAlarmSelect.getMinute());
                my_intent.putExtra("extra", "on");

                Calendar calendar_alarm = Calendar.getInstance();
                calendar_alarm.set(Calendar.HOUR_OF_DAY,dayAlarmSelect.getHour());
                calendar_alarm.set(Calendar.MINUTE,dayAlarmSelect.getMinute());

                pending_intent = PendingIntent.getBroadcast(AddAlarm.this,1,
                        my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                alarm_manager.set(AlarmManager.RTC_WAKEUP,
                        calendar_alarm.getTimeInMillis(), pending_intent);
            }
        }
        else if(list2.size()==1){ // chỉ có 1 báo thức trong các ngày còn lại của tuần
            int d1 = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            int h1 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int m1 = Calendar.getInstance().get(Calendar.MINUTE);
            int d2 = list2.get(0).getDay();
            int h2 = list2.get(0).getHour();
            int m2 = list2.get(0).getMinute();

            int minute = (d2-d1-1)*24*60 + (24-h1)*60-m1 + h2*60+m2 ;
            long milis = minute*60000 ;
            long time_alarm = Calendar.getInstance().getTimeInMillis()+ milis;

            Intent my_intent = new Intent(AddAlarm.this,AlarmReceiver.class);
            my_intent.putExtra("name",list2.get(0).getNameAlarm());
            my_intent.putExtra("ring",list2.get(0).getRingAlarm());
            my_intent.putExtra("vibrate",list2.get(0).getVibrate());
            my_intent.putExtra("day",list2.get(0).getDay());
            my_intent.putExtra("hour",list2.get(0).getHour());
            my_intent.putExtra("minute",list2.get(0).getMinute());
            my_intent.putExtra("extra", "on");

            Log.d(TAG,"HOUR :"+list2.get(0).getHour());
            Log.d(TAG,"MINUTE :"+list2.get(0).getMinute());

            pending_intent = PendingIntent.getBroadcast(AddAlarm.this,1,
                    my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarm_manager.set(AlarmManager.RTC_WAKEUP,
                    time_alarm, pending_intent);

        }
        else if(list2.size()>1){ // có nhiều hơn 1 alarm trong các ngày còn lại của tuần
            int choose = 0;
            int j=1;
            for(j=1;j<list2.size();j++){
                Calendar c_choose = Calendar.getInstance();
                c_choose.set(Calendar.HOUR_OF_DAY,list2.get(choose).getHour());
                c_choose.set(Calendar.MINUTE,list2.get(choose).getMinute());
                long time_choose = c_choose.getTimeInMillis();

                if(list2.get(j).getDay()<list2.get(choose).getDay()){
                    choose=j;
                }
                else if(list2.get(j).getDay()==list2.get(choose).getDay()){
                    Calendar c_for = Calendar.getInstance();
                    c_for.set(Calendar.HOUR_OF_DAY,list2.get(j).getHour());
                    c_for.set(Calendar.MINUTE,list2.get(j).getMinute());
                    long time_for = c_for.getTimeInMillis();
                    if(time_for<time_choose){
                        choose=j;
                    }
                }
            }
            Log.d(TAG,"list2 day min : " +list2.get(choose).getDay());
            Log.d(TAG,"list2 hour min : " +list2.get(choose).getHour());
            Log.d(TAG,"list2 minute min : " +list2.get(choose).getMinute());
            int d1 = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            int h1 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int m1 = Calendar.getInstance().get(Calendar.MINUTE);
            int d2 = list2.get(choose).getDay();
            int h2 = list2.get(choose).getHour();
            int m2 = list2.get(choose).getMinute();

            int minute = (d2-d1-1)*24*60 + (24-h1)*60-m1 + h2*60+m2 ;
            long milis = minute*60000 ;
            long time_alarm = Calendar.getInstance().getTimeInMillis()+ milis;

            Intent my_intent = new Intent(AddAlarm.this,AlarmReceiver.class);
            my_intent.putExtra("name",list2.get(choose).getNameAlarm());
            my_intent.putExtra("ring",list2.get(choose).getRingAlarm());
            my_intent.putExtra("vibrate",list2.get(choose).getVibrate());
            my_intent.putExtra("day",list2.get(choose).getDay());
            my_intent.putExtra("hour",list2.get(choose).getHour());
            my_intent.putExtra("minute",list2.get(choose).getMinute());
            my_intent.putExtra("extra", "on");

            pending_intent = PendingIntent.getBroadcast(AddAlarm.this,1,
                    my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarm_manager.set(AlarmManager.RTC_WAKEUP,
                    time_alarm, pending_intent);

        }
        else
        if(list0.size()==1){
            int d1 = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            int h1 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int m1 = Calendar.getInstance().get(Calendar.MINUTE);
            int d2 = list0.get(0).getDay();
            int h2 = list0.get(0).getHour();
            int m2 = list0.get(0).getMinute();

            int minute = (7-d1)*24*60 + (24-h2)*60-m1 + (d2-1)*24*60 + h2*60 + m2 ;
            long milis = minute*60000 ;
            long time_alarm = Calendar.getInstance().getTimeInMillis()+ milis;

            Intent my_intent = new Intent(AddAlarm.this,AlarmReceiver.class);
            my_intent.putExtra("name",list0.get(0).getNameAlarm());
            my_intent.putExtra("ring",list0.get(0).getRingAlarm());
            my_intent.putExtra("vibrate",list0.get(0).getVibrate());
            my_intent.putExtra("day",list0.get(0).getDay());
            my_intent.putExtra("hour",list0.get(0).getHour());
            my_intent.putExtra("minute",list0.get(0).getMinute());
            my_intent.putExtra("extra", "on");

            Log.d(TAG,"HOUR_List0 :"+list0.get(0).getHour());
            Log.d(TAG,"MINUTE_List0 :"+list0.get(0).getMinute());

            pending_intent = PendingIntent.getBroadcast(AddAlarm.this,1,
                    my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarm_manager.set(AlarmManager.RTC_WAKEUP,
                    time_alarm, pending_intent);

        }
        else if(list0.size()>1){
            int choose = 0;
            int j=1;
            for(j=1;j<list0.size();j++){
                Calendar c_choose = Calendar.getInstance();
                c_choose.set(Calendar.HOUR_OF_DAY,list0.get(choose).getHour());
                c_choose.set(Calendar.MINUTE,list0.get(choose).getMinute());
                long time_choose = c_choose.getTimeInMillis();

                if(list0.get(j).getDay()<list0.get(choose).getDay()){
                    choose=j;
                }
                else if(list0.get(j).getDay()==list0.get(choose).getDay()){
                    Calendar c_for = Calendar.getInstance();
                    c_for.set(Calendar.HOUR_OF_DAY,list0.get(j).getHour());
                    c_for.set(Calendar.MINUTE,list0.get(j).getMinute());
                    long time_for = c_for.getTimeInMillis();
                    if(time_for<time_choose){
                        choose=j;
                    }
                }
            }

            int d1 = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            int h1 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int m1 = Calendar.getInstance().get(Calendar.MINUTE);
            int d2 = list0.get(choose).getDay();
            int h2 = list0.get(choose).getHour();
            int m2 = list0.get(choose).getMinute();

            int minute = (7-d1)*24*60 + (24-h2)*60-m1 + (d2-1)*24*60 + h2*60 + m2 ;
            long milis = minute*60000 ;
            long time_alarm = Calendar.getInstance().getTimeInMillis()+ milis;

            Intent my_intent = new Intent(AddAlarm.this,AlarmReceiver.class);
            my_intent.putExtra("name",list0.get(choose).getNameAlarm());
            my_intent.putExtra("ring",list0.get(choose).getRingAlarm());
            my_intent.putExtra("vibrate",list0.get(choose).getVibrate());
            my_intent.putExtra("day",list0.get(choose).getDay());
            my_intent.putExtra("hour",list0.get(choose).getHour());
            my_intent.putExtra("minute",list0.get(choose).getMinute());
            my_intent.putExtra("extra", "on");

            Log.d(TAG,"DAY_List0 choose :"+list0.get(choose).getDay());
            Log.d(TAG,"HOUR_List0 choose:"+list0.get(choose).getHour());
            Log.d(TAG,"MINUTE_List0 choose:"+list0.get(choose).getMinute());

            pending_intent = PendingIntent.getBroadcast(AddAlarm.this,1,
                    my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarm_manager.set(AlarmManager.RTC_WAKEUP,
                    time_alarm, pending_intent);
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
