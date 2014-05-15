package hu.bme.aut.nightshaderemote.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import hu.bme.aut.nightshaderemote.FileExtensionFilter_sts;
import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.U;
import hu.bme.aut.nightshaderemote.connectivity.CommandHandler;
import hu.bme.aut.nightshaderemote.connectivity.commands.Command;
import hu.bme.aut.nightshaderemote.connectivity.commands.ControlCommand;
import hu.bme.aut.nightshaderemote.connectivity.commands.RunCommand;

/**
 * Created by Marci on 2014.03.01..
 */
public class ScriptsFragment extends Fragment {

    public static final String TAG = "ScriptFragment";
    private ListView mScriptList;
    private ArrayAdapter<String> adapter;
    protected View root;

    public static ScriptsFragment newInstance() {
        ScriptsFragment fragment = new ScriptsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ScriptsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_scripts, container, false);

        root.findViewById(R.id.scriptPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command c = new ControlCommand(ControlCommand.CommandName.PLAY_PAUSE);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });

        root.findViewById(R.id.scriptStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command c = new ControlCommand(ControlCommand.CommandName.STOP);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });




        root.findViewById(R.id.timeSlower).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command c = new ControlCommand(ControlCommand.CommandName.SLOWER);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });

        root.findViewById(R.id.timeReal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command c = new ControlCommand(ControlCommand.CommandName.REAL_TIME);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });

        root.findViewById(R.id.timeFaster).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command c = new ControlCommand(ControlCommand.CommandName.FASTER);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });

        root.findViewById(R.id.timeCurrent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command c = new ControlCommand(ControlCommand.CommandName.CURRENT_TIME);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });


        mScriptList = (ListView) root.findViewById(R.id.scriptList);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        mScriptList.setAdapter(adapter);
        
        mScriptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = adapter.getItem(position)+".sts";
                Command c = new RunCommand(filename);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });

        refreshScriptList();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshScriptList();
    }

    /**
     * Frissíti a scriptek listáját
     */
    private void refreshScriptList() {

        File sd = Environment.getExternalStorageDirectory();
        File searchDir = new File(sd, new File(U.C.APP_FOLDER, U.C.SCRIPTS_FOLDER).getPath());
        boolean result = searchDir.mkdirs();

        String[] mFileNames = searchDir.list(new FileExtensionFilter_sts());
        if (mFileNames == null) mFileNames = new String[0];
        Arrays.sort(mFileNames);

        adapter.clear();
        adapter.setNotifyOnChange(false);
        for (String s : mFileNames) {
            adapter.add(s.substring(0,s.length()-4));
        }
        adapter.notifyDataSetChanged();
    }
}
