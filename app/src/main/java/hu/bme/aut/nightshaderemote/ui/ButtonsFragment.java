package hu.bme.aut.nightshaderemote.ui;

/**
 * Created by Marci on 2014.03.01..
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.connectivity.CommandHandler;
import hu.bme.aut.nightshaderemote.connectivity.commands.Command;
import hu.bme.aut.nightshaderemote.connectivity.commands.FlagCommand;
import hu.bme.aut.nightshaderemote.connectivity.models.JFlagState;
import hu.bme.aut.nightshaderemote.connectivity.models.JResponse;

public class ButtonsFragment extends Fragment {

    public static final String TAG = "ButtonsFragment";

    protected View root;
    protected FlagButtonOnclickListener flagButtonOnclickListener;

    protected BroadcastReceiver responseReceiver;

    public static ButtonsFragment newInstance() {
        ButtonsFragment fragment = new ButtonsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ButtonsFragment() {
        flagButtonOnclickListener = new FlagButtonOnclickListener();
        responseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (CommandHandler.INTENT_ACTION_RESPONSE_ARRIVED.equalsIgnoreCase(intent.getAction())) {
                    if (intent.hasExtra(CommandHandler.KEY_RESPONSE)) {
                        JResponse response = (JResponse) intent.getSerializableExtra(CommandHandler.KEY_RESPONSE);

                        JFlagState fs = response.getFlagState();

                        if (fs != null) {
                            setFlagStates(fs);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_buttons, container, false);

        prepareButtons();
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .registerReceiver(responseReceiver, CommandHandler.INTENT_FILTER_RESPONSE_ARRIVED);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .unregisterReceiver(responseReceiver);
    }

    protected void setFlagStates(JFlagState fs) {
        getFlagButton(R.id.toggleButton_constlines).setChecked(fs.isConstellationLines());
        getFlagButton(R.id.toggleButton_constnames).setChecked(fs.isConstellationLabels());
        getFlagButton(R.id.toggleButton_constart).setChecked(fs.isConstellationArt());

        getFlagButton(R.id.toggleButton_azigrid).setChecked(fs.isAzimuthalGrid());
        getFlagButton(R.id.toggleButton_equagrid).setChecked(fs.isEquatorialGrid());
        getFlagButton(R.id.toggleButton_ground).setChecked(fs.isGround());

        getFlagButton(R.id.toggleButton_cardinalpoints).setChecked(fs.isCardinalPoints());
        getFlagButton(R.id.toggleButton_atmosphere).setChecked(fs.isAtmosphere());
        getFlagButton(R.id.toggleButton_planetnames).setChecked(fs.isBodyLabels());

        getFlagButton(R.id.toggleButton_nebulanames).setChecked(fs.isNebulaLabels());
        getFlagButton(R.id.toggleButton_coordinatesys).setChecked(fs.isMount());
        getFlagButton(R.id.toggleButton_constbound).setChecked(fs.isConstellationBoundaries());
    }

    protected ToggleButton getFlagButton(int id) {
        return (ToggleButton) root.findViewById(id);
    }

    private void prepareButtons() {
        root.findViewById(R.id.toggleButton_constlines).setTag(new FlagCommand(FlagCommand.CommandName.CONSTELLATION_LINES, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_constnames).setTag(new FlagCommand(FlagCommand.CommandName.CONSTELLATION_LABELS, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_constart).setTag(new FlagCommand(FlagCommand.CommandName.CONSTELLATION_ART, FlagCommand.CommandState.TOGGLE));

        root.findViewById(R.id.toggleButton_azigrid).setTag(new FlagCommand(FlagCommand.CommandName.AZIMUTHAL_GRID, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_equagrid).setTag(new FlagCommand(FlagCommand.CommandName.EQUTORIAL_GRID, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_ground).setTag(new FlagCommand(FlagCommand.CommandName.GROUND, FlagCommand.CommandState.TOGGLE));

        root.findViewById(R.id.toggleButton_cardinalpoints).setTag(new FlagCommand(FlagCommand.CommandName.CARDINAL_POINTS, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_atmosphere).setTag(new FlagCommand(FlagCommand.CommandName.ATMOSPHERE, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_planetnames).setTag(new FlagCommand(FlagCommand.CommandName.BODY_LABELS, FlagCommand.CommandState.TOGGLE));

        root.findViewById(R.id.toggleButton_nebulanames).setTag(new FlagCommand(FlagCommand.CommandName.NEBULA_LABELS, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_coordinatesys).setTag(new FlagCommand(FlagCommand.CommandName.MOUNT, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_constbound).setTag(new FlagCommand(FlagCommand.CommandName.CONSTELLATION_BOUNDARIES, FlagCommand.CommandState.TOGGLE));

        for (View v : root.getTouchables()) {
            if (v instanceof ToggleButton) {
                v.setOnClickListener(flagButtonOnclickListener);
            }
        }
    }

    private class FlagButtonOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getTag() != null && v.getTag() instanceof Command) {
                Command c = ((Command) v.getTag());
                /*prepareSendCommand().execute(c);*/
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));

                Log.d(TAG, "Executing command: " + c.getPath());
            } else {
                // no tag - no action
                Log.w(TAG, "No tag found!");
            }

        }
    }
}

