package co.magency.huzaima.timer.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import co.magency.huzaima.timer.Activity.AlarmRingingActivity;
import co.magency.huzaima.timer.BroadcastReceiver.AlarmReceiver;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class AlarmService extends Service implements RealmChangeListener<Timer> {

    private HandlerThread handlerThread;
    private Handler handler;
    private Map<String, Integer> currentLap = new HashMap<>();
    private Map<String, Timer> timer = new HashMap<>();
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate() {
        super.onCreate();

        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wakeLock");

        wakeLock.acquire();

        handlerThread = new HandlerThread("alarmservice" + System.currentTimeMillis(), Thread.MAX_PRIORITY);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timer.isEmpty() && currentLap.isEmpty()) {
                    stopSelf();
                }
            }
        }, 60 * 1000);
    }

    @Override
    public int onStartCommand(final Intent i, int flags, int startId) {

        handler.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {

                if (i != null) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    final Timer t = realm.where(Timer.class)
                            .equalTo(AppUtility.TIMER_COLUMN_NAME, i.getStringExtra(AppUtility.TIMER_NAME))
                            .findFirst();
                    t.addChangeListener(AlarmService.this);
                    realm.commitTransaction();
                    currentLap.put(i.getStringExtra(AppUtility.TIMER_NAME), 0);
                    timer.put(t.getName(), t);
                    startTimer(i.getStringExtra(AppUtility.TIMER_NAME));
                }
            }
        });
        return START_STICKY;
    }

    private void startTimer(final String name) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                final Intent tempIntent;

                final boolean broadcast;
                if (timer.get(name).getAlertType().equals(AppUtility.NOTIFICATION_ONLY)) {
                    tempIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    broadcast = true;
                } else {
                    tempIntent = new Intent(getApplicationContext(), AlarmRingingActivity.class);
                    broadcast = false;
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentLap.get(name) != null && timer.get(name) != null && timer.get(name).isValid()) {
                            if (currentLap.get(name) < timer.get(name).getNoOfLapse()) {
                                tempIntent.putExtra(AppUtility.TIMER_NAME, name);
                                currentLap.put(name, currentLap.get(name) + 1);
                                tempIntent.putExtra(AppUtility.TIMER_LAPSE, currentLap.get(name));
                                tempIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (!broadcast)
                                    startActivity(tempIntent);
                                else
                                    sendBroadcast(tempIntent);
                                handler.postDelayed(this, timer.get(name).getDuration() * 1000);
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        timer.remove(name);
                                        currentLap.remove(name);
                                    }
                                });
                            }
                        }
                    }
                }, timer.get(name).getDuration() * 1000);
            }
        });
    }

    @Override
    public void onChange(Timer element) {
        AppUtility.showToast("onCHanged called in AlarmService");
        if (!element.isValid()) {
            timer.remove(element.getName());
            currentLap.remove(element.getName());
            AppUtility.showToast("element not valid");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handlerThread.quit();
        wakeLock.release();
        super.onDestroy();
    }
}
