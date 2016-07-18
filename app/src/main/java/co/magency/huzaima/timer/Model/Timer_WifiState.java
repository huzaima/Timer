package co.magency.huzaima.timer.Model;

import io.realm.RealmObject;

/**
 * Created by Huzaima Khan on 7/18/2016.
 */
public class Timer_WifiState extends RealmObject {

    private boolean isWifiEnabled;

    public Timer_WifiState() {
    }

    public Timer_WifiState(boolean isWifiEnabled) {
        this.isWifiEnabled = isWifiEnabled;
    }

    public boolean isWifiEnabled() {
        return isWifiEnabled;
    }

    public void setWifiEnabled(boolean wifiEnabled) {
        isWifiEnabled = wifiEnabled;
    }
}
