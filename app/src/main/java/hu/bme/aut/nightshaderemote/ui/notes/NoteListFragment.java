package hu.bme.aut.nightshaderemote.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import hu.bme.aut.nightshaderemote.FileExtensionFilter_txt;
import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.U;

/**
 * Created by Marci on 2014.05.07..
 */
public class NoteListFragment extends Fragment {

    public static final String TAG = "NoteListFragment";
    private ListView mNoteList;
    private ArrayAdapter<String> adapter;

    protected View root;

    public static NoteListFragment newInstance() {
        NoteListFragment fragment = new NoteListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NoteListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notelist, container, false);

        mNoteList = (ListView) root.findViewById(R.id.noteListView);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        mNoteList.setAdapter(adapter);

        mNoteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = adapter.getItem(position)+".txt";

                Intent intent = new Intent(getActivity(), NoteActivity.class);
                intent.putExtra("NOTE_FILE_NAME", filename);
                startActivity(intent);

            }
        });

        refreshNoteList();

        return root;

    }
    private void refreshNoteList() {

        File sd = Environment.getExternalStorageDirectory();
        File searchDir = new File(sd, new File(U.C.APP_FOLDER, U.C.NOTES_FOLDER).getPath());
        boolean result = searchDir.mkdirs(); // első indulásnál jön létre

        String[] mFileNames = searchDir.list(new FileExtensionFilter_txt());
        if (mFileNames == null) mFileNames = new String[0];
        Arrays.sort(mFileNames);

        adapter.clear();
        adapter.setNotifyOnChange(false);
        for (String s : mFileNames) {
            adapter.add(s.substring(0,s.length()-4));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshNoteList();
    }
}
