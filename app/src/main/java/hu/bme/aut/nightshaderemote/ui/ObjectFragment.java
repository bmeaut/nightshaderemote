package hu.bme.aut.nightshaderemote.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.aut.nightshaderemote.R;

/**
 * @author Ákos Pap
 */
public class ObjectFragment extends Fragment {

    public static final String TAG = "ObjectFragment";

    protected View root;

    public static ObjectFragment newInstance() {
        ObjectFragment fragment = new ObjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_object, container, false);



        return root;
    }
}
