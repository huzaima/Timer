package co.magency.huzaima.timer.Utilities;

import android.content.Context;
import android.widget.Toast;

import co.magency.huzaima.timer.Model.Timer;

/**
 * Created by Huzaima Khan on 7/13/2016.
 */
public class AppUtility {

    public static final String HOUR = "HOUR";
    public static final String MINUTE = "MINUTE";
    public static final String SECOND = "SECOND";
    public static final String TIMER_NAME = "TIMER_NAME";
    public static final String TIMER_LAPSE = "TIMER_LAPSE";
    public static final String INPUT_SCREEN = "INPUT_SCREEN";
    public static final int NAME_INPUT_SCREEN = 1;
    public static final int TIME_INPUT_SCREEN = 2;
    public static final int NOTIFICATION_INPUT_SCREEN = 3;
    public static final String SET_NAME_FRAGMENT = "SET_NAME_FRAGMENT";
    public static final String SET_TIMER_FRAGMENT = "SET_TIMER_FRAGMENT";
    public static final String SET_NOTIFICATION_FRAGMENT = "SET_NOTIFICATION_FRAGMENT";
    public static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    public static final String NOTIFICATION_FREQUENCY = "NOTIFICATION_FREQUENCY";
    public static final String WIFI_STATE = "WIFI_STATE";
    public static final String WIFI = "WIFI";
    public static final String AFTER_COMPLETE_TIMER = "After Complete Timer";
    public static final String AFTER_EVERY_LAPSE = "After Every Lapse";
    public static final String DONT_NOTIFY = "DONT_NOTIFY";
    public static final String NOTIFICATION_ONLY = "Notification";
    public static final String ALARM = "Alarm";
    public static final String TIMER_OBJECT = "TIMER_OBJECT";
    public static final int NEW_TIMER_CREATED = 1;
    public static final String CALL_TO = "CALL_TO";
    public static final String MESSAGE_TO = "MESSAGE_TO";
    public static final String MESSAGE_TEXT = "MESSAGE";
    public static final String ALARM_TIME = "ALARM_TIME";
    public static String CURRENT_LAP = "CURRENT_LAP";
    public static boolean IS_ONE_LAPSE;

    // SharedPreferences keys
    public static final String SHARED_PREFERENCE_NAME = "co.magency.huzaima.timer-welcome";
    public static final String IS_LAUNCED_FIRST_TIME = "co.magency.huzaima.timer-first_time";

    // Realm database column name for class Timer
    public static final String TIMER_COLUMN_NAME = "name";
    public static final String TIMER_COLUMN_CREATED_AT = "createdAt";
    public static final String TIMER_COLUMN_DURATION = "duration";

    // RecyclerView populate sort order
    public static String TIMER_LIST_SORT_BY;
    public static io.realm.Sort TIMER_LIST_SORT_ORDER;

    public static Timer timer;

    public static Context context;

    public static void showToast(String text) {
        if (AppUtility.context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }
}
