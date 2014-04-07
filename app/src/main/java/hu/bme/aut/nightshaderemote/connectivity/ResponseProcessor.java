package hu.bme.aut.nightshaderemote.connectivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import hu.bme.aut.nightshaderemote.connectivity.models.JResponse;

/**
 * @author Ákos Pap
 */
public class ResponseProcessor {

    public static final String TAG = "ResponseProcessor";
    public static final String INTENT_ACTION_RESPONSE_ARRIVED = "responseArrived";
    public static final String KEY_RESPONSE = "response";

    public static IntentFilter INTENT_FILTER_RESPONSE_ARRIVED = new IntentFilter(ResponseProcessor.INTENT_ACTION_RESPONSE_ARRIVED);

    protected Context context;

    public ResponseProcessor(Context context) {
        this.context = context;
    }

    public void process(String response) {
        if (! TextUtils.isEmpty(response) && ! "--- no result ---".equalsIgnoreCase(response)) {
            JResponse res = null;
            try {
                res = new ObjectMapper().readValue(response, JResponse.class);

                sendBroadcast(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, TextUtils.isEmpty(res.getResponse()) ? "--" : res.getResponse());
        }
    }

    protected void sendBroadcast(JResponse response) {
        Intent i = new Intent(INTENT_ACTION_RESPONSE_ARRIVED);
        i.putExtra(KEY_RESPONSE, response);
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }
}
