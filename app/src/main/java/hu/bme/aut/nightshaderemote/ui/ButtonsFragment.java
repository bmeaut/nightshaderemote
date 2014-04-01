package hu.bme.aut.nightshaderemote.ui;

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

import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.U;
import hu.bme.aut.nightshaderemote.connectivity.Command;
import hu.bme.aut.nightshaderemote.connectivity.FlagCommand;
import hu.bme.aut.nightshaderemote.connectivity.SendCommand;
import hu.bme.aut.nightshaderemote.connectivity.models.RefreshCommand;

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

        setHasOptionsMenu(true);

        return root;
    }

    private void prepareButtons() {
        root.findViewById(R.id.toggleButton_c).setTag(new FlagCommand(FlagCommand.CommandName.CONSTELLATION_LINES, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_v).setTag(new FlagCommand(FlagCommand.CommandName.CONSTELLATION_LABELS, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_r).setTag(new FlagCommand(FlagCommand.CommandName.CONSTELLATION_ART, FlagCommand.CommandState.TOGGLE));

        root.findViewById(R.id.toggleButton_z).setTag(new FlagCommand(FlagCommand.CommandName.AZIMUTHAL_GRID, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_e).setTag(new FlagCommand(FlagCommand.CommandName.EQUTORIAL_GRID, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_g).setTag(new FlagCommand(FlagCommand.CommandName.GROUND, FlagCommand.CommandState.TOGGLE));

        root.findViewById(R.id.toggleButton_q).setTag(new FlagCommand(FlagCommand.CommandName.CARDINAL_POINTS, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_a).setTag(new FlagCommand(FlagCommand.CommandName.ATMOSPHERE, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_p).setTag(new FlagCommand(FlagCommand.CommandName.BODY_LABELS, FlagCommand.CommandState.TOGGLE));

        root.findViewById(R.id.toggleButton_n).setTag(new FlagCommand(FlagCommand.CommandName.NEBULA_LABELS, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_enter).setTag(new FlagCommand(FlagCommand.CommandName.MOUNT, FlagCommand.CommandState.TOGGLE));
        root.findViewById(R.id.toggleButton_space).setTag(new RefreshCommand()); // TODO ideiglenes, menübe kirakni !!!

        for (View v : root.getTouchables()) {
            if (v instanceof ToggleButton) {
                v.setOnClickListener(flagButtonOnclickListener);
            }
        }
    }

    private SendCommand prepareSendCommand() {
        // TODO shared prefs-ből!!!!
        return new SendCommand(U.getServerAddressPref(), U.getServerPortPref(), new SendCommand.OnCommandSentListener() {
            @Override
            public void onCommandSent(String result) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class FlagButtonOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getTag() != null && v.getTag() instanceof Command) {
                Command c = ((Command) v.getTag());
                prepareSendCommand().execute(c);

                Log.d(TAG, "Executing command: " + c.getPath());
            } else {
                // no tag - no action
                Log.w(TAG, "No tag found!");
            }

        }
    }
}

