package co.magency.huzaima.timer.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.magency.huzaima.timer.Interface.OnNextButtonClickListener;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;

public class SetNotificationFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    @BindView(R.id.wifi)
    public CheckBox wifi;
    @BindView(R.id.call)
    public CheckBox call;
    @BindView(R.id.message)
    public CheckBox message;
    @BindView(R.id.notify)
    public RadioGroup notify;
    @BindView(R.id.notify_after)
    public RadioGroup notifyAfter;
    @BindView(R.id.wifi_state)
    public RadioGroup wifiState;
    private FloatingActionButton next;
    private Bundle bundle = new Bundle();
    private OnNextButtonClickListener nextButtonClickListener;
    private Unbinder unbinder;

    public SetNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        unbinder = ButterKnife.bind(this, view);
        next = (FloatingActionButton) getActivity().findViewById(R.id.next);
        bundle.putInt(AppUtility.INPUT_SCREEN, AppUtility.NOTIFICATION_INPUT_SCREEN);
        bundle.putString(AppUtility.NOTIFICATION_TYPE, AppUtility.ALARM);
        bundle.putString(AppUtility.NOTIFICATION_FREQUENCY, AppUtility.AFTER_EVERY_LAPSE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnNextButtonClickListener) {
            nextButtonClickListener = (OnNextButtonClickListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        attachListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachListeners();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void attachListeners() {

        // RadioGroup
        notify.setOnCheckedChangeListener(this);
        notifyAfter.setOnCheckedChangeListener(this);
        wifiState.setOnCheckedChangeListener(this);

        // CheckBox
        wifi.setOnClickListener(this);
        call.setOnClickListener(this);
        message.setOnClickListener(this);

        // FAB
        next.setOnClickListener(this);
        next.setEnabled(true);
        next.setImageResource(R.drawable.ic_done_white_24px);
    }

    private void detachListeners() {
        // RadioGroup
        notify.setOnCheckedChangeListener(null);
        notifyAfter.setOnCheckedChangeListener(null);
        wifiState.setOnCheckedChangeListener(null);

        // CheckBox
        wifi.setOnClickListener(null);
        call.setOnClickListener(null);
        message.setOnClickListener(null);

        // FAB
        next.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifi:
                if (wifi.isChecked()) {
                    bundle.putBoolean(AppUtility.WIFI, true);
                    wifiState.setVisibility(View.VISIBLE);
                } else {
                    bundle.putBoolean(AppUtility.WIFI, false);
                    wifiState.setVisibility(View.GONE);
                }
                break;
            case R.id.call:
                break;
            case R.id.message:
                break;
            case R.id.next:
                nextButtonClickListener.onNextButtonClicked(bundle);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.dont_notify:
                bundle.putString(AppUtility.NOTIFICATION_TYPE, AppUtility.DONT_NOTIFY);
                for (int i = 0; i < notifyAfter.getChildCount(); i++) {
                    notifyAfter.getChildAt(i).setEnabled(false);
                }
                break;
            case R.id.notification:
                bundle.putString(AppUtility.NOTIFICATION_TYPE, AppUtility.NOTIFICATION_ONLY);

                for (int i = 0; i < notifyAfter.getChildCount(); i++) {
                    notifyAfter.getChildAt(i).setEnabled(true);
                }
                break;
            case R.id.alarm:
                bundle.putString(AppUtility.NOTIFICATION_TYPE, AppUtility.ALARM);
                for (int i = 0; i < notifyAfter.getChildCount(); i++) {
                    notifyAfter.getChildAt(i).setEnabled(true);
                }
                break;
            case R.id.notify_every_lapse:
                bundle.putString(AppUtility.NOTIFICATION_FREQUENCY, AppUtility.AFTER_EVERY_LAPSE);
                break;
            case R.id.notify_after_complete:
                bundle.putString(AppUtility.NOTIFICATION_FREQUENCY, AppUtility.AFTER_COMPLETE_TIMER);
                break;
            case R.id.wifi_on:
                bundle.putBoolean(AppUtility.WIFI_STATE, true);
                break;
            case R.id.wifi_off:
                bundle.putBoolean(AppUtility.WIFI_STATE, false);
                break;
        }
    }
}
