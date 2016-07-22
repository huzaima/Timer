package co.magency.huzaima.timer.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<Map<String, String>> contactList = new ArrayList<>();

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
                final View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_call, null);
                final AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(view)
                        .setTitle("Call")
                        .setPositiveButton("Done", null)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                call.setChecked(false);
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.dismiss();
                                call.setChecked(false);
                            }
                        }).create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) view.findViewById(R.id.number);
                        if (editText.getText().toString().isEmpty()) {
                            editText.setError("Number cannot be empty");
                        } else {
                            String number = editText.getText().toString();
                            bundle.putString(AppUtility.CALL_TO, number);
                            dialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.message:
                final View view2 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_message, null);
                final AlertDialog dialog1 = new AlertDialog.Builder(getContext())
                        .setView(view2)
                        .setTitle("Message")
                        .setPositiveButton("Done", null)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        message.setChecked(false);
                                    }
                                }
                        ).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                  @Override
                                                  public void onCancel(DialogInterface dialog) {
                                                      dialog.dismiss();
                                                      message.setChecked(false);
                                                  }
                                              }
                        ).create();
                dialog1.show();
                dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) view2.findViewById(R.id.number);
                        EditText message = (EditText) view2.findViewById(R.id.message);
                        if (editText.getText().toString().trim().isEmpty()) {
                            editText.setError("Number cannot be empty");
                        } else if (message.getText().toString().trim().isEmpty()) {
                            message.setError("Message cannot be empty");
                        } else {
                            String number = editText.getText().toString();
                            String messageText = message.getText().toString();
                            bundle.putString(AppUtility.MESSAGE_TO, number);
                            bundle.putString(AppUtility.MESSAGE_TEXT, messageText);
                            dialog1.dismiss();
                        }
                    }
                });
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

    public void PopulatePeopleList() {

        contactList.clear();

        Cursor people = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (people.moveToNext()) {
            String contactName = people.getString(people.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME));

            String contactId = people.getString(people.getColumnIndex(
                    ContactsContract.Contacts._ID));
            String hasPhone = people.getString(people.getColumnIndex(
                    ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if ((Integer.parseInt(hasPhone) > 0)) {

                // You know have the number so now query it like this
                Cursor phones = getContext().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null, null);
                while (phones.moveToNext()) {

                    //store numbers and display a dialog letting the user select which.
                    String phoneNumber = phones.getString(
                            phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                    String numberType = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.TYPE));

                    Map<String, String> NamePhoneType = new HashMap<String, String>();

                    NamePhoneType.put("Name", contactName);
                    NamePhoneType.put("Phone", phoneNumber);

                    if (numberType.equals("0"))
                        NamePhoneType.put("Type", "Work");
                    else if (numberType.equals("1"))
                        NamePhoneType.put("Type", "Home");
                    else if (numberType.equals("2"))
                        NamePhoneType.put("Type", "Mobile");
                    else
                        NamePhoneType.put("Type", "Other");

                    //Then add this map to the list.
                    contactList.add(NamePhoneType);
                }
                phones.close();
            }
        }
        people.close();
    }
}
