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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.connectivity.CommandHandler;
import hu.bme.aut.nightshaderemote.connectivity.commands.Command;
import hu.bme.aut.nightshaderemote.connectivity.commands.ControlCommand;
import hu.bme.aut.nightshaderemote.connectivity.commands.FetchCommand;
import hu.bme.aut.nightshaderemote.connectivity.commands.ParametrizedControlCommand;
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

    protected Spinner objectTypeSp;
    protected EditText objectIdentifierEt;

    protected EditText freeTextIdEt;

    protected ImageView objectImageIv;
    protected TextView objectNameTv;

    protected SeekBar zoomBar;


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
        objectTypeSp = (Spinner) root.findViewById(R.id.objectType);
        objectIdentifierEt = (EditText) root.findViewById(R.id.objectIdentifier);

        zoomBar = (SeekBar) root.findViewById(R.id.zoomBar);
        zoomBar.setEnabled(false);
        zoomBar.setThumb(getResources().getDrawable(android.R.drawable.ic_menu_search));

        root.findViewById(R.id.buFind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String objectId = getObjectId();

                ParametrizedFetchCommand c = new ParametrizedFetchCommand(FetchCommand.FetchTarget.OBJECT_IMAGE);
                c.addParameter(ParametrizedFetchCommand.PARAM_OBJECT_ID, objectId);
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

        root.findViewById(R.id.buZoomIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command c = new ControlCommand(ControlCommand.CommandName.ZOOM_IN);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));

                zoomBar.incrementProgressBy(1);
            }
        });

        root.findViewById(R.id.buZoomOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command c = new ControlCommand(ControlCommand.CommandName.ZOOM_OUT);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));

                zoomBar.incrementProgressBy(-1);
            }
        });

        root.findViewById(R.id.buTrack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = objectTypeSp.getSelectedItem().toString();
                String identifier = objectIdentifierEt.getText().toString();

                ParametrizedControlCommand c = new ParametrizedControlCommand(ControlCommand.CommandName.SELECT);
                c.addParameter(ParametrizedControlCommand.PARAM_OBJECT_TYPE, type);
                c.addParameter(ParametrizedControlCommand.PARAM_IDENTIFIER, identifier);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });

        root.findViewById(R.id.buUnselect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControlCommand c = new ControlCommand(ControlCommand.CommandName.DESELECT);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
                objectNameTv.setText(R.string.object_nothing_selected);
            }
        });

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
