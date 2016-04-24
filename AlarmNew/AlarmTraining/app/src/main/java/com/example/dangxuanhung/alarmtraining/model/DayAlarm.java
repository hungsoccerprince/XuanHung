package com.example.dangxuanhung.alarmtraining.model;

/**
 * Created by hungdx on 22/04/2016.
 */
public class DayAlarm {
    private int idDay;
    private int idAlarm;
    private String nameAlarm;
    private int day;
    private int hour;
    private int minute;
    private String ringAlarm;
    private Boolean vibrate;

    public int getIdDay() {
        return idDay;
    }

    public void setIdDay(int idDay) {
        this.idDay = idDay;
    }

    public int getIdAlarm() {
        return idAlarm;
    }

    public void setIdAlarm(int idAlarm) {
        this.idAlarm = idAlarm;
    }

    public String getNameAlarm() {
        return nameAlarm;
    }

    public void setNameAlarm(String nameAlarm) {
        this.nameAlarm = nameAlarm;
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
    public String getRingAlarm() {
        return ringAlarm;
    }

    public void setRingAlarm(String ringAlarm) {
        this.ringAlarm = ringAlarm;
    }

    public void setVibrate(Boolean vibrate) {
        this.vibrate = vibrate;
    }

    public Boolean getVibrate() {
        return vibrate;
    }

}
