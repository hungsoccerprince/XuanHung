package com.example.dangxuanhung.alarmtraining.cache;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dangxuanhung.alarmtraining.DatabaseHelper;
import com.example.dangxuanhung.alarmtraining.SetAlarmService;
import com.example.dangxuanhung.alarmtraining.model.Alarm;

import java.util.ArrayList;

/**
 * Created by Dang Xuan Hung on 04/03/2016.
 */
public class ListAdapter extends ArrayAdapter<Alarm> {

    private static final String TAG = ListAdapter.class.getSimpleName() ;
    private ArrayList<Alarm> arrAlarm;
    private Context context;
    private Alarm alarm;
    private int resource;

    private TextView tvNameAlarm,tvIdAlarm,tvTimeAlarm,tvDayAlarm;
    private Switch switchState;
    private DatabaseHelper dbHelper;


    public ListAdapter(Context context, int textViewResourceId,ArrayList<Alarm> array) {
        super(context, textViewResourceId,array);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.resource = textViewResourceId;
        this.arrAlarm = array;
    }

    public View getView(int position, final View convertView, final ViewGroup parent) {
        View alarmView = convertView;

        if (alarmView == null) {
            alarmView = new CustomViewListAlarm(getContext());
        }
        dbHelper = new DatabaseHelper(getContext());
        alarm = arrAlarm.get(position);


        if (alarm != null) {

            tvTimeAlarm = ((CustomViewListAlarm)alarmView).tvTimeAlarm;
            tvNameAlarm = ((CustomViewListAlarm) alarmView).tvNameAlarm;
            tvIdAlarm = ((CustomViewListAlarm) alarmView).tvIdAlarm;
            tvDayAlarm = ((CustomViewListAlarm) alarmView).tvDayAlarm;
            switchState = ((CustomViewListAlarm)alarmView).switchState;

            switchState.setTextOff("Off");
            switchState.setTextOn("On");

            if(alarm.getState().equals("on")){
                switchState.setChecked(true);
            }

            switchState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ContentValues v_update_alarm = new ContentValues();
                        v_update_alarm.put("state","on");
                        try {
                            dbHelper.update(v_update_alarm,"_id_alarm="+tvIdAlarm.getText(),"alarm_table");
                            dbHelper.update(v_update_alarm,"id_alarm="+tvIdAlarm.getText(),"day_table");
                            Log.d(TAG,"id "+ tvIdAlarm.getText());
                        }
                        catch (Exception e){
                            Log.e(TAG, String.valueOf(e));
                        }

                        Intent i_service = new Intent(getContext(),SetAlarmService.class);
                        getContext().startService(i_service);
                        Toast.makeText(context,"On",Toast.LENGTH_SHORT).show();
                    }
                    if(!isChecked) {
                        ContentValues v_update_alarm = new ContentValues();
                        v_update_alarm.put("state","off");
                        try {
                            dbHelper.update(v_update_alarm,"_id_alarm="+tvIdAlarm.getText(),"alarm_table");
                            dbHelper.update(v_update_alarm,"id_alarm="+tvIdAlarm.getText(),"day_table");
                            Log.d(TAG,"id "+ tvIdAlarm.getText());
                        }
                        catch (Exception e){
                            Log.e(TAG, String.valueOf(e));
                        }

                        Intent i_service = new Intent(getContext(),SetAlarmService.class);
                        getContext().startService(i_service);
                        Toast.makeText(context,"Off",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // lay doi tuong alarm va dua ra UI
            tvIdAlarm.setText(String.valueOf(alarm.getIdAlarm()));
            tvTimeAlarm.setText(String.valueOf(alarm.getHourAlarm())+ ":" + String.valueOf(alarm.getMinuteAlarm()));
            tvNameAlarm.setText(alarm.getNameAlarm());

            String dayAlarm = alarm.getArrDay();
            String dayView = "";
            if(dayAlarm.contains("1")){
                dayView += "Sun ";
            }
            if(dayAlarm.contains("2")){
                dayView += "Mon ";
            }
            if(dayAlarm.contains("3")){
                dayView += "Tue ";
            }
            if(dayAlarm.contains("4")){
                dayView += "Wed ";
            }
            if(dayAlarm.contains("5")){
                dayView += "Thu ";
            }
            if(dayAlarm.contains("6")){
                dayView += "Fri ";
            }
            if(dayAlarm.contains("7")){
                dayView += "Sat ";
            }

            tvDayAlarm.setText(dayView);
        }
        return alarmView;
    }

}
