package hu.bme.aut.nightshaderemote.ui.notes;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hu.bme.aut.nightshaderemote.FileExtensionFilter_txt;
import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.U;

/**
 * Az "SD_CARD/NightShadeRemote/notes" mappában található ".txt" kiterjesztésű fájlokat beolvassa.
 * A megfelelő megjelenítés érdekében ezeknek UTF-8 karakterkódolásúnak kell lenni.
 * A fájlokból listát készít és a nevüket megjeleníti egy spinnerben. Az egyes elemeket kiválazstva
 * a fájlok tartalmát, megjeleníti egy scrollozható EditView-ban amit szerkeszteni is lehet.
 * Az EditView-ban történő változás hatására a txt fájl is mentődik.
 * Toolbar-on lévő "Add" gobbal létrehozhatunk új note-ot, "Delete" gombbal kitörölhetjük a kiválasztott elemet.
 *
 * Created by Marci on 2014.05.07..
 */
public class NoteListFragment extends Fragment implements NewNoteDialogFragment.NoteAddedListener {

    public static final String TAG = "NoteListFragment";
    private NotesAdapter adapter;
    private Note selectedNote;
    private View root;
    EditText noteText;
    Spinner spinner;

    TextView empty;

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

        spinner = (Spinner) root.findViewById(R.id.noteSelector);
        adapter = new NotesAdapter();
        spinner.setAdapter(adapter);
        noteText =((EditText) root.findViewById(R.id.noteText));
        empty = (TextView) root.findViewById(R.id.emptyText);

        setHasOptionsMenu(true);

        noteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (selectedNote != null) {
                    selectedNote.setContent(noteText.getText().toString());
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedNote = (Note) adapter.getItem(position);
                displayNote();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedNote = null;
                displayNote();
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_new, menu);
        inflater.inflate(R.menu.menu_delete, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addnewitem:
                NewNoteDialogFragment newNoteDialogFragment = new NewNoteDialogFragment();
                FragmentManager fm = getFragmentManager();
                newNoteDialogFragment.setTargetFragment(this, 0);
                newNoteDialogFragment.show(fm, NewNoteDialogFragment.TAG);
                return true;
            case R.id.deleteselecteditem:
                if (selectedNote != null) {
                    selectedNote.delete();
                    adapter.remove(selectedNote);
                }

                if (adapter.getCount() > 0) {
                    spinner.setSelection(0);
                    selectedNote = (Note) spinner.getSelectedItem();
                } else {
                    selectedNote = null;
                }

                displayNote();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshNoteList();
    }

    @Override
    public void onNoteAdded(String title) {
        File f = createTXTFile(title + ".txt");
        Note note = new Note(f);
        adapter.add(note);
        spinner.setSelection(adapter.getPosition(note), false);

        displayNote();
    }

    /**
     * Frissíti a Note listát
     */
    private void refreshNoteList() {

        File sd = Environment.getExternalStorageDirectory();
        File searchDir = new File(sd, new File(U.C.APP_FOLDER, U.C.NOTES_FOLDER).getPath());
        boolean result = searchDir.mkdirs(); // első indulásnál jön létre

        File[] mFiles = searchDir.listFiles(new FileExtensionFilter_txt());
        if (mFiles != null && mFiles.length > 0) {

            adapter.clear();

            List<Note> notes = new ArrayList<>(mFiles.length);
            for (File f : mFiles) {
                notes.add(new Note(f));
            }

            adapter.addAll(notes);

            selectedNote = (Note) adapter.getItem(0);
        } else {
            selectedNote = null;
        }

        displayNote();

    }

    /**
     * Megjeleníti a spinnerben kiválasztott file tartalmát egy EditText-ben
     */
    private void displayNote() {
        if (selectedNote != null) {
            spinner.setVisibility(View.VISIBLE);
            noteText.setVisibility(View.VISIBLE);
            noteText.setText(selectedNote.getContent());
            empty.setVisibility(View.GONE);
        } else {
            spinner.setVisibility(View.GONE);
            noteText.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
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
     * Létrehoz egy üres txt fájlt a NightShadeRemote/notes mappában
     * @param sFileName A létrehozandó fájl neve
     */
    public File createTXTFile(String sFileName) {
        final String APP_FOLDER = "NightshadeRemote";
        final String CUSTOM_BUTTONS_FOLDER = "notes";

        File sd = Environment.getExternalStorageDirectory();
        File searchDir = new File(sd, new File(APP_FOLDER, CUSTOM_BUTTONS_FOLDER).getPath());

        return new File(searchDir, sFileName);
    }

    protected static class NotesAdapter extends BaseAdapter {

        protected List<Note> items;

        public NotesAdapter() {
            items = new ArrayList<>();
        }

        public void add(Note note) {
            items.add(note);
            notifyDataSetChanged();
        }

        public void addAll(Collection<Note> notes) {
            items.addAll(notes);
            notifyDataSetChanged();
        }

        public void clear() {
            items.clear();
            notifyDataSetChanged();
        }

        public void remove(Note note) {
            items.remove(note);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Note note = items.get(position);

            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
                TextView v = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(v);
            }

            TextView v = (TextView) convertView.getTag();

            v.setText(note.getTitle());

            return convertView;
        }

        public int getPosition(Note note) {
            return items.indexOf(note);
        }
    }
}
