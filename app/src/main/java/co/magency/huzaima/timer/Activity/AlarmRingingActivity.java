package co.magency.huzaima.timer.Activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;

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
