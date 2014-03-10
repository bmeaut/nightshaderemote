package hu.bme.aut.nightshaderemote;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * Created by Marci on 2014.03.01..
 */
public class ScriptsFragment extends Fragment {

private ListView mScriptList;

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
        View v = inflater.inflate(R.layout.fragment_scripts, container, false);

        mScriptList = (ListView) v.findViewById(R.id.scriptList);

        refreshList();

        return v;
    }

    private void refreshList() {
        final String APP_FOLDER = "NightshadeRemote";
        final String SCRIPTS_FOLDER = "scripts";

        File sd = Environment.getExternalStorageDirectory();
        File searchDir = new File(sd, new File(APP_FOLDER, SCRIPTS_FOLDER).getPath());
        searchDir.mkdirs(); // első indulásnál jön létre

        String[] mFileNames = searchDir.list(new FileExtensionFilter());
        if (mFileNames == null) mFileNames = new String[0];
        Arrays.sort(mFileNames);

        ArrayAdapter<String> scriptAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, mFileNames);
        mScriptList.setAdapter(scriptAdapter);
    }

    class FileExtensionFilter implements FilenameFilter
    {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".sts") || name.endsWith(".STS"));
        }
    }
}