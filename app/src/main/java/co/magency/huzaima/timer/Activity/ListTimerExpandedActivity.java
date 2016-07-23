package co.magency.huzaima.timer.Activity;

import android.animation.AnimatorInflater;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.magency.huzaima.timer.BroadcastReceiver.AlarmReceiver;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Service.AlarmService;
import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class ListTimerExpandedActivity extends AppCompatActivity implements RealmChangeListener<Timer>,
        View.OnClickListener {

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
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_timer_expanded);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();

        playTimer.setOnClickListener(this);

        realm.beginTransaction();
        timer = AppUtility.timer;
        timer = realm.where(Timer.class).equalTo(AppUtility.TIMER_COLUMN_NAME, timer.getName()).findFirst();
        timer.addChangeListener(this);
        realm.commitTransaction();

        if (timer != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                findViewById(R.id.title_card).setStateListAnimator(
                        AnimatorInflater.loadStateListAnimator(getApplicationContext(), R.anim.lift_on_touch)
                );
            timerName.setText(timer.getName());
            timerLapse.setText(getString(R.string.lapse_text, timer.getNoOfLapse()));

            if (timer.getAlertType().equals(AppUtility.DONT_NOTIFY)) {
                timerNotification.setText("Don't notify me");
                playTimer.setAlpha(0.5f);
            } else
                timerNotification.setText(getString(R.string.notify_text, timer.getAlertType(), timer.getAlertFrequency()));

            String temp = timer.getHour() + "h : " + timer.getMinute() + "m : " + timer.getSecond() + "s";
            timerTime.setText(temp);
        }

        titleToolbar.setTitle("Timer");
        titleToolbar.setTitleTextColor(Color.WHITE);

        if (timer.getCall() == null) {
            call.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                call.setClickable(true);
                call.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(),
                        R.anim.lift_on_touch));
            }
            callText.setText("To: " + timer.getCall().getNumber());
            callToolbar.setTitle("Call");
            callToolbar.setTitleTextColor(Color.WHITE);
            callToolbar.inflateMenu(R.menu.card_menu);
            callToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_call, null);
                    final EditText editText = (EditText) view.findViewById(R.id.number);
                    editText.setText(timer.getCall().getNumber());
                    editText.setTextColor(Color.BLACK);
                    final AlertDialog dialog = new AlertDialog.Builder(ListTimerExpandedActivity.this)
                            .setView(view)
                            .setTitle("Call")
                            .setPositiveButton("Done", null)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editText.getText().toString().isEmpty()) {
                                editText.setError("Number cannot be empty");
                            } else {
                                String number = editText.getText().toString();
                                realm.beginTransaction();
                                timer.getCall().setNumber(number);
                                realm.commitTransaction();
                                dialog.dismiss();
                            }
                        }
                    });
                    return true;
                }
            });
        }

        if (timer.getMessage() == null) {
            message.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                message.setClickable(true);
                message.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(),
                        R.anim.lift_on_touch));
            }
            messageToText.setText("To: " + timer.getMessage().getTo());
            messageText.setText("Message: " + timer.getMessage().getMessage());
            messageToolbar.setTitle("Message");
            messageToolbar.setTitleTextColor(Color.WHITE);
            messageToolbar.inflateMenu(R.menu.card_menu);
            messageToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final View view2 = LayoutInflater
                            .from(getApplicationContext())
                            .inflate(R.layout.dialog_message, null);
                    final EditText editText = (EditText) view2.findViewById(R.id.number);
                    final EditText message = (EditText) view2.findViewById(R.id.message);
                    editText.setText(timer.getMessage().getTo());
                    editText.setTextColor(Color.BLACK);
                    message.setText(timer.getMessage().getMessage());
                    message.setTextColor(Color.BLACK);
                    final AlertDialog dialog1 = new AlertDialog.Builder(ListTimerExpandedActivity.this)
                            .setView(view2)
                            .setTitle("Message")
                            .setPositiveButton("Done", null)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }
                            ).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                      @Override
                                                      public void onCancel(DialogInterface dialog) {
                                                          dialog.dismiss();
                                                      }
                                                  }
                            ).create();
                    dialog1.show();
                    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editText.getText().toString().trim().isEmpty()) {
                                editText.setError("Number cannot be empty");
                            } else if (message.getText().toString().trim().isEmpty()) {
                                message.setError("Message cannot be empty");
                            } else {
                                String number = editText.getText().toString();
                                String messageText = message.getText().toString();
                                realm.beginTransaction();
                                timer.getMessage().setTo(number);
                                timer.getMessage().setMessage(messageText);
                                realm.commitTransaction();
                                dialog1.dismiss();
                            }
                        }
                    });
                    return true;
                }
            });
        }

        if (timer.getWifiState() == null) {
            wifi.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                wifi.setClickable(true);
                wifi.setStateListAnimator(
                        AnimatorInflater.loadStateListAnimator(
                                getApplicationContext(), R.anim.lift_on_touch));
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
                                                                   realm.beginTransaction();
                                                                   Timer temp = realm.where(Timer.class)
                                                                           .equalTo(AppUtility.TIMER_COLUMN_NAME, timer.getName())
                                                                           .findFirst();
                                                                   temp.getWifiState().setWifiEnabled(((Switch) v).isChecked());
                                                                   realm.commitTransaction();
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
    public void onClick(View v) {

        // TODO: Add condition for when lapse is 0 and notify after every lapse

        playTimer.setEnabled(false);
        playTimer.setAlpha(0.5f);

        if (timer.getAlertType().equals(AppUtility.DONT_NOTIFY)) {
            AppUtility.showToast("Cannot start timer when \"Don't Notify\" is set");
        } else if (!timer.getAlertType().equals(AppUtility.DONT_NOTIFY) &&
                timer.getNoOfLapse() > 0 &&
                timer.getAlertFrequency().equals(AppUtility.AFTER_EVERY_LAPSE)) {
            Intent intent = new Intent(getApplicationContext(), AlarmService.class);
            intent.putExtra(AppUtility.TIMER_NAME, timer.getName());
            startService(intent);
        } else if (timer.getAlertFrequency().equals(AppUtility.AFTER_COMPLETE_TIMER)) {
            final AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            Intent tempIntent;
            PendingIntent pendingIntent;

            if (timer.getAlertType().equals(AppUtility.NOTIFICATION_ONLY)) {
                tempIntent = new Intent(getApplicationContext(), AlarmReceiver.class);

                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                        3523341,
                        tempIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
            } else {
                tempIntent = new Intent(getApplicationContext(), AlarmRingingActivity.class);

                pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                        3523341,
                        tempIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
            }
            tempIntent.putExtra(AppUtility.TIMER_NAME, timer.getName());
            int time;
            if (timer.getNoOfLapse() > 0)
                time = (int) SystemClock.elapsedRealtime() + timer.getNoOfLapse() * timer.getDuration() * 1000;
            else
                time = (int) SystemClock.elapsedRealtime() + timer.getDuration() * 1000;

            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    time,
                    pendingIntent);
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
        realm.removeAllChangeListeners();
        timer.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
