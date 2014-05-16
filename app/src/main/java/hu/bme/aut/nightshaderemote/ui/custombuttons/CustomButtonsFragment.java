package hu.bme.aut.nightshaderemote.ui.custombuttons;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hu.bme.aut.nightshaderemote.FileExtensionFilter_sts;
import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.U;
import hu.bme.aut.nightshaderemote.connectivity.CommandHandler;
import hu.bme.aut.nightshaderemote.connectivity.commands.Command;
import hu.bme.aut.nightshaderemote.connectivity.commands.ExecuteCommand;

/**
 * Az "SD_CARD/NightShadeRemote/custom_buttons" mappában található ".sts" kiterjesztésű fájlokat beolvassa.
 * A megfelelő megjelenítés érdekében ezeknek UTF-8 karakterkódolásúnak kell lenni.
 * A fájlokból listát készít és a nevüket megjeleníti egy GridView-ban Gombok formájában. Az egyes gombokat megnyomva
 * az ".sts" kiterjesztésű fájlok tartalam scriptként futtatásra kerül a szerveren
 * Toolbar-on lévő "Add" gobbal létrehozhatunk új CustomButton-okat
 * A gombokat hosszan nyomva tartva a Context menüben a "Edit" és "Delete" lehetőség érhető el.
 * A Context menübe nem a GridView van beregisztálva, hanem az egyes CustomButton-ok
 * A "Delete" az adott gombhoz tartozó fájlt törli a "custom_buttons" mappából és frissíti a GridView-t
 * "Edit" és az "Add" (Toolbar-on) hatására a CustomButtonsDialogFragment dialógus "ablak" jelenik meg.
 *
 * Created by Marci on 2014.03.15..
 */
public class CustomButtonsFragment extends Fragment implements CustomButtonDialogFragment.IButtonAddedListener {

    public static final String TAG = "CustomButtonsFragment";
    private GridView gridView;
    private CustomButton clickedButton;
    private CustomButtonsAdapter customButtonsAdapter;



    public static CustomButtonsFragment newInstance() {
        CustomButtonsFragment fragment = new CustomButtonsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CustomButtonsFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_custombuttons, container, false);

        gridView = (GridView) root.findViewById(R.id.customButtonList);
        customButtonsAdapter = new CustomButtonsAdapter();
        gridView.setAdapter(customButtonsAdapter);

        TextView empty = new TextView(getActivity());
        empty.setText(getString(R.string.custombuttons_empty_text));
        gridView.setEmptyView(empty);

        setHasOptionsMenu(true);
        refreshCustomButtons();

        return root;
    }

    protected void onCustomButtonClicked(CustomButton cubu) {
        Log.d(TAG, "Executing script <" + cubu.getTitle() + ">: " + cubu.getScriptText());
        Toast.makeText(getActivity(), "Executing <" + cubu.getTitle() + ">", Toast.LENGTH_SHORT).show();

        String scriptContent = cubu.getScriptText();
        if (!TextUtils.isEmpty(scriptContent)) {
            Command c = new ExecuteCommand(scriptContent);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CommandHandler.createIntent(c));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addnewitem:
                CustomButtonDialogFragment addNewButtonDialog = new CustomButtonDialogFragment();
                Bundle b = new Bundle();
                b.putString("Mode", "NEW");
                addNewButtonDialog.setArguments(b);
                addNewButtonDialog.setTargetFragment(this, 0);
                FragmentManager fm = getFragmentManager();
                addNewButtonDialog.show(fm, CustomButtonDialogFragment.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_custombuttons, menu);
        clickedButton = (CustomButton) v.getTag();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_item:
                File sd = Environment.getExternalStorageDirectory();
                File searchDir = new File(sd, new File(U.C.APP_FOLDER, U.C.CUSTOM_BUTTONS_FOLDER).getPath());
                File stsFile = new File(searchDir, clickedButton.getTitle().concat(".sts"));
                boolean deleted = stsFile.delete();
                refreshCustomButtons();
                return true;

            case R.id.edit_item:
                CustomButtonDialogFragment editButtonDialog = new CustomButtonDialogFragment();
                Bundle b = new Bundle();
                b.putString("Title", clickedButton.getTitle());
                b.putString("Script", clickedButton.getScriptText());
                b.putString("Mode", "EDIT");
                editButtonDialog.setArguments(b);
                editButtonDialog.setTargetFragment(this, 0);
                FragmentManager fm = getFragmentManager();
                editButtonDialog.show(fm, CustomButtonDialogFragment.TAG);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onButtonAdded() {
        refreshCustomButtons();
    }

    /**
     * Frissíti a CustomButton listát a telepített mappából
     */
    private void refreshCustomButtons() {

        File sd = Environment.getExternalStorageDirectory();
        File searchDir = new File(sd, new File(U.C.APP_FOLDER, U.C.CUSTOM_BUTTONS_FOLDER).getPath());

        // első indulásnál létrehozza a mappát ha még nem létezett
        boolean result = searchDir.mkdirs();
        // beolvassa az sts fájlokat egy tömbbe
        File[] files = searchDir.listFiles(new FileExtensionFilter_sts());
        // kiüríti a customButtonList tömböt, hogy újraépíthesse, így nem duplikálódnak az elemek
        customButtonsAdapter.clear();
        // végigfut a tömbön és létrehozza custom button objektumokat
        List<CustomButton> fromDirectory = new ArrayList<>(files.length);
        for (File f : files) {
            StringBuilder scriptContent = new StringBuilder();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(f));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    scriptContent.append(line).append('\n');
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            fromDirectory.add(new CustomButton(f.getName().substring(0, f.getName().length() - 4), scriptContent.toString()));
        }
        customButtonsAdapter.addAll(fromDirectory);
    }

    /**
     * Created by Marci on 2014.03.17..
     */
    public class CustomButtonsAdapter extends BaseAdapter {
        private List<CustomButton> items;

        public CustomButtonsAdapter() {
            items = new ArrayList<>();
        }

        public void add(CustomButton button) {
            items.add(button);
            notifyDataSetChanged();
        }

        public void remove(CustomButton button) {
            items.remove(button);
        }

        public void addAll(Collection<CustomButton> buttons) {
            items.addAll(buttons);
            notifyDataSetChanged();
        }

        public void setItems(List<CustomButton> newItems) {
            items = newItems;
            notifyDataSetChanged();
        }

        public void clear() {
            items.clear();
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
            final CustomButton customButton = items.get(position);

            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.item_custombutton, null);

            Button button = (Button) itemView.findViewById(R.id.button);

            if (customButton.getTitle().length() > 8) {
                button.setText(customButton.getTitle().substring(0, 7) + "...");
            } else {
                button.setText(customButton.getTitle());
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCustomButtonClicked((CustomButton) v.getTag());
                }
            });
            button.setTag(customButton);
            registerForContextMenu(button);

            return itemView;
        }
    }
}
