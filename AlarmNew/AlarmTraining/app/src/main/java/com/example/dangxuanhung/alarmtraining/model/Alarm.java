package com.example.dangxuanhung.alarmtraining.model;

/**
 * Created by Dang Xuan Hung on 07/03/2016.
 */
public class Alarm {
    private int idAlarm;
    private String nameAlarm;
    private int hourAlarm;
    private int minuteAlarm;
    private String ringAlarm;
    private String arrDay;
    private String state;
    private String vibrate;
    private String mode;
    private String type;

    public Alarm(){
        super();
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

    public int getHourAlarm() {
        return hourAlarm;
    }

    public void setHourAlarm(int hourAlarm) {
        this.hourAlarm = hourAlarm;
    }

    public int getMinuteAlarm() {
        return minuteAlarm;
    }

    public void setMinuteAlarm(int minuteAlarm) {
        this.minuteAlarm = minuteAlarm;
    }
    public String getRingAlarm() {
        return ringAlarm;
    }

    public void setRingAlarm(String ringAlarm) {
        this.ringAlarm = ringAlarm;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }

    public String getVibrate() {
        return vibrate;
    }

    public String getArrDay() {
        return arrDay;
    }

    public void setArrDay(String arrDay) {
        this.arrDay = arrDay;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
