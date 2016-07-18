package co.magency.huzaima.timer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;

import java.util.ArrayList;
import java.util.List;

import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.PreferenceManager;

public class WelcomeActivity extends OnboarderActivity {

    List<OnboarderPage> onBoarderPages;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferenceManager = new PreferenceManager(getApplicationContext());
        if (!preferenceManager.isLaunchedFirstTime()) {
            onFinishButtonPressed();
        }
        super.onCreate(savedInstanceState);

        preferenceManager.setIsLaunchedFirstTIme(false);

        onBoarderPages = new ArrayList<>();

        OnboarderPage onBoarderPage1 = new OnboarderPage(getString(R.string.slide_1_title), getString(R.string.slide_1_desc), R.drawable.timerclock);
        OnboarderPage onBoarderPage2 = new OnboarderPage(getString(R.string.slide_2_title), getString(R.string.slide_2_desc), R.drawable.timerclock);
        OnboarderPage onBoarderPage3 = new OnboarderPage(getString(R.string.slide_3_title), getString(R.string.slide_3_desc), R.drawable.timerclock);
        OnboarderPage onBoarderPage4 = new OnboarderPage(getString(R.string.slide_4_title), getString(R.string.slide_4_desc), R.drawable.timerclock);

        onBoarderPage1.setBackgroundColor(R.color.bg_screen1);
        onBoarderPage2.setBackgroundColor(R.color.bg_screen2);
        onBoarderPage3.setBackgroundColor(R.color.bg_screen3);
        onBoarderPage4.setBackgroundColor(R.color.bg_screen4);

        onBoarderPages.add(onBoarderPage1);
        onBoarderPages.add(onBoarderPage2);
        onBoarderPages.add(onBoarderPage3);
        onBoarderPages.add(onBoarderPage4);

        setDividerVisibility(View.GONE);
        shouldUseFloatingActionButton(true);

        setOnboardPagesReady(onBoarderPages);
    }

    @Override
    public void onFinishButtonPressed() {
        startActivity(new Intent(getApplicationContext(), ListTimerActivity.class));
        finish();
    }

}
