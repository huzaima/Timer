package co.magency.huzaima.timer.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
        setUpSwipeToDelete();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    addTimer.hide();
                } else if (dy < 0) {
                    addTimer.show();
                }
            }
        });
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
            case R.id.deleteall:
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Timer.class).findAll().deleteAllFromRealm();
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.no_timers_added).setVisibility(View.VISIBLE);
                        AppUtility.showToast("All timers deleted");
                    }
                });
                break;
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

    private void setUpSwipeToDelete() {

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            Drawable background;
            Drawable xMark;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = getApplicationContext().getResources().getDrawable(R.drawable.ic_delete_white_48px);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                TimerRecyclerViewAdapter adapter = (TimerRecyclerViewAdapter) recyclerView.getAdapter();

                realm.beginTransaction();
                adapter.getData().deleteFromRealm(swipedPosition);
                realm.commitTransaction();

                if (adapter.getData().size() == 0)
                    findViewById(R.id.no_timers_added).setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                View itemView = viewHolder.itemView;

                if (viewHolder.getAdapterPosition() == -1) {
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int margin = (int) getResources().getDimension(R.dimen.swipe_to_delete_drawable);

                int xMarkLeft = itemView.getRight() - margin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - margin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
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
