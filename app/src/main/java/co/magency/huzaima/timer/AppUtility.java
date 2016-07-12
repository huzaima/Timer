package co.magency.huzaima.timer;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Huzaima Khan on 7/13/2016.
 */
public class AppUtility {

    public static final String HOUR = "HOUR";
    public static final String MINUTE = "MINUTE";
    public static final String SECOND = "SECOND";

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
