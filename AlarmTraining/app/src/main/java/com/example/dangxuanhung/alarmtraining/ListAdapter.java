package com.example.dangxuanhung.alarmtraining;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dang Xuan Hung on 04/03/2016.
 */
public class ListAdapter extends ArrayAdapter<Alarm> {

    ArrayList<Alarm> arrAlarm;
    Context context;
    Alarm alarm;
    int resource;

    TextView tvNameAlarm;
    TextView tvIdAlarm;
    TextView tvTimeAlarm;
    TextView tvDayAlarm;

    public ListAdapter(Context context, int textViewResourceId,ArrayList<Alarm> array) {
        super(context, textViewResourceId,array);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.resource = textViewResourceId;
        this.arrAlarm = array;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View alarmView = convertView;

        if (alarmView == null) {
            alarmView = new CustomViewListAlarm(getContext());
        }
        alarm = arrAlarm.get(position);
        if (alarm != null) {
            tvTimeAlarm = ((CustomViewListAlarm)alarmView).tvTimeAlarm;
            tvNameAlarm = ((CustomViewListAlarm) alarmView).tvNameAlarm;
            tvIdAlarm = ((CustomViewListAlarm) alarmView).tvIdAlarm;
            tvDayAlarm = ((CustomViewListAlarm) alarmView).tvDayAlarm;

            // lay doi tuong alarm va dua ra UI
            tvIdAlarm.setText(String.valueOf(alarm.getIdAlarm()));
            tvTimeAlarm.setText(alarm.getTimeAlarm());
            tvNameAlarm.setText(alarm.getNameAlarm());

            String dayAlarm = alarm.getDayAlarm();
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

    public String getHourFormat(Date d) {
        SimpleDateFormat dft = new SimpleDateFormat("hh:mm a",
                Locale.getDefault());
        return dft.format(d);
    }
}
