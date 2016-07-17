package co.magency.huzaima.timer.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gigamole.library.ArcProgressStackView;

import java.util.ArrayList;

import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;

public class TimerActivity extends AppCompatActivity {

    private ArcProgressStackView arcProgressStackView;
    private TextView hourText, minuteText, secondText;
    int targetTime; // in seconds
    int currentTime; // in seconds
    final int DELAY = 1000; // in milliseconds
    Handler handler;
    Runnable runnable;
    private ArrayList<ArcProgressStackView.Model> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Intent intent = getIntent();
        int hour = intent.getIntExtra(AppUtility.HOUR, 0);
        int minute = intent.getIntExtra(AppUtility.MINUTE, 0);
        int seconds = intent.getIntExtra(AppUtility.SECOND, 0);
        targetTime = hour * 60 * 60 + minute * 60 + seconds;
        currentTime = targetTime;

        final String arr[] = getResources().getStringArray(R.array.default_preview);

        hourText = (TextView) findViewById(R.id.hour);
        minuteText = (TextView) findViewById(R.id.minute);
        secondText = (TextView) findViewById(R.id.second);

        arcProgressStackView = (ArcProgressStackView) findViewById(R.id.apsv);
        models = new ArrayList<>();
        models.add(new ArcProgressStackView.Model("Hour", 3, Color.GRAY, Color.parseColor(arr[0])));
        models.add(new ArcProgressStackView.Model("Minute", 3, Color.GRAY, Color.parseColor(arr[1])));
        models.add(new ArcProgressStackView.Model("Second", 3, Color.GRAY, Color.parseColor(arr[2])));
        arcProgressStackView.setModels(models);


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentTime > 0) {
                    handler.postDelayed(runnable, DELAY);
                }
                String h = (currentTime / 60) / 60 + "";
                if (h.length() == 1)
                    h = "0" + h;
                hourText.setText(h);
                String m = (currentTime / 60) % 60 + "";
                if (m.length() == 1)
                    m = "0" + m;
                minuteText.setText(m);
                String s = currentTime % 60 + "";
                if (s.length() == 1)
                    s = "0" + s;
                secondText.setText(s);

                int targetHour = (int) ((targetTime / 60.0) / 60.0);

                if (targetHour > 0)
                    models.get(0).setProgress((targetHour - Integer.parseInt(h)) / targetHour);
                else
                    models.get(0).setProgress(100);

                models.get(1).setProgress(100 * (60 - Integer.parseInt(m)) / 60f);
                models.get(2).setProgress(100 * (60 - Integer.parseInt(s)) / 60f);
                currentTime -= 1;
            }
        };
        handler.post(runnable);
    }
}
