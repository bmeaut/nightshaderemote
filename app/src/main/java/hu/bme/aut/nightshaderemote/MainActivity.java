package hu.bme.aut.nightshaderemote;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import hu.bme.aut.nightshaderemote.connectivity.SendCommand;

/**
 * Created by akos on 2/14/14.
 */
public class MainActivity extends ActionBarActivity implements SendCommand.OnCommandSentListener {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tgbtnConstLines).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = "/constellationLines/toggle";

                new SendCommand("192.168.0.108", 8888, command, MainActivity.this).execute();
            }
        });
    }

    @Override
    public void onCommandSent(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }
}
