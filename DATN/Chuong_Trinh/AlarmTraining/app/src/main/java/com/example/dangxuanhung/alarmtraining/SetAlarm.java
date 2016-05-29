package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.example.dangxuanhung.alarmtraining.model.DayAlarm;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by hungdx on 25/04/2016.
 */
public class SetAlarm {
    private static final String TAG = SetAlarm.class.getSimpleName();
    Calendar calendar = Calendar.getInstance();

    /*public void setAlarm(ArrayList<DayAlarm> arr, Activity activity){
        DayAlarm dayAlarmSelect = new DayAlarm();
        int i=0;
        ArrayList<DayAlarm> list0 = new ArrayList<>(); // list báo thức tuần tiếp theo
        ArrayList<DayAlarm> list1 = new ArrayList<>(); // list báo thức trong ngày
        ArrayList<DayAlarm> list2 = new ArrayList<>(); // list báo thức sắp tới trong tuần
        Calendar calendar_now = Calendar.getInstance();
        int nowDay = calendar_now.get(Calendar.DAY_OF_WEEK);
        int nowTime = (int) calendar_now.getTimeInMillis();

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
        Log.d(TAG, "lis1 : "+list1.size());

        if (list1.size()>=1) {
            ArrayList<DayAlarm> mlist = new ArrayList<>(); // list alarm gần nhất trong ngày
            int j=0;
            for(j=0;j<list1.size();j++){
                Calendar c = Calendar.getInstance() ;
                c.set(Calendar.HOUR_OF_DAY,list1.get(j).getHour());
                c.set(Calendar.MINUTE,list1.get(j).getMinute());
                if(c.getTimeInMillis()>=nowTime){
                    mlist.add(list1.get(j));
                }
            }
            Log.d(TAG,String.valueOf(mlist.size()));
            Log.d(TAG,"day 1 : "+ mlist.get(0).getIdDay());
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
            }else if(mlist.size()>1){
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
            }

        }
        
        // Set Alarm Manager
        Intent my_intent = new Intent(activity.getAppContext(),AlarmReceiver.class);
        my_intent.putExtra("name",dayAlarmSelect.getNameAlarm());
        my_intent.putExtra("ring",dayAlarmSelect.getRingAlarm());
        my_intent.putExtra("vibrate",dayAlarmSelect.getVibrate());
        my_intent.putExtra("extra", "on");

        calendar.set(Calendar.HOUR_OF_DAY,dayAlarmSelect.getHour());
        calendar.set(Calendar.MINUTE,dayAlarmSelect.getMinute());

        Log.d(TAG,"HOUR :"+calendar.get(Calendar.HOUR_OF_DAY));
        Log.d(TAG,"MINUTE :"+calendar.get(Calendar.MINUTE));

        PendingIntent pending_intent = PendingIntent.getBroadcast(AddAlarm.getAppContext(), 1,
                my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);

        alarm_manager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pending_intent);
    }*/
}
