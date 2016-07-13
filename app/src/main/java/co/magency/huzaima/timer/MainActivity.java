package co.magency.huzaima.timer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.lylc.widget.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity {

    private CircularProgressBar circularProgressBar;
    long targetTime; //in milliseconds
    long currentTime; //in milliseconds
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        int hour = intent.getIntExtra(AppUtility.HOUR, 0);
        int minute = intent.getIntExtra(AppUtility.MINUTE, 0);
        int seconds = intent.getIntExtra(AppUtility.SECOND, 0);
        targetTime = hour * 60 * 60 + minute * 60 + seconds;

//        long diff = new Date(new Date().getTime() + targetTime).getTime() - new Date().getTime();
//        long diffSeconds = diff / 1000 % 60;
//        long diffMinutes = diff / (60 * 1000) % 60;
//        long diffHours = diff / (60 * 60 * 1000) % 24;

        Snackbar.make(findViewById(R.id.circularprogressbar), hour + ":" + minute + ":" + seconds + "\n" + targetTime, Snackbar.LENGTH_INDEFINITE).show();

        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularprogressbar);
        circularProgressBar.setProgress(0);
        circularProgressBar.setSubTitle("minutes remaining");
        circularProgressBar.setTitle("");

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentTime > 0) {
                    handler.postDelayed(runnable, 1000);
                } else
                    AppUtility.showToast(getApplicationContext(), "Done");
                circularProgressBar.setProgress((int) ((currentTime / (float) targetTime) * 100));
                circularProgressBar.setTitle(String.valueOf(currentTime / 1000));
                currentTime -= 1000;
            }
        };
        handler.post(runnable);
    }
}
