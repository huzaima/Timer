package co.magency.huzaima.timer.Activity;

import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    @BindView(R.id.stop)
    public Button stop;
    private Handler handler;
    private Ringtone r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);

        ButterKnife.bind(this);
        handler = new Handler();

        name.setText(getIntent().getStringExtra(AppUtility.TIMER_NAME));

        stop.setOnClickListener(this);

        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);
        r.setStreamType(AudioManager.STREAM_ALARM);
        r.play();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (ringing.getVisibility() == View.VISIBLE)
                    ringing.setVisibility(View.INVISIBLE);
                else
                    ringing.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        r.stop();
        finish();
    }
}
