package co.magency.huzaima.timer.Activity;

import android.animation.AnimatorInflater;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.RealmChangeListener;

public class ListTimerExpandedActivity extends AppCompatActivity implements RealmChangeListener<Timer> {

    @BindView(R.id.timer_name)
    public TextView timerName;
    @BindView(R.id.timer_lapse)
    public TextView timerLapse;
    @BindView(R.id.timer_notification)
    public TextView timerNotification;
    @BindView(R.id.timer_time)
    public TextView timerTime;
    @BindView(R.id.wifi_text)
    public TextView wifiText;
    @BindView(R.id.call_text)
    public TextView callText;
    @BindView(R.id.message_to)
    public TextView messageToText;
    @BindView(R.id.message_text)
    public TextView messageText;
    @BindView(R.id.title_toolbar)
    public Toolbar titleToolbar;
    @BindView(R.id.call_toolbar)
    public Toolbar callToolbar;
    @BindView(R.id.message_toolbar)
    public Toolbar messageToolbar;
    @BindView(R.id.wifi_toolbar)
    public Toolbar wifiToolbar;
    @BindView(R.id.wifi)
    public CardView wifi;
    @BindView(R.id.call)
    public CardView call;
    @BindView(R.id.message)
    public CardView message;
    @BindView(R.id.play_timer)
    FloatingActionButton playTimer;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_timer_expanded);

        ButterKnife.bind(this);

        AppUtility.realm.beginTransaction();
        timer = AppUtility.timer;
        timer = AppUtility.realm.where(Timer.class).equalTo(AppUtility.TIMER_COLUMN_NAME, timer.getName()).findFirst();
        timer.addChangeListener(this);
        AppUtility.realm.commitTransaction();

        if (timer != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                findViewById(R.id.title_card).setStateListAnimator(
                        AnimatorInflater.loadStateListAnimator(getApplicationContext(), R.anim.lift_on_touch)
                );
            timerName.setText(timer.getName());
            timerLapse.setText(getString(R.string.lapse_text, timer.getNoOfLapse()));

            if (timer.getAlertType().equals(AppUtility.DONT_NOTIFY))
                timerNotification.setText("Don't notify me");
            else
                timerNotification.setText(getString(R.string.notify_text, timer.getAlertType(), timer.getAlertFrequency()));

            String temp = timer.getHour() + "h : " + timer.getMinute() + "m : " + timer.getSecond() + "s";
            timerTime.setText(temp);
        }

        titleToolbar.setTitle("Timer");
        titleToolbar.setTitleTextColor(Color.WHITE);
        titleToolbar.inflateMenu(R.menu.card_menu);
        titleToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppUtility.showToast(item.getTitle() + " clicked");
                return true;
            }
        });

        if (timer.getCall() == null) {
            call.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                call.setClickable(true);
                call.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(), R.anim.lift_on_touch));
            }
            callText.setText("To: " + timer.getCall().getNumber());
            callToolbar.setTitle("Call");
            callToolbar.setTitleTextColor(Color.WHITE);
            callToolbar.inflateMenu(R.menu.card_menu);
            callToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    AppUtility.showToast(item.getTitle() + " clicked");
                    return true;
                }
            });
        }

        if (timer.getMessage() == null) {
            message.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                message.setClickable(true);
                message.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(), R.anim.lift_on_touch));
            }
            messageToText.setText("To: " + timer.getMessage().getTo());
            messageText.setText("Message: " + timer.getMessage().getMessage());
            messageToolbar.setTitle("Message");
            messageToolbar.setTitleTextColor(Color.WHITE);
            messageToolbar.inflateMenu(R.menu.card_menu);
            messageToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    AppUtility.showToast(item.getTitle() + " clicked");
                    return true;
                }
            });
        }

        if (timer.getWifiState() == null) {
            wifi.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                wifi.setClickable(true);
                wifi.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(), R.anim.lift_on_touch));
            }
            wifiToolbar.setTitle("WiFi");
            wifiToolbar.setTitleTextColor(Color.WHITE);
            wifiToolbar.inflateMenu(R.menu.card_menu);
            wifiText.setText("Turn " + (timer.getWifiState().isWifiEnabled() ? "on" : "off"));
            wifiToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                                                       @Override
                                                       public boolean onMenuItemClick(MenuItem item) {
                                                           View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_wifi_edit, null);
                                                           final AlertDialog builder = new AlertDialog.Builder(ListTimerExpandedActivity.this)
                                                                   .setTitle("WiFi")
                                                                   .setView(v)
                                                                   .setCancelable(true)
                                                                   .create();
                                                           ((Switch) v.findViewById(R.id.wifi_switch)).setChecked(timer.getWifiState().isWifiEnabled());
                                                           v.findViewById(R.id.wifi_switch).setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(final View v) {
                                                                   AppUtility.realm.beginTransaction();
                                                                   Timer temp = AppUtility.realm.where(Timer.class)
                                                                           .equalTo(AppUtility.TIMER_COLUMN_NAME, timer.getName())
                                                                           .findFirst();
                                                                   temp.getWifiState().setWifiEnabled(((Switch) v).isChecked());
                                                                   AppUtility.realm.commitTransaction();
                                                               }
                                                           });
                                                           builder.show();
                                                           return true;
                                                       }
                                                   }

            );
        }
    }

    @Override
    public void onChange(Timer element) {

        if (wifi.getVisibility() == View.VISIBLE)
            wifiText.setText("Turn " + (timer.getWifiState().isWifiEnabled() ? "on" : "off"));

        if (message.getVisibility() == View.VISIBLE) {
            messageToText.setText("To: " + timer.getMessage().getTo());
            messageText.setText("Message: " + timer.getMessage().getMessage());
        }

        if (call.getVisibility() == View.VISIBLE)
            callText.setText("To: " + timer.getCall().getNumber());
    }

    @Override
    protected void onDestroy() {
        timer.removeChangeListeners();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
