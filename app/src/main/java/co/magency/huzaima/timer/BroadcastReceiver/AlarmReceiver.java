package co.magency.huzaima.timer.BroadcastReceiver;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;

import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.Model.Timer_Call;
import co.magency.huzaima.timer.Model.Timer_Message;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.Realm;

public class AlarmReceiver extends BroadcastReceiver {

    private Context context;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context c, Intent intent) {

        context = c;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Timer timer = realm.where(Timer.class)
                .equalTo(AppUtility.TIMER_COLUMN_NAME, intent.getStringExtra(AppUtility.TIMER_NAME))
                .findFirst();
        realm.commitTransaction();

        if (timer.getCall() != null)
            makeCall(timer.getCall());
        if (timer.getMessage() != null)
            sendMessage(timer.getMessage());
        if (timer.getWifiState() != null)
            turnWifi(timer.getWifiState().isWifiEnabled());

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("Timer: " + intent.getStringExtra(AppUtility.TIMER_NAME))
                .setContentText("Lapse " + intent.getIntExtra(AppUtility.TIMER_LAPSE, 0) + " completed")
                .setSmallIcon(R.drawable.timerclock)
                .setSound(uri);

        manager.cancel(11111111);
        manager.notify(11111111, builder.build());

    }

    private void makeCall(Timer_Call t) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + t.getNumber()));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }

    private void sendMessage(Timer_Message m) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(m.getTo(), null, m.getMessage(), null, null);
        AppUtility.showToast("Message sent");
    }

    private void turnWifi(boolean wifiState) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(wifiState);
    }
}
