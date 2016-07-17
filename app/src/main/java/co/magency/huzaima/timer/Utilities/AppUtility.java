package co.magency.huzaima.timer.Utilities;

import android.content.Context;
import android.widget.Toast;

import io.realm.Realm;

/**
 * Created by Huzaima Khan on 7/13/2016.
 */
public class AppUtility {

    public static final String HOUR = "HOUR";
    public static final String MINUTE = "MINUTE";
    public static final String SECOND = "SECOND";
    public static final String TIMER_NAME = "TIMER_NAME";
    public static final String INPUT_SCREEN = "INPUT_SCREEN";
    public static final int NAME_INPUT_SCREEN = 1;
    public static final int TIME_INPUT_SCREEN = 2;
    public static final String PREVIOUS_SCREEN = "PREVIOUS_SCREEN";
    public static final String SET_NAME_FRAGMENT = "SET_NAME_FRAGMENT";
    public static final String SET_TIMER_FRAGMENT = "SET_TIMER_FRAGMENT";

    public static final String SHARED_PREFERENCE_NAME = "co.magency.huzaima.timer-welcome";
    public static final String IS_LAUNCED_FIRST_TIME = "co.magency.huzaima.timer-first_time";

    public static Realm realm;

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}