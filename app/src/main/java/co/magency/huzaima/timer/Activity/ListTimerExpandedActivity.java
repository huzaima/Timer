package co.magency.huzaima.timer.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.magency.huzaima.timer.R;

public class ListTimerExpandedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_timer_expanded);


    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
