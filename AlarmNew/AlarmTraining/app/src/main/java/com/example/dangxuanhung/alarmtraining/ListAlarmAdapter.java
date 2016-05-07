package com.example.dangxuanhung.alarmtraining;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dangxuanhung.alarmtraining.model.Alarm;

import java.util.ArrayList;

/**
 * Created by hungdx on 03/05/2016.
 */
public class ListAlarmAdapter extends RecyclerView.Adapter<ListAlarmAdapter.ViewHolder> {

    private static final String TAG = ListAlarmAdapter.class.getSimpleName();
    private ArrayList<Alarm> arrAlarm;
    private Context context;
    private View.OnClickListener mListener;
    private DatabaseHelper dbHelper;

    public ListAlarmAdapter(Context context,ArrayList<Alarm> array, View.OnClickListener listener){
        this.arrAlarm = array;
        this.context = context;
        this.mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeAlarm;
        TextView tvIdAlarm;
        TextView tvNameAlarm;
        TextView tvDayAlarm;
        Context context;
        Switch switchState;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTimeAlarm = (TextView)itemView.findViewById(R.id.tvTimeAlarm);
            tvIdAlarm = (TextView)itemView.findViewById(R.id.tvIdAlarm);
            tvNameAlarm = (TextView)itemView.findViewById(R.id.tvNameAlarm);
            tvDayAlarm = (TextView)itemView.findViewById(R.id.tvDayAlarm);
            switchState = (Switch)itemView.findViewById(R.id.switchState);

        }
    }

    @Override
    public ListAlarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View view = inflater.inflate(R.layout.listview, parent, false);

        dbHelper = new DatabaseHelper(context);
        view.setOnClickListener(mListener);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAlarmAdapter.ViewHolder viewHolder, int position) {
        final Alarm alarm = arrAlarm.get(position);
        String hour = alarm.getHourAlarm()<10?"0"+String.valueOf(alarm.getHourAlarm()):String.valueOf(alarm.getHourAlarm());
        String minute = alarm.getMinuteAlarm()<10?"0"+String.valueOf(alarm.getMinuteAlarm()):String.valueOf(alarm.getMinuteAlarm());

        viewHolder.tvIdAlarm.setText(String.valueOf(alarm.getIdAlarm()));
     //   viewHolder.tvTimeAlarm.setText(String.valueOf(alarm.getHourAlarm())+ ":" + String.valueOf(alarm.getMinuteAlarm()));
        viewHolder.tvTimeAlarm.setText(hour + ":" + minute);
        viewHolder.tvNameAlarm.setText(alarm.getNameAlarm());

        Switch switchState = viewHolder.switchState;

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
                        dbHelper.update(v_update_alarm,"_id_alarm="+alarm.getIdAlarm(),"alarm_table");
                        dbHelper.update(v_update_alarm,"id_alarm="+alarm.getIdAlarm(),"day_table");
                        Log.d(TAG,"id "+ alarm.getIdAlarm());
                    }
                    catch (Exception e){
                        Log.e(TAG, String.valueOf(e));
                    }

                    Intent i_service = new Intent(context,SetAlarmService.class);
                    context.startService(i_service);
                    Toast.makeText(context,"On",Toast.LENGTH_SHORT).show();
                }
                if(!isChecked) {
                    ContentValues v_update_alarm = new ContentValues();
                    v_update_alarm.put("state","off");
                    try {
                        dbHelper.update(v_update_alarm,"_id_alarm="+alarm.getIdAlarm(),"alarm_table");
                        dbHelper.update(v_update_alarm,"id_alarm="+alarm.getIdAlarm(),"day_table");
                        Log.d(TAG,"id "+ alarm.getIdAlarm());
                    }
                    catch (Exception e){
                        Log.e(TAG, String.valueOf(e));
                    }

                    Intent i_service = new Intent(context,SetAlarmService.class);
                    context.startService(i_service);
                    Toast.makeText(context,"Off",Toast.LENGTH_SHORT).show();
                }
            }
        });


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

        viewHolder.tvDayAlarm.setText(dayView);

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return arrAlarm.size();
    }

}
