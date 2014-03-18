package hu.bme.aut.nightshaderemote;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Marci on 2014.03.15..
 */
public class CustomButtonsFragment extends Fragment {

    public int counter = 0; // ideiglenes, teszteléshez

    public static final String TAG = "CustomButtonsFragment";
    private GridView mCustomButtonList;
    ArrayList<CustomButton> customButtonsList = new ArrayList<CustomButton>();
    CustomButtonsAdapter customButtonsAdapter = new CustomButtonsAdapter(this.getActivity(),customButtonsList);


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

        mCustomButtonList = (GridView) v.findViewById(R.id.customButtonList);

        refreshCustomButtons();

        setHasOptionsMenu(true);
        return v;
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
        customButtonsList.clear();

        // végigfut a tömbön és létrehozza custom button objektumokat
        for (int i = 0; i < files.length; i++ ){

            // levágja a fájl kiterjesztést
            customButtonsList.add(new CustomButton(files[i].getName().substring(0,files[i].getName().length()-4), files[i].toString()));
        }

        // megadja a GridView-nak az adaptert
        mCustomButtonList.setAdapter(customButtonsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCustomButtons();
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
        switch (item.getItemId()){
            case R.id.addnewbutton:
                counter +=1;        // ideiglenes, teszteléshez
                CreateSTSFile("New".concat(String.valueOf(counter)).concat(".sts"),"tartalom");
                refreshCustomButtons();
                //Toast.makeText(getActivity(), "New Button", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void CreateSTSFile(String sFileName, String sBody){
        try
        {
            final String APP_FOLDER = "NightshadeRemote";
            final String CUSTOM_BUTTONS_FOLDER = "custom_buttons";

            File sd = Environment.getExternalStorageDirectory();
            File searchDir = new File(sd, new File(APP_FOLDER, CUSTOM_BUTTONS_FOLDER).getPath());

            File stsFile = new File(searchDir, sFileName);
            FileWriter writer = new FileWriter(stsFile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
