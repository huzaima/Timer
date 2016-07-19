package co.magency.huzaima.timer.Application;

import android.app.Application;

import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Huzaima Khan on 7/16/2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder(getApplicationContext())
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();

        AppUtility.realm = Realm.getInstance(realmConfiguration);

        AppUtility.context = getApplicationContext();
    }

}
