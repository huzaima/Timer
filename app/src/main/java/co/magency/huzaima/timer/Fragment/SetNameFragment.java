package co.magency.huzaima.timer.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import co.magency.huzaima.timer.Activity.CreateTimerActivity;
import co.magency.huzaima.timer.Interface.OnNextButtonClickListener;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;

public class SetNameFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton next;
    private EditText name, lapse;
    private OnNextButtonClickListener onNextButtonClickListener;

    public SetNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_set_name, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
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

    private void initViews(View v) {

        // EditText
        name = (EditText) v.findViewById(R.id.timer_name);
        lapse = (EditText) v.findViewById(R.id.no_of_lapses);

        // FAB from parent activity
        next = CreateTimerActivity.next;
    }

    private void attachListeners() {
        next.setOnClickListener(this);
        next.setEnabled(true);
    }

    private void detachListeners() {
        next.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        String tempName = name.getText().toString();
        int noOfLapse;
        try {
            noOfLapse = Integer.parseInt(lapse.getText().toString());
        } catch (NumberFormatException e) {
            noOfLapse = 0;
        }

        Bundle bundle = new Bundle();

        if (!tempName.isEmpty()) {
            bundle.putInt(AppUtility.INPUT_SCREEN, AppUtility.NAME_INPUT_SCREEN);
            bundle.putString(AppUtility.TIMER_NAME, tempName);
            bundle.putInt(AppUtility.TIMER_LAPSE, noOfLapse);
            onNextButtonClickListener.buttonClicked(bundle);
        } else {
            name.setError("Name cannot be empty");
        }
    }
}
