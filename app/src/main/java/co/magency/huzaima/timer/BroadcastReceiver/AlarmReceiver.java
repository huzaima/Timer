package co.magency.huzaima.timer.BroadcastReceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("Timer: " + intent.getStringExtra(AppUtility.TIMER_NAME))
                .setContentText("Lapse " + intent.getIntExtra(AppUtility.TIMER_LAPSE, 0) + " completed")
                .setSmallIcon(R.drawable.timerclock2)
                .setSound(uri);

        manager.cancel(11111111);
        manager.notify(11111111, builder.build());

    }
}
