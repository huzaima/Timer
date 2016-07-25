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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.magency.huzaima.timer.Interface.OnNextButtonClickListener;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;

public class SetNameFragment extends Fragment implements View.OnClickListener {

    public FloatingActionButton next;
    @BindView(R.id.timer_name)
    public EditText name;
    @BindView(R.id.no_of_lapses)
    public EditText lapse;
    private OnNextButtonClickListener onNextButtonClickListener;
    private Unbinder unbinder;

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

        unbinder = ButterKnife.bind(this, view);
        next = (FloatingActionButton) getActivity().findViewById(R.id.next);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
            if (noOfLapse == 0)
                AppUtility.IS_ONE_LAPSE = true;
            else
                AppUtility.IS_ONE_LAPSE = false;
            onNextButtonClickListener.onNextButtonClicked(bundle);
        } else

        {
            name.setError("Name cannot be empty");
        }
    }
}
