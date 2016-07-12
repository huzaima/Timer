package co.magency.huzaima.timer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SetTimerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button one, two, three, four, five, six, seven, eight, nine, zero, startTimer;
    ImageButton delete, reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);

        initViews();

        attachListeners();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.one:
                break;
            case R.id.two:
                break;
            case R.id.three:
                break;
            case R.id.four:
                break;
            case R.id.five:
                break;
            case R.id.six:
                break;
            case R.id.seven:
                break;
            case R.id.eight:
                break;
            case R.id.nine:
                break;
            case R.id.zero:
                break;
            case R.id.delete:
                break;
            case R.id.reset:
                break;
        }
    }

    private void initViews() {
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
    }

    private void attachListeners() {
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
    }
}
