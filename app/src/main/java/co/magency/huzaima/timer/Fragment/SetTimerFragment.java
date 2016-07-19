package co.magency.huzaima.timer.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import co.magency.huzaima.timer.Activity.CreateTimerActivity;
import co.magency.huzaima.timer.Interface.OnNextButtonClickListener;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;

public class SetTimerFragment extends Fragment implements View.OnClickListener {

    private Button one, two, three, four, five, six, seven, eight, nine, zero;
    private FloatingActionButton next;
    private int currentDigitCount = 0;
    private ImageButton delete, reset;
    private TextView hour, minute, second;
    private HashMap<Integer, String[]> states;
    private OnNextButtonClickListener onNextButtonClickListener;

    public SetTimerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_set_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        attachListeners();

        states = new HashMap<>(6);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnNextButtonClickListener) {
            onNextButtonClickListener = (OnNextButtonClickListener) context;
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
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.zero:
                if (hour.getText().equals("00") && minute.getText().equals("00") && second.getText().equals("00")) {
                    next.setEnabled(false);
                    break;
                }
            case R.id.one:
            case R.id.two:
            case R.id.three:
            case R.id.four:
            case R.id.five:
            case R.id.six:
            case R.id.seven:
            case R.id.eight:
            case R.id.nine:
                Button button = (Button) v;
                String state[] = new String[]{hour.getText().toString(), minute.getText().toString(), second.getText().toString()};
                String text = button.getText().toString();
                if (currentDigitCount == 0) {
                    next.setEnabled(true);
                    second.setText("0" + text);
                    states.put(currentDigitCount, state);
                    currentDigitCount++;
                } else if (currentDigitCount == 1) {
                    shiftLeftSecondDigit(text);
                    states.put(currentDigitCount, state);
                    currentDigitCount++;
                } else if (currentDigitCount == 2) {
                    minute.setText("0" + second.getText().charAt(0));
                    shiftLeftSecondDigit(text);
                    states.put(currentDigitCount, state);
                    currentDigitCount++;
                } else if (currentDigitCount == 3) {
                    shiftLeftMinuteDigit();
                    shiftLeftSecondDigit(text);
                    states.put(currentDigitCount, state);
                    currentDigitCount++;
                } else if (currentDigitCount == 4) {
                    hour.setText("0" + minute.getText().charAt(0));
                    shiftLeftMinuteDigit();
                    shiftLeftSecondDigit(text);
                    states.put(currentDigitCount, state);
                    currentDigitCount++;
                } else if (currentDigitCount == 5) {
                    shiftLeftHourDigit();
                    shiftLeftMinuteDigit();
                    shiftLeftSecondDigit(text);
                    states.put(currentDigitCount, state);
                    currentDigitCount++;
                }
                break;

            case R.id.delete:
                if (currentDigitCount > 0) {
                    currentDigitCount--;
                    state = states.get(currentDigitCount);
                    hour.setText(state[0]);
                    minute.setText(state[1]);
                    second.setText(state[2]);
                }
                if (currentDigitCount == 0)
                    next.setEnabled(false);
                break;

            case R.id.reset:
                hour.setText("00");
                minute.setText("00");
                second.setText("00");
                currentDigitCount = 0;
                states.clear();
                next.setEnabled(false);
                break;

            case R.id.next:
                int targetHour = Integer.parseInt(hour.getText().toString());
                int targetMinute = Integer.parseInt(minute.getText().toString());
                int targetSeconds = Integer.parseInt(second.getText().toString());

                Bundle bundle = new Bundle();
                bundle.putInt(AppUtility.INPUT_SCREEN, AppUtility.TIME_INPUT_SCREEN);
                bundle.putInt(AppUtility.HOUR, targetHour);
                bundle.putInt(AppUtility.MINUTE, targetMinute);
                bundle.putInt(AppUtility.SECOND, targetSeconds);
                onNextButtonClickListener.onNextButtonClicked(bundle);
                break;
        }
    }

    private void shiftLeftSecondDigit(String text) {
        second.setText(second.getText().charAt(1) + text);
    }

    private void shiftLeftMinuteDigit() {
        minute.setText(minute.getText().charAt(1) + "" + second.getText().charAt(0));
    }

    private void shiftLeftHourDigit() {
        hour.setText(hour.getText().charAt(1) + "" + minute.getText().charAt(0));
    }

    private void initViews(View v) {

        // Buttons
        one = (Button) v.findViewById(R.id.one);
        two = (Button) v.findViewById(R.id.two);
        three = (Button) v.findViewById(R.id.three);
        four = (Button) v.findViewById(R.id.four);
        five = (Button) v.findViewById(R.id.five);
        six = (Button) v.findViewById(R.id.six);
        seven = (Button) v.findViewById(R.id.seven);
        eight = (Button) v.findViewById(R.id.eight);
        nine = (Button) v.findViewById(R.id.nine);
        zero = (Button) v.findViewById(R.id.zero);
        delete = (ImageButton) v.findViewById(R.id.delete);
        reset = (ImageButton) v.findViewById(R.id.reset);

        // FAB
        next = CreateTimerActivity.next;

        // TextViews
        hour = (TextView) v.findViewById(R.id.hour);
        minute = (TextView) v.findViewById(R.id.minute);
        second = (TextView) v.findViewById(R.id.second);

    }

    private void attachListeners() {

        // Buttons
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        delete.setOnClickListener(this);
        reset.setOnClickListener(this);

        next.setOnClickListener(this);
        next.setEnabled(false);

        next.setVisibility(View.VISIBLE);
    }

    private void detachListeners() {
        one.setOnClickListener(null);
        two.setOnClickListener(null);
        three.setOnClickListener(null);
        four.setOnClickListener(null);
        five.setOnClickListener(null);
        six.setOnClickListener(null);
        seven.setOnClickListener(null);
        eight.setOnClickListener(null);
        nine.setOnClickListener(null);
        zero.setOnClickListener(null);
        delete.setOnClickListener(null);
        reset.setOnClickListener(null);

        next.setOnClickListener(null);
    }
}
