package hu.bme.aut.nightshaderemote.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import hu.bme.aut.nightshaderemote.U;
import hu.bme.aut.nightshaderemote.connectivity.commands.Command;
import hu.bme.aut.nightshaderemote.connectivity.models.JResponse;

/**
 * @author Ákos Pap
 */
public class CommandHandler implements SendCommand.OnCommandSentListener {
    public static final String TAG = "CommandHandler";

    public static final String INTENT_ACTION_COMMAND = "commandAction";
    public static final String INTENT_ACTION_RESPONSE_ARRIVED = "responseArrived";
    public static final String INTENT_ACTION_NO_RESPONSE = "noResponse";

    public static IntentFilter INTENT_FILTER_RESPONSE_ARRIVED = new IntentFilter(INTENT_ACTION_RESPONSE_ARRIVED);
    public static IntentFilter INTENT_FILTER_NO_RESPONSE = new IntentFilter(INTENT_ACTION_NO_RESPONSE);

    public static final String KEY_COMMAND = "command";
    public static final String KEY_RESPONSE = "response";
    public static final String KEY_MESSAGE = "message";

    public static Intent createIntent(Command c) {
        Intent i = new Intent(INTENT_ACTION_COMMAND);
        i.putExtra(KEY_COMMAND, c);
        return i;
    }

    protected Context context;
    protected LocalBroadcastManager lbm;
    protected BroadcastReceiver receiver;


    public CommandHandler(Context context) {
        this.context = context;
        this.lbm = LocalBroadcastManager.getInstance(context);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Received intent: " + intent.getAction());

                if (intent.hasExtra(KEY_COMMAND)) {
                    Command c = (Command) intent.getSerializableExtra(KEY_COMMAND);
                    new SendCommand(U.getServerAddressPref(), U.getServerPortPref(), CommandHandler.this)
                            .execute(c);
                }
            }
        };
    }

    @Override
    public void onCommandSent(String result) {
        if (! TextUtils.isEmpty(result)) {
            JResponse res = null;
            try {
                res = new ObjectMapper().readValue(result, JResponse.class);

                sendBroadcast(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, TextUtils.isEmpty(res.getResponse()) ? "--" : res.getResponse());
        } else {
            Log.d(TAG, "No response arrived!");
            sendErrorBroadcast("Something went wrong...\nNo response from server!");
        }
    }

    protected void sendBroadcast(JResponse response) {
        Intent i = new Intent(INTENT_ACTION_RESPONSE_ARRIVED);
        i.putExtra(KEY_RESPONSE, response);
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }

    protected void sendErrorBroadcast(String message) {
        Intent i = new Intent(INTENT_ACTION_NO_RESPONSE);
        i.putExtra(KEY_MESSAGE, message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }

    public void onResume() {
        lbm.registerReceiver(receiver, new IntentFilter(INTENT_ACTION_COMMAND));
    }

    public void onPause() {
        lbm.unregisterReceiver(receiver);
    }
}
