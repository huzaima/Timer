package co.magency.huzaima.timer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Date;

import picker.ugurtekbas.com.Picker.Picker;
import picker.ugurtekbas.com.Picker.TimeChangedListener;

public class MainActivity extends AppCompatActivity implements TimeChangedListener,
        View.OnClickListener {

    Button button;
    Picker picker;
    long ms;
    Handler handler;
    Runnable runnable;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        picker = (Picker) findViewById(R.id.picktime);
        button = (Button) findViewById(R.id.start);
//        date = new Date();
//
//        picker.setTime(date.getHours(),date.getMinutes());
//        picker.initTime(0, 0);
//
//        picker.setTimeChangedListener(this);
        button.setOnClickListener(this);
//
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (date.getTime() != ms) {
//                    date.setTime(date.getTime() - 1);
//                    handler.postDelayed(runnable, 1000);
//                } else {
//                    Toast.makeText(getApplicationContext(),"Alarmmmm!!!!",
//                            Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        };
    }

    @Override
    public void onClick(View v) {
        //picker.disableTouch(true);
        //button.setVisibility(View.GONE);
//        Toast.makeText(getApplicationContext(),
//                DateUtils.formatElapsedTime(ms - date.getTime()),Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), SetTimerActivity.class));
    }

    @Override
    public void timeChanged(Date date) {
        ms = date.getTime();
    }
}
