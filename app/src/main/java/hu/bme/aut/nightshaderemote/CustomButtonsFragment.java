package hu.bme.aut.nightshaderemote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Marci on 2014.03.15..
 */
public class CustomButtonsFragment extends Fragment {

    public static final String TAG = "CustomButtonsFragment";
    private GridView mCustomButtonList;
    //String [] planets;
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

       // planets= getResources().getStringArray(R.array.planetsArray);
        mCustomButtonList = (GridView) v.findViewById(R.id.customButtonList);

        customButtonsList.add(new CustomButton("első", "az első gombhoz tartozó script"));
        customButtonsList.add(new CustomButton("második", "a második gombhoz tartozó script"));
        customButtonsList.add(new CustomButton("harmadik", "a harmadik gombhoz tartozó script"));


        refreshList();

        setHasOptionsMenu(true);
        return v;
    }

    private void refreshList() {

       // ArrayAdapter<String> scriptAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, planets);
       // mCustomButtonList.setAdapter(scriptAdapter);
        mCustomButtonList.setAdapter(customButtonsAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
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
                Toast.makeText(getActivity(), "New Button", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
