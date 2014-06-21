package hu.bme.aut.nightshaderemote.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import hu.bme.aut.nightshaderemote.R;

public class CreditsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        String plain = getString(R.string.credits_text);

        Spanned html = Html.fromHtml(plain);

        ((TextView) findViewById(R.id.credits_text)).setText(html);
    }
}
