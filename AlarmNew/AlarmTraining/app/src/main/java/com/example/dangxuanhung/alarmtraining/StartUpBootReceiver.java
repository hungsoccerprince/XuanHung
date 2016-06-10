package com.example.dangxuanhung.alarmtraining;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by hungdx on 03/05/2016.
 */
public class StartUpBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("startuptest", "StartUpBootReceiver BOOT_COMPLETED");

            Intent i_setAlarm = new Intent(context,SetAlarmService.class);
            i_setAlarm.putExtra("next",1);
            context.startService(i_setAlarm);
        }

        if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction())) {
            Log.d("startuptest", "StartUpBootReceiver BOOT_COMPLETED");

            Intent i_setAlarm = new Intent(context,SetAlarmService.class);
            i_setAlarm.putExtra("next",0);
            context.startService(i_setAlarm);
        }

        if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction())) {
            Log.d("startuptest", "StartUpBootReceiver BOOT_COMPLETED");

            Intent i_setAlarm = new Intent(context,SetAlarmService.class);
            i_setAlarm.putExtra("next",0);
            context.startService(i_setAlarm);
        }

        if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
            Log.d("startuptest", "StartUpBootReceiver BOOT_COMPLETED");

            Intent i_setAlarm = new Intent(context,SetAlarmService.class);
            i_setAlarm.putExtra("next",0);
            context.startService(i_setAlarm);
        }

    }

}
