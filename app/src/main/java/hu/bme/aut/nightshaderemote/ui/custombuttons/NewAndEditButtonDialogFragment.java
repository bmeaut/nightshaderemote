package hu.bme.aut.nightshaderemote.ui.custombuttons;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import hu.bme.aut.nightshaderemote.R;

/**
 * Created by Marci on 2014.03.20..
 */
public class NewAndEditButtonDialogFragment extends DialogFragment {
    public static final String TAG = "NewAndEditButtonDialogFragment";

    private EditText fileNameText;
    private EditText scriptText;
    private String initialFileName = "";
    private String initialScript = "";
    private IButtonAddedListener listener;




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getTargetFragment() != null) {
            try {
                listener = (IButtonAddedListener) getTargetFragment();
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Target Fragment does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_fragment_newbutton, container, false);

        fileNameText = (EditText) root.findViewById(R.id.filename);
        scriptText = (EditText) root.findViewById(R.id.script);

        // Beállítja a kezdeti értékeket

        fileNameText.setText(initialFileName);
        scriptText.setText(initialScript);


        root.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = fileNameText.getText().toString().concat(".sts");
                String script = scriptText.getText().toString();

                createSTSFile(fileName, script);
                if (listener != null) {
                    listener.onButtonAdded();
                }
                dismiss();
            }
        });

        root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return root;
    }

    public void createSTSFile(String sFileName, String sBody) {
        final String APP_FOLDER = "NightshadeRemote";
        final String CUSTOM_BUTTONS_FOLDER = "custom_buttons";

        File sd = Environment.getExternalStorageDirectory();
        File searchDir = new File(sd, new File(APP_FOLDER, CUSTOM_BUTTONS_FOLDER).getPath());

        File stsFile = new File(searchDir, sFileName);
        FileWriter writer = null;
        try {
            writer = new FileWriter(stsFile);
            writer.append(sBody);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                // wtf
            }
        }
    }
    public void setInitialValues (String Title, String Script){

        initialFileName = Title;
        initialScript = Script;
    }

    //interface
    public interface IButtonAddedListener {
        void onButtonAdded();
    }
}
