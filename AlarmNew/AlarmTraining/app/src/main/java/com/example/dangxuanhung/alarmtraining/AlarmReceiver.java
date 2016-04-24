package com.example.dangxuanhung.alarmtraining;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Dang Xuan Hung on 22/02/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = "Receiver";
    @Override
    public void onReceive(final Context context, Intent intent) {

        String state = intent.getExtras().getString("extra");
        String ring_alarm = intent.getExtras().getString("ring");
        String name_alarm = intent.getExtras().getString("name");
        boolean vibrate = intent.getExtras().getBoolean("vibrate");

        Intent service_intent = new Intent(context,RingtonePlayingService.class);
        Calendar c = Calendar.getInstance();

        Log.e(TAG, name_alarm);
        Log.e(TAG, ring_alarm);

        if(state.equals("on")){
            Intent ring_intent = new Intent(context,StopAlarm.class);
            ring_intent.putExtra("name",name_alarm);
            ring_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ring_intent);

            service_intent.putExtra("extra",state);
            service_intent.putExtra("ring_alarm", ring_alarm);

            context.startService(service_intent);
        }
        else {
            //   Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            //   vibrator.vibrate(2000);
            service_intent.putExtra("extra",state);
            service_intent.putExtra("ring_alarm",ring_alarm);
            context.startService(service_intent);

        }
    }
}
