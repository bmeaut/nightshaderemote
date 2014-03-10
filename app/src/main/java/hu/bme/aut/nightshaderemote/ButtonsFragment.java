package hu.bme.aut.nightshaderemote;

/**
 * Created by Marci on 2014.03.01..
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import hu.bme.aut.nightshaderemote.connectivity.Command;
import hu.bme.aut.nightshaderemote.connectivity.FlagCommand;
import hu.bme.aut.nightshaderemote.connectivity.SendCommand;

public class ButtonsFragment extends Fragment {

    public static final String TAG = "ButtonsFragment";

    protected View root;
    protected FlagButtonOnclickListener flagButtonOnclickListener;

    public static ButtonsFragment newInstance() {
        ButtonsFragment fragment = new ButtonsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ButtonsFragment() {
        flagButtonOnclickListener = new FlagButtonOnclickListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_buttons, container, false);

        prepareButtons();

        return root;
    }

    private void prepareButtons() {
        root.findViewById(R.id.toggleButton_c).setTag(FlagCommand.CommandName.CONSTELLATION_LINES);
        root.findViewById(R.id.toggleButton_v).setTag(FlagCommand.CommandName.CONSTELLATION_LABELS);
        root.findViewById(R.id.toggleButton_r).setTag(FlagCommand.CommandName.CONSTELLATION_ART);

        root.findViewById(R.id.toggleButton_z).setTag(FlagCommand.CommandName.AZIMUTHAL_GRID);
        root.findViewById(R.id.toggleButton_e).setTag(FlagCommand.CommandName.EQUTORIAL_GRID);
        root.findViewById(R.id.toggleButton_g).setTag(FlagCommand.CommandName.GROUND);

        root.findViewById(R.id.toggleButton_q).setTag(FlagCommand.CommandName.CARDINAL_POINTS);
        root.findViewById(R.id.toggleButton_a).setTag(FlagCommand.CommandName.ATMOSPHERE);
        root.findViewById(R.id.toggleButton_p).setTag(FlagCommand.CommandName.BODY_LABELS);

        root.findViewById(R.id.toggleButton_n).setTag(FlagCommand.CommandName.NEBULA_LABELS);
        root.findViewById(R.id.toggleButton_enter).setTag(FlagCommand.CommandName.MOUNT);
        //root.findViewById(R.id.toggleButton_space).setTag(/* track */);

        for (View v : root.getTouchables()) {
            if (v instanceof ToggleButton) {
                v.setOnClickListener(flagButtonOnclickListener);
            }
        }
    }

    private SendCommand prepareSendCommand() {
        // TODO shared prefs-ből!!!!
        return new SendCommand("192.168.0.108", 8888, new SendCommand.OnCommandSentListener() {
            @Override
            public void onCommandSent(String result) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class FlagButtonOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getTag() != null) {
                try {
                    FlagCommand.CommandName commandName  = (FlagCommand.CommandName) v.getTag();
                    Command c = new FlagCommand(commandName, FlagCommand.CommandState.TOGGLE);
                    prepareSendCommand().execute(c);

                    Log.d(TAG, "Executing command: " + c.getPath());
                } catch (ClassCastException e) {
                    // no tag - no action
                    Log.w(TAG, "Not a commandName tag!", e);
                }
            } else {
                // no tag - no action
                Log.w(TAG, "No tag found!");
            }

        }
    }
}

