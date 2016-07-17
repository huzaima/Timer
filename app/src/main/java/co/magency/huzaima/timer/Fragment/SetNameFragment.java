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
    private EditText name;
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

        // FAB from parent activity
        next = CreateTimerActivity.next;
    }

    private void attachListeners() {
        next.setOnClickListener(this);
        next.setEnabled(true);
        next.setImageResource(R.drawable.ic_navigate_next_white_24px);
    }

    private void detachListeners() {
        next.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        String tempName = name.getText().toString();
        if (name != null && !tempName.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putInt(AppUtility.INPUT_SCREEN, AppUtility.NAME_INPUT_SCREEN);
            bundle.putString(AppUtility.TIMER_NAME, tempName);
            onNextButtonClickListener.buttonClicked(bundle);
        } else {
            name.setError("Name cannot be empty");
        }
    }
}
