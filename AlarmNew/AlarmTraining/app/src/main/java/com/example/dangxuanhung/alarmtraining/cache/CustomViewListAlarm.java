package com.example.dangxuanhung.alarmtraining.cache;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dangxuanhung.alarmtraining.R;

/**
 * Created by Dang Xuan Hung on 04/03/2016.
 */
public class CustomViewListAlarm extends LinearLayout {

    TextView tvTimeAlarm;
    TextView tvIdAlarm;
    TextView tvNameAlarm;
    TextView tvDayAlarm;
    Context context;
    Switch switchState;

    public CustomViewListAlarm(Context context){

        super(context);

        this.context = context;
        LayoutInflater li = (LayoutInflater)this.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.listview, this, true);


        tvTimeAlarm = (TextView)findViewById(R.id.tvTimeAlarm);
        tvNameAlarm = (TextView)findViewById(R.id.tvNameAlarm);
        tvDayAlarm = (TextView)findViewById(R.id.tvDayAlarm);
        switchState = (Switch)findViewById(R.id.switchState);

    }
}
