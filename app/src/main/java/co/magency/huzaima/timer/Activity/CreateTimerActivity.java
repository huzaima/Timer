package co.magency.huzaima.timer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.github.fabtransitionactivity.SheetLayout;

import co.magency.huzaima.timer.Fragment.SetNameFragment;
import co.magency.huzaima.timer.Fragment.SetTimerFragment;
import co.magency.huzaima.timer.Interface.OnNextButtonClickListener;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.Realm;

public class CreateTimerActivity extends AppCompatActivity implements OnNextButtonClickListener, SheetLayout.OnFabAnimationEndListener {

    public static FloatingActionButton next;
    private Timer timer;
    private SheetLayout sheetLayout;
    private Intent intent;
    private SetNameFragment setNameFragment;
    private SetTimerFragment setTimerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timer);

        initViews();
        timer = new Timer();

        setNameFragment = new SetNameFragment();
        setTimerFragment = new SetTimerFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, setNameFragment, AppUtility.SET_NAME_FRAGMENT);
        fragmentTransaction.commit();

    }

    private void initViews() {

        // FAB
        next = (FloatingActionButton) findViewById(R.id.next);

        // Sheet animation for FAB
        sheetLayout = (SheetLayout) findViewById(R.id.bottom_sheet);
        sheetLayout.setFab(next);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sheetLayout != null && sheetLayout.isFabExpanded())
            sheetLayout.contractFab();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void buttonClicked(Bundle bundle) {

        final int SCREEN = bundle.getInt(AppUtility.INPUT_SCREEN);
        if (SCREEN == AppUtility.NAME_INPUT_SCREEN) {
            timer.setName(bundle.getString(AppUtility.TIMER_NAME));
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.placeholder, setTimerFragment, AppUtility.SET_TIMER_FRAGMENT).addToBackStack(AppUtility.SET_TIMER_FRAGMENT);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        } else if (SCREEN == AppUtility.TIME_INPUT_SCREEN) {
            timer.setHour(bundle.getInt(AppUtility.HOUR));
            timer.setMinute(bundle.getInt(AppUtility.MINUTE));
            timer.setSecond(bundle.getInt(AppUtility.SECOND));
            timer.setNoOfLapse(0);

            Realm realm = AppUtility.realm;

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(timer);
                }
            });

            sheetLayout.setFabAnimationEndListener(this);

            intent = new Intent(getApplicationContext(), TimerActivity.class);
            intent.putExtra(AppUtility.HOUR, bundle.getInt(AppUtility.HOUR));
            intent.putExtra(AppUtility.MINUTE, bundle.getInt(AppUtility.MINUTE));
            intent.putExtra(AppUtility.SECOND, bundle.getInt(AppUtility.SECOND));

            sheetLayout.expandFab();
        }
    }

    @Override
    public void onFabAnimationEnd() {
        startActivity(intent);
        finish();
    }
}
