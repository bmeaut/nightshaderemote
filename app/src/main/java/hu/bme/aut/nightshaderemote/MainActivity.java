package hu.bme.aut.nightshaderemote;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by akos on 2/14/14.
 */
public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
