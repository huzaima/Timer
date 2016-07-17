package co.magency.huzaima.timer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.fabtransitionactivity.SheetLayout;
import com.wooplr.spotlight.SpotlightView;

import co.magency.huzaima.timer.R;

public class ListTimerActivity extends AppCompatActivity implements View.OnClickListener, SheetLayout.OnFabAnimationEndListener {

    private FloatingActionButton addTimer;
    private SheetLayout sheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_timer);

        initViews();

        new SpotlightView.Builder(this)
                .enableRevalAnimation(true)
                .introAnimationDuration(400)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvText("Welcome")
                .subHeadingTvText("Create your first timer")
                .lineAnimDuration(400)
                .usageId("fab_spotlight")
                .target(addTimer)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachListeners();
        if (sheetLayout != null && sheetLayout.isFabExpanded())
            sheetLayout.contractFab();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public void initViews() {
        addTimer = (FloatingActionButton) findViewById(R.id.add_timer);
        sheetLayout = (SheetLayout) findViewById(R.id.bottom_sheet);
    }

    public void attachListeners() {
        addTimer.setOnClickListener(this);
        sheetLayout.setFab(addTimer);
        sheetLayout.setFabAnimationEndListener(this);
    }

    public void detachListeners() {
        addTimer.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        sheetLayout.expandFab();
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getApplicationContext(), CreateTimerActivity.class);
        startActivity(intent);
    }
}
