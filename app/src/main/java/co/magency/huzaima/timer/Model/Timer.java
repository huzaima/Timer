package co.magency.huzaima.timer.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Huzaima Khan on 7/17/2016.
 */
public class Timer extends RealmObject {

    @PrimaryKey
    @Required
    private String name;
    private int hour;
    private int minute;
    private int second;
    private int noOfLapse;
    private String alertType;
    private String alertFrequency;
    private Timer_WifiState wifiState;
    private Timer_Call call;
    private Timer_Message message;

    public Timer() {

    }

    public Timer(String name, int hour, int minute, int second, int noOfLapse) {
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.noOfLapse = noOfLapse;
        wifiState = null;
        call = null;
        message = null;
    }

    public Timer(String name, int hour, int minute, int second, int noOfLapse, String alertType,
                 String alertFrequency, Timer_WifiState wifiState, Timer_Call call, Timer_Message message) {
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.noOfLapse = noOfLapse;
        this.alertType = alertType;
        this.alertFrequency = alertFrequency;
        this.wifiState = wifiState;
        this.call = call;
        this.message = message;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertFrequency() {
        return alertFrequency;
    }

    public void setAlertFrequency(String alertFrequency) {
        this.alertFrequency = alertFrequency;
    }

    public Timer_WifiState getWifiState() {
        return wifiState;
    }

    public void setWifiState(Timer_WifiState wifiState) {
        this.wifiState = wifiState;
    }

    public Timer_Call getCall() {
        return call;
    }

    public void setCall(Timer_Call call) {
        this.call = call;
    }

    public Timer_Message getMessage() {
        return message;
    }

    public void setMessage(Timer_Message message) {
        this.message = message;
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

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfLapse() {
        return noOfLapse;
    }

    public void setNoOfLapse(int noOfLapse) {
        this.noOfLapse = noOfLapse;
    }
}
