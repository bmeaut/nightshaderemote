package hu.bme.aut.nightshaderemote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hu.bme.aut.nightshaderemote.connectivity.Command;
import hu.bme.aut.nightshaderemote.connectivity.ExecuteCommand;
import hu.bme.aut.nightshaderemote.connectivity.FlagCommand;
import hu.bme.aut.nightshaderemote.connectivity.RunCommand;
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
                Command command = new FlagCommand(FlagCommand.CommandName.ATMOSPHERE, FlagCommand.CommandState.TOGGLE);

                new SendCommand(U.getServerAddressPref(), U.getServerPortPref(), MainActivity.this).execute(command);
            }
        });

        findViewById(R.id.executeScript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ((EditText) MainActivity.this.findViewById(R.id.scriptContent)).getText().toString();

                Command c = new ExecuteCommand(content);

                new SendCommand(U.getServerAddressPref(), U.getServerPortPref(), MainActivity.this).execute(c);
            }
        });

        findViewById(R.id.runScript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "multi.sts";
                Command c = new RunCommand(filename);
                new SendCommand(U.getServerAddressPref(), U.getServerPortPref(), MainActivity.this).execute(c);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void onCommandSent(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_preferences:
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
