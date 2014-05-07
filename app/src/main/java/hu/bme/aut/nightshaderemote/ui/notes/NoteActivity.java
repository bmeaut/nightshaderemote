package hu.bme.aut.nightshaderemote.ui.notes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import hu.bme.aut.nightshaderemote.R;

public class NoteActivity extends ActionBarActivity {

    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        filename = getIntent().getExtras().getString("NOTE_FILE_NAME");

        Toast.makeText(this,filename,Toast.LENGTH_LONG).show();
    }
}
