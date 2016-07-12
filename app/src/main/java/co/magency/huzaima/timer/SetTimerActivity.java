package co.magency.huzaima.timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

public class SetTimerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button one, two, three, four, five, six, seven, eight, nine, zero, startTimer;
    private int currentDigitCount = 0;
    ImageButton delete, reset;
    TextView hour, minute, second;
    HashMap<Integer, String[]> states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);

        initViews();
        attachListeners();

        states = new HashMap<>(6);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.zero:
                if (hour.getText().equals("00") && minute.getText().equals("00") && second.getText().equals("00")) {
                    startTimer.setVisibility(View.VISIBLE);
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
                    startTimer.setVisibility(View.VISIBLE);
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
                    startTimer.setVisibility(View.INVISIBLE);
                break;

            case R.id.reset:
                hour.setText("00");
                minute.setText("00");
                second.setText("00");
                currentDigitCount = 0;
                startTimer.setVisibility(View.INVISIBLE);
                break;

            case R.id.start_timer:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(AppUtility.HOUR, hour.getText().toString());
                intent.putExtra(AppUtility.MINUTE, minute.getText().toString());
                intent.putExtra(AppUtility.SECOND, second.getText().toString());
                startActivity(intent);
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

    private void initViews() {

        // Buttons
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        zero = (Button) findViewById(R.id.zero);
        delete = (ImageButton) findViewById(R.id.delete);
        reset = (ImageButton) findViewById(R.id.reset);
        startTimer = (Button) findViewById(R.id.start_timer);

        startTimer.setVisibility(View.INVISIBLE);

        // TextViews
        hour = (TextView) findViewById(R.id.hour);
        minute = (TextView) findViewById(R.id.minute);
        second = (TextView) findViewById(R.id.second);

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
        startTimer.setOnClickListener(this);
    }
}
