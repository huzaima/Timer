package co.magency.huzaima.timer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.magency.huzaima.timer.Fragment.SetNameFragment;
import co.magency.huzaima.timer.Fragment.SetNotificationFragment;
import co.magency.huzaima.timer.Fragment.SetTimerFragment;
import co.magency.huzaima.timer.Interface.OnNextButtonClickListener;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.Model.Timer_Call;
import co.magency.huzaima.timer.Model.Timer_Message;
import co.magency.huzaima.timer.Model.Timer_WifiState;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.Realm;

public class CreateTimerActivity extends AppCompatActivity implements OnNextButtonClickListener {

    @BindView(R.id.next)
    public FloatingActionButton next;
    private Timer timer;
    private SetNameFragment setNameFragment;
    private SetTimerFragment setTimerFragment;
    private SetNotificationFragment setNotificationFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timer);

        ButterKnife.bind(this);

        setNameFragment = new SetNameFragment();
        setTimerFragment = new SetTimerFragment();
        setNotificationFragment = new SetNotificationFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, setNameFragment, AppUtility.SET_NAME_FRAGMENT);
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void onNextButtonClicked(Bundle bundle) {

        final int SCREEN = bundle.getInt(AppUtility.INPUT_SCREEN);
        if (SCREEN == AppUtility.NAME_INPUT_SCREEN) {
            timer = new Timer();
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

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.placeholder, setNotificationFragment, AppUtility.SET_NOTIFICATION_FRAGMENT);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();

        } else if (SCREEN == AppUtility.NOTIFICATION_INPUT_SCREEN) {

            if (bundle.containsKey(AppUtility.WIFI)) {
                Timer_WifiState wifiState = new Timer_WifiState(bundle.getBoolean(AppUtility.WIFI_STATE));
                timer.setWifiState(wifiState);
            }

            if (bundle.containsKey(AppUtility.CALL_TO)) {
                Timer_Call call = new Timer_Call(bundle.getString(AppUtility.CALL_TO));
                timer.setCall(call);
            }

            if (bundle.containsKey(AppUtility.MESSAGE_TO) &&
                    bundle.containsKey(AppUtility.MESSAGE_TEXT)) {
                Timer_Message message = new Timer_Message(
                        bundle.getString(AppUtility.MESSAGE_TO),
                        bundle.getString(AppUtility.MESSAGE_TEXT));
                timer.setMessage(message);
            }

            String alertType = bundle.getString(AppUtility.NOTIFICATION_TYPE);
            timer.setAlertType(alertType);
            String alertFrequency = bundle.getString(AppUtility.NOTIFICATION_FREQUENCY);
            timer.setAlertFrequency(alertFrequency);

            Realm realm = Realm.getDefaultInstance();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(timer);
                    AppUtility.timer = timer;
                }
            });

            realm.close();

            Intent intent = new Intent(getApplicationContext(), ListTimerExpandedActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
