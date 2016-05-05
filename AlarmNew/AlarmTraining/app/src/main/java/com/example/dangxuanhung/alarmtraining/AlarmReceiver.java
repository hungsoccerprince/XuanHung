package com.example.dangxuanhung.alarmtraining;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Dang Xuan Hung on 22/02/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = AlarmReceiver.class.getSimpleName();
    @Override
    public void onReceive(final Context context, Intent intent) {

        String state = intent.getExtras().getString("extra");
        String ring_alarm = intent.getExtras().getString("ring");
        String name_alarm = intent.getExtras().getString("name");
        String  vibrate = intent.getExtras().getString("vibrate");
        String mode = intent.getExtras().getString("mode");
        String type =  intent.getExtras().getString("type");
        int day = intent.getExtras().getInt("day");
        int hour = intent.getExtras().getInt("hour");
        int minute = intent.getExtras().getInt("minute");

        Log.d(TAG, "type : "+type);

        Intent service_intent = new Intent(context,RingtonePlayingService.class);
        Calendar c = Calendar.getInstance();


        if(day==c.get(Calendar.DAY_OF_WEEK) && hour==c.get(Calendar.HOUR_OF_DAY) && minute== c.get(Calendar.MINUTE)){
            if(state.equals("on")){
                service_intent.putExtra("extra",state);
                service_intent.putExtra("ring_alarm", ring_alarm);
                service_intent.putExtra("type",type);
                Log.d(TAG, "type : "+ type);

                context.startService(service_intent);

                Intent ring_intent = new Intent(context,StopAlarm.class);
                ring_intent.putExtra("name",name_alarm);
                ring_intent.putExtra("mode",mode);
                ring_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ring_intent);

                if(vibrate.equals("yes")){
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(20000);
                }
            }
            else {
                service_intent.putExtra("extra",state);
                service_intent.putExtra("ring_alarm",ring_alarm);
                context.startService(service_intent);
            }
        }

    }
}
