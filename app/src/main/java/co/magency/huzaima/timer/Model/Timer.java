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

    public Timer() {

    }

    public Timer(String name, int hour, int minute, int second, int noOfLapse) {
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.noOfLapse = noOfLapse;
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
