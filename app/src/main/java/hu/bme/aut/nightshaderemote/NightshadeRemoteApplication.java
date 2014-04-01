package hu.bme.aut.nightshaderemote;

import android.app.Application;

import hu.bme.aut.nightshaderemote.connectivity.ResponseProcessor;

/**
 * Created by akos on 2014.03.15..
 */
public class NightshadeRemoteApplication extends Application {

    protected static ResponseProcessor responseProcessor;
    public static ResponseProcessor responseProcessor() { return responseProcessor; }

    @Override
    public void onCreate() {
        super.onCreate();
        U.init(getApplicationContext());
        responseProcessor = new ResponseProcessor(this);
    }
}
