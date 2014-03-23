package hu.bme.aut.nightshaderemote.ui.custombuttons;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hu.bme.aut.nightshaderemote.FileExtensionFilter;
import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.U;
import hu.bme.aut.nightshaderemote.connectivity.Command;
import hu.bme.aut.nightshaderemote.connectivity.ExecuteCommand;
import hu.bme.aut.nightshaderemote.connectivity.SendCommand;

/**
 * Created by Marci on 2014.03.15..
 */
public class CustomButtonsFragment extends Fragment implements AddNewButtonDialogFragment.IButtonAddedListener {

    CustomButton clickedButton; // TODO egyelőre nem tudom szebben megoldani

    public static final String TAG = "CustomButtonsFragment";
    private GridView gridView;
    CustomButtonsAdapter customButtonsAdapter;


    public static CustomButtonsFragment newInstance() {
        CustomButtonsFragment fragment = new CustomButtonsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CustomButtonsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_custombuttons, container, false);

        gridView = (GridView) v.findViewById(R.id.customButtonList);
        customButtonsAdapter = new CustomButtonsAdapter();
        gridView.setAdapter(customButtonsAdapter);

        refreshCustomButtons();


        setHasOptionsMenu(true);
        return v;
    }

    protected void onCustomButtonClicked(CustomButton cubu) {
        Log.d(TAG, "Executing script <" + cubu.getTitle() + ">: " + cubu.getScriptText());
        Toast.makeText(getActivity(), "Executing <" + cubu.getTitle() + ">", Toast.LENGTH_SHORT).show();

        String scriptContent = cubu.getScriptText();
        if (!TextUtils.isEmpty(scriptContent)) {
            Command c = new ExecuteCommand(scriptContent);
            new SendCommand(U.getServerAddressPref(), U.getServerPortPref(), new SendCommand.OnCommandSentListener() {
                @Override
                public void onCommandSent(String result) {
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            }).execute(c);
        }
    }

    private void refreshCustomButtons() {

        final String APP_FOLDER = "NightshadeRemote";
        final String CUSTOM_BUTTONS_FOLDER = "custom_buttons";

        File sd = Environment.getExternalStorageDirectory();
        File searchDir = new File(sd, new File(APP_FOLDER, CUSTOM_BUTTONS_FOLDER).getPath());

        // első indulásnál létrehozza a mappát ha még nem létezett
        boolean result = searchDir.mkdirs();

        // beolvassa az sts fájlokat egy tömbbe
        File[] files = searchDir.listFiles(new FileExtensionFilter());

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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_custombuttons, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.addnewbutton:
                //a new button gomb megnyomására létrejön egy dialódus fragment
                AddNewButtonDialogFragment addNewButtonDialog = new AddNewButtonDialogFragment();
                addNewButtonDialog.setTargetFragment(this, 0);
                FragmentManager fm = getFragmentManager();
                addNewButtonDialog.show(fm, AddNewButtonDialogFragment.TAG);
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
                final String APP_FOLDER = "NightshadeRemote";
                final String CUSTOM_BUTTONS_FOLDER = "custom_buttons";

                File sd = Environment.getExternalStorageDirectory();
                File searchDir = new File(sd, new File(APP_FOLDER, CUSTOM_BUTTONS_FOLDER).getPath());
                File stsFile = new File(searchDir, clickedButton.getTitle().concat(".sts")); // TODO ezzel így csak kisbetűs sts kiterjesztésű fájt lehet törölni
                boolean deleted = stsFile.delete();
                refreshCustomButtons();
                //Toast.makeText(getActivity(), clickedButton.getTitle(), Toast.LENGTH_SHORT).show(); //TODO teszteléshez

                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onButtonAdded() {
        refreshCustomButtons();
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
            View itemView = inflater.inflate(R.layout.item_cutombutton, null);

            Button button = (Button) itemView.findViewById(R.id.button);
            button.setText(customButton.getTitle());
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
