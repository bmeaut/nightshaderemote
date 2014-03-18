package hu.bme.aut.nightshaderemote.ui.custombuttons;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.U;
import hu.bme.aut.nightshaderemote.connectivity.Command;
import hu.bme.aut.nightshaderemote.connectivity.ScriptCommand;
import hu.bme.aut.nightshaderemote.connectivity.SendCommand;

/**
 * Created by Marci on 2014.03.15..
 */
public class CustomButtonsFragment extends Fragment {

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

       // planets= getResources().getStringArray(R.array.planetsArray);
        gridView = (GridView) v.findViewById(R.id.customButtonList);

        customButtonsAdapter = new CustomButtonsAdapter();

        gridView.setAdapter(customButtonsAdapter);


        // TODO temporary!
        customButtonsAdapter.add(new CustomButton("constL", "flag constellation_drawing on"));
        customButtonsAdapter.add(new CustomButton("nebula", "flag nebula_names on"));
        customButtonsAdapter.add(new CustomButton("multi", "flag constellation_drawing on\nflag nebula_names on"));


        setHasOptionsMenu(true);
        return v;
    }

    protected void onCustomButtonClicked(CustomButton cubu) {
        Log.d(TAG, "Executing script <" + cubu.getTitle() + ">: " + cubu.getScriptText());
        Toast.makeText(getActivity(), "Executing <" + cubu.getTitle() + ">", Toast.LENGTH_SHORT).show();

        String scriptContent = cubu.getScriptText();
        if (! TextUtils.isEmpty(scriptContent)) {
            Command c = new ScriptCommand(scriptContent);
            new SendCommand(U.getServerAddressPref(), U.getServerPortPref(), new SendCommand.OnCommandSentListener() {
                @Override
                public void onCommandSent(String result) {
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            }).execute(c);
        }
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
        switch (item.getItemId()){
            case R.id.addnewbutton:
                Toast.makeText(getActivity(), "New Button", Toast.LENGTH_SHORT).show();
                customButtonsAdapter.add(new CustomButton("new button", ""));
                return true;
        }
        return super.onOptionsItemSelected(item);
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

            return itemView;
        }
    }
}
