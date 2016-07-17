package co.magency.huzaima.timer.Application;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

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
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).withMetaTables().build())
                        .build());

        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder(getApplicationContext())
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();

        AppUtility.realm = Realm.getInstance(realmConfiguration);
    }

}
