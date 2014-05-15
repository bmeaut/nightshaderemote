package hu.bme.aut.nightshaderemote.ui.notes;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hu.bme.aut.nightshaderemote.R;

/**
 * Created by Marci on 2014.05.10..
 */
public class NewNoteDialogFragment extends DialogFragment {
    public static final String TAG = "NewNoteDialogFragment";

    private final String KEY_TITLE = "title";
    private View root;
    private EditText noteTitle;
    private Button ok;
    private Button cancel;
    private NoteAddedListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getTargetFragment() != null) {
            try {
                listener = (NoteAddedListener) getTargetFragment();
            } catch (ClassCastException ce) {
                Log.e(TAG, "Target Fragment does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.dialog_fragment_newnote, container, false);

        noteTitle = (EditText) root.findViewById(R.id.noteTitle);
        ok = (Button)root.findViewById(R.id.ok);
        cancel = (Button)root.findViewById(R.id.cancel);

        getDialog().setTitle(getString(R.string.title_newnote));

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(KEY_TITLE)){
                noteTitle.setText(savedInstanceState.getString(KEY_TITLE));
            }
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileName = noteTitle.getText().toString();
                if(TextUtils.isEmpty(fileName)){
                    Toast.makeText(getActivity(),getResources().getString(R.string.error_entername),Toast.LENGTH_SHORT).show();
                }else {
                    if (listener != null) {
                        listener.onNoteAdded(fileName);
                    }
                    dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE, noteTitle.getText().toString());
    }

    public interface NoteAddedListener {
        void onNoteAdded(String title);
    }
}
