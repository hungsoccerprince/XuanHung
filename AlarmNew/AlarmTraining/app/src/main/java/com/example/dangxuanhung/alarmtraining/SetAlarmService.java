package com.example.dangxuanhung.alarmtraining;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dangxuanhung.alarmtraining.model.DayAlarm;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by hungdx on 29/04/2016.
 */
public class SetAlarmService extends Service {

    private static final String TAG = SetAlarmService.class.getSimpleName() ;
    private DatabaseHelper dbHelper;
    private ArrayList<DayAlarm> arrAlarm;
    private PendingIntent pending_intent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        dbHelper = new DatabaseHelper(this);
        arrAlarm = new ArrayList<DayAlarm>();
        arrAlarm = getArrayDay();
        setAlarm(arrAlarm);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // get anh sách Day from database
    public ArrayList<DayAlarm> getArrayDay() {
        ArrayList<DayAlarm> arrDayAlarm = new ArrayList<>();
        Cursor kq_day = dbHelper.getData("Select * from day_table where state='on'");
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

                Intent my_intent = new Intent(SetAlarmService.this,AlarmReceiver.class);
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

                pending_intent = PendingIntent.getBroadcast(SetAlarmService.this,1,
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

                Intent my_intent = new Intent(SetAlarmService.this,AlarmReceiver.class);
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

                pending_intent = PendingIntent.getBroadcast(SetAlarmService.this,1,
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

            Intent my_intent = new Intent(SetAlarmService.this,AlarmReceiver.class);
            my_intent.putExtra("name",list2.get(0).getNameAlarm());
            my_intent.putExtra("ring",list2.get(0).getRingAlarm());
            my_intent.putExtra("vibrate",list2.get(0).getVibrate());
            my_intent.putExtra("day",list2.get(0).getDay());
            my_intent.putExtra("hour",list2.get(0).getHour());
            my_intent.putExtra("minute",list2.get(0).getMinute());
            my_intent.putExtra("extra", "on");

            Log.d(TAG,"HOUR :"+list2.get(0).getHour());
            Log.d(TAG,"MINUTE :"+list2.get(0).getMinute());

            pending_intent = PendingIntent.getBroadcast(SetAlarmService.this,1,
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

            Intent my_intent = new Intent(SetAlarmService.this,AlarmReceiver.class);
            my_intent.putExtra("name",list2.get(choose).getNameAlarm());
            my_intent.putExtra("ring",list2.get(choose).getRingAlarm());
            my_intent.putExtra("vibrate",list2.get(choose).getVibrate());
            my_intent.putExtra("day",list2.get(choose).getDay());
            my_intent.putExtra("hour",list2.get(choose).getHour());
            my_intent.putExtra("minute",list2.get(choose).getMinute());
            my_intent.putExtra("extra", "on");

            pending_intent = PendingIntent.getBroadcast(SetAlarmService.this,1,
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

            Intent my_intent = new Intent(SetAlarmService.this,AlarmReceiver.class);
            my_intent.putExtra("name",list0.get(0).getNameAlarm());
            my_intent.putExtra("ring",list0.get(0).getRingAlarm());
            my_intent.putExtra("vibrate",list0.get(0).getVibrate());
            my_intent.putExtra("day",list0.get(0).getDay());
            my_intent.putExtra("hour",list0.get(0).getHour());
            my_intent.putExtra("minute",list0.get(0).getMinute());
            my_intent.putExtra("extra", "on");

            Log.d(TAG,"HOUR_List0 :"+list0.get(0).getHour());
            Log.d(TAG,"MINUTE_List0 :"+list0.get(0).getMinute());

            pending_intent = PendingIntent.getBroadcast(SetAlarmService.this,1,
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

            Intent my_intent = new Intent(SetAlarmService.this,AlarmReceiver.class);
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

            pending_intent = PendingIntent.getBroadcast(SetAlarmService.this,1,
                    my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarm_manager.set(AlarmManager.RTC_WAKEUP,
                    time_alarm, pending_intent);
        }
    }
}
