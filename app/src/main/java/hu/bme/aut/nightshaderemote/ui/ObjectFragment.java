package hu.bme.aut.nightshaderemote.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.connectivity.CommandHandler;
import hu.bme.aut.nightshaderemote.connectivity.commands.FetchCommand;
import hu.bme.aut.nightshaderemote.connectivity.commands.ParametrizedFetchCommand;
import hu.bme.aut.nightshaderemote.connectivity.models.JObjectImage;
import hu.bme.aut.nightshaderemote.connectivity.models.JResponse;

/**
 * @author Ákos Pap
 */
public class ObjectFragment extends Fragment {

    public static final String TAG = "ObjectFragment";

    protected View root;

    protected Spinner catalogSpinner;
    protected EditText cataloggedIdEt;
    protected EditText freeTextIdEt;

    protected ImageView objectImageIv;
    protected TextView objectNameTv;


    protected BroadcastReceiver objectImageReceiver;

    public static ObjectFragment newInstance() {
        ObjectFragment fragment = new ObjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_object, container, false);

        catalogSpinner = (Spinner) root.findViewById(R.id.catalogType);
        cataloggedIdEt = (EditText) root.findViewById(R.id.objectIdentifierCatalog);
        freeTextIdEt = (EditText) root.findViewById(R.id.objectIdentifier);
        objectImageIv = (ImageView) root.findViewById(R.id.objectImage);
        objectNameTv = (TextView) root.findViewById(R.id.objectName);

        Button buTrack = (Button) root.findViewById(R.id.buTrack);
        buTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String objectId = getObjectId();

                ParametrizedFetchCommand c = new ParametrizedFetchCommand(FetchCommand.FetchTarget.OBJECT_IMAGE);
                c.addParameter("objectId", objectId);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });

        objectImageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (CommandHandler.INTENT_ACTION_RESPONSE_ARRIVED.equalsIgnoreCase(intent.getAction())) {
                    if (intent.hasExtra(CommandHandler.KEY_RESPONSE)) {
                        JResponse response = (JResponse) intent.getSerializableExtra(CommandHandler.KEY_RESPONSE);

                        JObjectImage oi = response.getObjectImage();

                        if (oi != null) {
                            showImage(oi);
                        } else {
                            Log.w(TAG, "Received broadcast didn't contain flag states!");
                        }
                    } else {
                        Log.w(TAG, "Received broadcast didn't contain any response!");
                    }
                } else {
                    Log.w(TAG, "Received a wrong broadcast!");
                }
            }
        };

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(objectImageReceiver, CommandHandler.INTENT_FILTER_RESPONSE_ARRIVED);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .unregisterReceiver(objectImageReceiver);
    }

    private void showImage(JObjectImage oi) {
        Bitmap img = oi.getImage();

        if (img == null) {
            objectImageIv.setImageResource(R.drawable.not_found);
        } else {
            objectImageIv.setImageBitmap(img);
        }

        objectNameTv.setText(oi.getName());
    }

    protected String getObjectId() {
        String catalog = catalogSpinner.getSelectedItem().toString();
        String id = cataloggedIdEt.getText().toString();

        return catalog + id;
    }


}
