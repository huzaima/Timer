package co.magency.huzaima.timer.Model;

import io.realm.RealmObject;

/**
 * Created by Huzaima Khan on 7/18/2016.
 */
public class Timer_Call extends RealmObject {

    private String number;

    public Timer_Call() {
    }

    public Timer_Call(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
