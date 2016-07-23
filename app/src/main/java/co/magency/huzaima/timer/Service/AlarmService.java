package co.magency.huzaima.timer.Service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;

import co.magency.huzaima.timer.Activity.AlarmRingingActivity;
import co.magency.huzaima.timer.BroadcastReceiver.AlarmReceiver;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.Realm;

public class AlarmService extends IntentService {

    public AlarmService() {
        super("AlarmService");
    }

    final Handler handler = new Handler();
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent tempIntent;

    @Override
    protected void onHandleIntent(Intent intent) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Timer timer = realm.where(Timer.class)
                .equalTo(AppUtility.TIMER_COLUMN_NAME, intent.getStringExtra(AppUtility.TIMER_NAME))
                .findFirst();
        realm.commitTransaction();

        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        if (timer.getAlertType().equals(AppUtility.NOTIFICATION_ONLY)) {

            tempIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            tempIntent.putExtra(AppUtility.ALARM_TIME, timer.getDuration());
            tempIntent.putExtra(AppUtility.TIMER_LAPSE, timer.getNoOfLapse());
            tempIntent.putExtra(AppUtility.TIMER_NAME, timer.getName());

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    3523341,
                    tempIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

        } else {
            tempIntent = new Intent(getApplicationContext(), AlarmRingingActivity.class);
            tempIntent.putExtra(AppUtility.TIMER_NAME, timer.getName());

            pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    3523341,
                    tempIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
        }

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + timer.getDuration() * 1000,
                timer.getDuration() * 1000,
                pendingIntent);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alarmManager.cancel(pendingIntent);
                stopSelf();
            }
        }, timer.getNoOfLapse() * timer.getDuration() * 1000);

    }
}
