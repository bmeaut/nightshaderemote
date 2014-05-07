package hu.bme.aut.nightshaderemote.ui.notes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.aut.nightshaderemote.R;

/**
 * Created by Marci on 2014.05.07..
 */
public class NoteFragment extends Fragment {

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_note,container,false);

        return root;
    }
}
