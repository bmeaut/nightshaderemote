package hu.bme.aut.nightshaderemote;

import android.app.Application;

/**
 * Created by akos on 2014.03.15..
 */
public class NightshadeRemoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        U.init(getApplicationContext());
    }
}
