package hu.bme.aut.nightshaderemote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marci on 2014.03.17..
 */
public class CustomButtonsAdapter extends BaseAdapter {
    private final List<CustomButton> customButtons;

    public CustomButtonsAdapter(final Context context, final ArrayList<CustomButton> aCustomButtons){
        customButtons = aCustomButtons;
    }

    @Override
    public int getCount() {
        return customButtons.size();
    }

    @Override
    public Object getItem(int position) {
        return customButtons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CustomButton customButton = customButtons.get(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_cutombutton, null);
        Button button = (Button) itemView.findViewById(R.id.button);
        button.setText(customButton.getTitle());
        return itemView;
    }
}
