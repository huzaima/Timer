package co.magency.huzaima.timer.Model;

import io.realm.RealmObject;

/**
 * Created by Huzaima Khan on 7/18/2016.
 */
public class Timer_Message extends RealmObject {

    private String to;
    private String message;

    public Timer_Message() {
    }

    public Timer_Message(String to, String message) {
        this.to = to;
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
