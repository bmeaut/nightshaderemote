package hu.bme.aut.nightshaderemote.ui.notes;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    File txtFile;
    EditText noteText;

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

        Spinner spinner = (Spinner) root.findViewById(R.id.noteSelector);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        spinner.setAdapter(adapter);
        noteText =((EditText) root.findViewById(R.id.noteText));
        Button save = ((Button) root.findViewById(R.id.save));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile(txtFile,noteText.getText().toString());
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String filename = adapter.getItem(position)+".txt";
                txtFile = openTXTFile(filename);
                String text = readFile(txtFile);
                noteText.setText(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

    /**
     * Kapott fájlnév alapján megnyitja azt, a "NightshadeRemote/notes" mappából
     * @param sFileName A megnyitandó fájl neve
     * @return Visszatér a kívánt File típusú objektummal
     */
    public File openTXTFile(String sFileName) {
        final String APP_FOLDER = "NightshadeRemote";
        final String NOTES_FOLDER = "notes";

        File sd = Environment.getExternalStorageDirectory();
        File searchDir = new File(sd, new File(APP_FOLDER, NOTES_FOLDER).getPath());

        File mFile = new File(searchDir, sFileName);

        return mFile;
    }

    /**
     * Beolvassa a kapot File tartalmát
     * @param file File típusú objkektum, amiből olvasni szereténk
     * @return Visszatér egy Stringgel, ami a File tartalmát hordozza
     */
    public String readFile(File file){

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    /**
     * Felülírja a kívánt fájlt
     * @param file A File amit felül szeretnénk írni
     * @param text A File Új tartalma
     */
    public void saveFile (File file, String text){
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.append(text);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
