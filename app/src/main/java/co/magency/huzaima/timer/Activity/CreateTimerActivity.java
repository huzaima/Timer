package co.magency.huzaima.timer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.github.fabtransitionactivity.SheetLayout;

import co.magency.huzaima.timer.Fragment.SetNameFragment;
import co.magency.huzaima.timer.Fragment.SetNotificationFragment;
import co.magency.huzaima.timer.Fragment.SetTimerFragment;
import co.magency.huzaima.timer.Interface.OnNextButtonClickListener;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.Model.Timer_WifiState;
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
    private SetNotificationFragment setNotificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timer);

        initViews();
        timer = new Timer();

        intent = new Intent(getApplicationContext(), TimerActivity.class);

        setNameFragment = new SetNameFragment();
        setTimerFragment = new SetTimerFragment();
        setNotificationFragment = new SetNotificationFragment();

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
        sheetLayout.setFabAnimationEndListener(this);
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
            timer.setNoOfLapse(bundle.getInt(AppUtility.TIMER_LAPSE));
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.placeholder, setTimerFragment, AppUtility.SET_TIMER_FRAGMENT);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        } else if (SCREEN == AppUtility.TIME_INPUT_SCREEN) {
            timer.setHour(bundle.getInt(AppUtility.HOUR));
            timer.setMinute(bundle.getInt(AppUtility.MINUTE));
            timer.setSecond(bundle.getInt(AppUtility.SECOND));
            timer.setNoOfLapse(bundle.getInt(AppUtility.TIMER_LAPSE));

            intent.putExtra(AppUtility.HOUR, bundle.getInt(AppUtility.HOUR));
            intent.putExtra(AppUtility.MINUTE, bundle.getInt(AppUtility.MINUTE));
            intent.putExtra(AppUtility.SECOND, bundle.getInt(AppUtility.SECOND));

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.placeholder, setNotificationFragment, AppUtility.SET_NOTIFICATION_FRAGMENT);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();

        } else if (SCREEN == AppUtility.NOTIFICATION_INPUT_SCREEN) {

            boolean wifi = bundle.getBoolean(AppUtility.WIFI, false);

            if (wifi) {
                Timer_WifiState wifiState = new Timer_WifiState(bundle.getBoolean(AppUtility.WIFI_STATE));
                timer.setWifiState(wifiState);
            }

            String alertType = bundle.getString(AppUtility.NOTIFICATION_TYPE);
            timer.setAlertType(alertType);
            String alertFrequency = bundle.getString(AppUtility.NOTIFICATION_FREQUENCY);
            timer.setAlertFrequency(alertFrequency);

            Realm realm = AppUtility.realm;

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(timer);
                }
            });

            sheetLayout.expandFab();
        }
    }

    @Override
    public void onFabAnimationEnd() {
        startActivity(intent);
        finish();
    }
}
