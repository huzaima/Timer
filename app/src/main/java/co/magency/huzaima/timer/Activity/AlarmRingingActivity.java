package co.magency.huzaima.timer.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.Model.Timer_Call;
import co.magency.huzaima.timer.Model.Timer_Message;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class AlarmRingingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ringing)
    public TextView ringing;
    @BindView(R.id.name)
    public TextView name;
    @BindView(R.id.current_lapse)
    public TextView currentLap;
    @BindView(R.id.stop)
    public Button stop;
    private Handler handler;
    private Ringtone r;
    private boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        handler = new Handler();

        name.setText(getIntent().getStringExtra(AppUtility.TIMER_NAME));
        currentLap.setText(getString(R.string.current_lapse, getIntent().getIntExtra(AppUtility.TIMER_LAPSE, 0)));

        stop.setOnClickListener(this);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Timer timer = realm.where(Timer.class)
                .equalTo(AppUtility.TIMER_COLUMN_NAME, getIntent().getStringExtra(AppUtility.TIMER_NAME))
                .findFirst();
        timer.addChangeListener(new RealmChangeListener<Timer>() {
            @Override
            public void onChange(Timer element) {
                AppUtility.showToast("onCHanged called in AlarmRingingActivity");
                if (element.isValid())
                    finish();
            }
        });
        realm.commitTransaction();

        if (timer.getCall() != null)
            makeCall(timer.getCall());
        if (timer.getMessage() != null)
            sendMessage(timer.getMessage());
        if (timer.getWifiState() != null)
            turnWifi(timer.getWifiState().isWifiEnabled());

        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);
        r.setStreamType(AudioManager.STREAM_ALARM);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (check) {
                    if (ringing.getAlpha() > 0)
                        ringing.setAlpha(ringing.getAlpha() - 0.1f);
                    else
                        check = !check;
                } else {
                    if (ringing.getAlpha() < 1)
                        ringing.setAlpha(ringing.getAlpha() + 0.1f);
                    else
                        check = !check;
                }

                if (r != null && !r.isPlaying())
                    r.play();

                handler.postDelayed(this, 50);
            }
        }, 50);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                r.stop();
                r = null;
            }
        }, 60 * 1000);
    }

    @Override
    public void onClick(View v) {
        if (r != null)
            r.stop();
        handler.removeCallbacksAndMessages(null);
        finish();
    }

    private void makeCall(Timer_Call t) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + t.getNumber()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    private void sendMessage(Timer_Message m) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(m.getTo(), null, m.getMessage(), null, null);
        AppUtility.showToast("Message sent");
    }

    private void turnWifi(boolean wifiState) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(wifiState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent() != null) {
            name.setText(getIntent().getStringExtra(AppUtility.TIMER_NAME));
            currentLap.setText(getString(R.string.current_lapse, getIntent().getIntExtra(AppUtility.TIMER_LAPSE, 0)));
        }
    }
}
