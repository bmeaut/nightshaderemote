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

import hu.bme.aut.nightshaderemote.FileExtensionFilter;
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

        root.findViewById(R.id.scriptBuPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command c = new ControlCommand(ControlCommand.CommandName.PLAY_PAUSE);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });

        root.findViewById(R.id.scriptBuPlay).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Command c = new ControlCommand(ControlCommand.CommandName.STOP);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
                return true;
            }
        });

        mScriptList = (ListView) root.findViewById(R.id.scriptList);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        mScriptList.setAdapter(adapter);

        mScriptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = adapter.getItem(position);
                Command c = new RunCommand(filename);
                /*new SendCommand(U.getServerAddressPref(), U.getServerPortPref(), new SendCommand.OnCommandSentListener() {
                    @Override
                    public void onCommandSent(String result) {
                        Toast .makeText(ScriptsFragment.this.getActivity(), result, Toast.LENGTH_SHORT).show();
                    }
                }).execute(c);*/
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
            }
        });

        refreshScriptList();

        return root;
    }

    private void refreshScriptList() {

        File sd = Environment.getExternalStorageDirectory();
        File searchDir = new File(sd, new File(U.C.APP_FOLDER, U.C.SCRIPTS_FOLDER).getPath());
        boolean result = searchDir.mkdirs(); // első indulásnál jön létre

        String[] mFileNames = searchDir.list(new FileExtensionFilter());
        if (mFileNames == null) mFileNames = new String[0];
        Arrays.sort(mFileNames);

        adapter.clear();
        adapter.setNotifyOnChange(false);
        for (String s : mFileNames) {
            adapter.add(s);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshScriptList();
    }
}
