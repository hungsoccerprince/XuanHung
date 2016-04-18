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

    @Override
    public void onReceive(final Context context, Intent intent) {

        String state = intent.getExtras().getString("extra");
        String ring_alarm = intent.getExtras().getString("ring_alarm");
        int pCode = intent.getExtras().getInt("pCode");
        int hour = intent.getExtras().getInt("hour");
        int minute = intent.getExtras().getInt("minute");

        Intent service_intent = new Intent(context,RingtonePlayingService.class);
        Calendar c = Calendar.getInstance();

        Log.e("hour", String.valueOf(hour));
        Log.e("hour_now", String.valueOf(c.get(Calendar.HOUR_OF_DAY)));


        if(state.equals("on")){

            ArrayList<Integer> arrDay = intent.getIntegerArrayListExtra("arrDay");
            int i;
            for(i=0;i<arrDay.size();i++){

                if(c.get(Calendar.DAY_OF_WEEK)==arrDay.get(i) && c.get(Calendar.HOUR_OF_DAY)==hour && c.get(Calendar.MINUTE)==minute){

                    Log.e("DAY",String.valueOf(arrDay.get(i)));

                    Intent ring_intent = new Intent(context,StopAlarm.class);
                    ring_intent.putExtra("pCode",pCode);
                    ring_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(ring_intent);

                    service_intent.putExtra("extra",state);
                    service_intent.putExtra("ring_alarm", ring_alarm);

                    context.startService(service_intent);
                }
            }

         //   Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
         //   vibrator.vibrate(2000);
        }

        else{
            service_intent.putExtra("extra",state);
            service_intent.putExtra("ring_alarm",ring_alarm);

            context.startService(service_intent);
        }
    }
}
