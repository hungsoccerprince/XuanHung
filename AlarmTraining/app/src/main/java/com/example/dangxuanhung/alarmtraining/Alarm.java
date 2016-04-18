package com.example.dangxuanhung.alarmtraining;

/**
 * Created by Dang Xuan Hung on 07/03/2016.
 */
public class Alarm {
    private int pCode;
    private int idAlarm;
    private String nameAlarm;
    private String timeAlarm;
    private String ringAlarm;
    private String dayAlarm;
    private Boolean vibrate;

    public Alarm(){
        super();
    }

    public int getpCode() {
        return pCode;
    }

    public void setpCode(int pCode) {
        this.pCode = pCode;
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

    public String getRingAlarm() {
        return ringAlarm;
    }

    public void setRingAlarm(String ringAlarm) {
        this.ringAlarm = ringAlarm;
    }

    public String getTimeAlarm() {
        return timeAlarm;
    }

    public void setTimeAlarm(String timeAlarm) {
        this.timeAlarm = timeAlarm;
    }

    public String getDayAlarm() {
        return dayAlarm;
    }

    public void setDayAlarm(String dayAlarm) {
        this.dayAlarm = dayAlarm;
    }

    public void setVibrate(Boolean vibrate) {
        this.vibrate = vibrate;
    }

    public Boolean getVibrate() {
        return vibrate;
    }
}
