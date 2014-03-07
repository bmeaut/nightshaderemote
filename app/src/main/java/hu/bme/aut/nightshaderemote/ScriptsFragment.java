package hu.bme.aut.nightshaderemote;

import android.os.Bundle;
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

    ListView mScriptList;
    String[] mFileNames;
    File mFile;

    public static ScriptsFragment newInstance(int sectionNumber) {
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

        mFile = new File("/sdcard/ns_scripts");
        mFileNames = mFile.list(new FileExtensionFilter());
        Arrays.sort(mFileNames);


        ArrayAdapter<String> scriptAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, mFileNames);
        mScriptList.setAdapter(scriptAdapter);

        return v;
    }

    class FileExtensionFilter implements FilenameFilter
    {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".sts") || name.endsWith(".STS"));
        }
    }
}