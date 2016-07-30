package co.magency.huzaima.timer.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Huzaima Khan on 7/16/2016.
 */
public class PreferenceManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    private final int PRIVATE_MODE = 0;

    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(AppUtility.SHARED_PREFERENCE_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setIsLaunchedFirstTIme(boolean isLaunchedFirstTime) {
        editor.putBoolean(AppUtility.IS_LAUNCED_FIRST_TIME, isLaunchedFirstTime);
        editor.commit();
    }

    public boolean isLaunchedFirstTime() {
        return sharedPreferences.getBoolean(AppUtility.IS_LAUNCED_FIRST_TIME, false);
    }

    public void setIsShowcaseViewShown(boolean isShowcaseViewShown) {
        editor.putBoolean(AppUtility.IS_SHOWCASE_VIEW_SHOWN, isShowcaseViewShown);
        editor.commit();
    }

    public boolean isShowcaseViewShown() {
        return sharedPreferences.getBoolean(AppUtility.IS_SHOWCASE_VIEW_SHOWN, false);
    }
}
