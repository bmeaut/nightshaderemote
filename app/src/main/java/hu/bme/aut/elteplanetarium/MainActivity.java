package hu.bme.aut.elteplanetarium;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by akos on 2/14/14.
 */
public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
