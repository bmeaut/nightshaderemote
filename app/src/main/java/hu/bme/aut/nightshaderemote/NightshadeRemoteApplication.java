package hu.bme.aut.nightshaderemote;

import android.app.Application;

import hu.bme.aut.nightshaderemote.connectivity.CommandHandler;

/**
 * Created by akos on 2014.03.15..
 */
public class NightshadeRemoteApplication extends Application {

    protected CommandHandler commandHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        U.init(getApplicationContext());
        commandHandler = new CommandHandler(this);

        commandHandler.onResume();
    }

}
