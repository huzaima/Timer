package co.magency.huzaima.timer.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.fabtransitionactivity.SheetLayout;
import com.wooplr.spotlight.SpotlightView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.magency.huzaima.timer.Adapter.TimerRecyclerViewAdapter;
import co.magency.huzaima.timer.Interface.OnItemClickListener;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;
import co.magency.huzaima.timer.Utilities.PreferenceManager;
import io.realm.Realm;
import io.realm.Sort;

public class ListTimerActivity extends AppCompatActivity implements View.OnClickListener,
        SheetLayout.OnFabAnimationEndListener,
        OnItemClickListener {

    @BindView(R.id.add_timer)
    public FloatingActionButton addTimer;
    @BindView(R.id.timer_list)
    public RecyclerView recyclerView;
    @BindView(R.id.sheet_wrapper)
    public SheetLayout sheetLayout;
    private TimerRecyclerViewAdapter timerRecyclerViewAdapter;
    private Realm realm;
    public static Activity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_timer);

        instance = this;

        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        PreferenceManager pref = new PreferenceManager(getApplicationContext());

        if (!pref.isShowcaseViewShown()) {

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

            pref.setIsShowcaseViewShown(true);

        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        timerRecyclerViewAdapter = new TimerRecyclerViewAdapter(getApplicationContext(),
                realm.where(Timer.class).findAll());
        if (timerRecyclerViewAdapter.getData().size() > 0) {
            findViewById(R.id.no_timers_added).setVisibility(View.GONE);
        }

        recyclerView.setAdapter(timerRecyclerViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = this;
        attachListeners();
        if (sheetLayout != null && sheetLayout.isFabExpanded())
            sheetLayout.contractFab();

        if (timerRecyclerViewAdapter != null && timerRecyclerViewAdapter.getData().size() > 0) {
            findViewById(R.id.no_timers_added).setVisibility(View.GONE);
        } else {
            findViewById(R.id.no_timers_added).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        instance = null;
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sort:
                new AlertDialog.Builder(ListTimerActivity.this)
                        .setTitle("Choose sort order")
                        .setCancelable(true)
                        .setItems(R.array.sort_order, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                    case 1:
                                        timerRecyclerViewAdapter.updateData(timerRecyclerViewAdapter
                                                .getData().sort(AppUtility.TIMER_COLUMN_CREATED_AT,
                                                        which == 0 ? Sort.ASCENDING : Sort.DESCENDING));
                                        break;
                                    case 2:
                                    case 3:
                                        timerRecyclerViewAdapter.updateData(timerRecyclerViewAdapter
                                                .getData().sort(AppUtility.TIMER_COLUMN_NAME,
                                                        which == 2 ? Sort.ASCENDING : Sort.DESCENDING));
                                        break;
                                    case 4:
                                    case 5:
                                        timerRecyclerViewAdapter.updateData(timerRecyclerViewAdapter
                                                .getData().sort(AppUtility.TIMER_COLUMN_DURATION,
                                                        which == 4 ? Sort.ASCENDING : Sort.DESCENDING));
                                        break;
                                }
                            }
                        }).create().show();
        }
        return true;
    }

    public void attachListeners() {
        addTimer.setOnClickListener(this);
        sheetLayout.setFab(addTimer);
        sheetLayout.setFabAnimationEndListener(this);
        timerRecyclerViewAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(Timer timer) {
        Intent intent = new Intent(getApplicationContext(), ListTimerExpandedActivity.class);
        AppUtility.timer = timer;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    findViewById(R.id.id_tansition), getString(R.string.timer_name_card));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
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
