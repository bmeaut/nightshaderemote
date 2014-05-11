package hu.bme.aut.nightshaderemote.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.connectivity.CommandHandler;
import hu.bme.aut.nightshaderemote.connectivity.commands.Command;
import hu.bme.aut.nightshaderemote.connectivity.commands.ControlCommand;
import hu.bme.aut.nightshaderemote.connectivity.models.JResponse;
import hu.bme.aut.nightshaderemote.ui.custombuttons.CustomButtonsFragment;
import hu.bme.aut.nightshaderemote.ui.notes.NoteListFragment;

public class MainActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(mSectionsPagerAdapter);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra(CommandHandler.KEY_RESPONSE)) {
                    JResponse response = ((JResponse) intent.getSerializableExtra(CommandHandler.KEY_RESPONSE));
                    Toast.makeText(
                            context,
                            String.format("Response arrived!\nText: %s",
                                    response.getResponse()
                            ),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }, CommandHandler.INTENT_FILTER_RESPONSE_ARRIVED);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra(CommandHandler.KEY_MESSAGE)) {
                    Toast.makeText(context, intent.getStringExtra(CommandHandler.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                }
            }
        }, CommandHandler.INTENT_FILTER_NO_RESPONSE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_preferences:
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            case R.id.action_reset:
                Command c = new ControlCommand(ControlCommand.CommandName.RESET);
                LocalBroadcastManager.getInstance(this).sendBroadcast(CommandHandler.createIntent(c));
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        private List<Fragment> fragmentList;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
            fragmentList.add(ButtonsFragment.newInstance());
            fragmentList.add(ScriptsFragment.newInstance());
            fragmentList.add(CustomButtonsFragment.newInstance());
            fragmentList.add(ObjectFragment.newInstance());
            fragmentList.add(NoteListFragment.newInstance());
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return fragmentList.size();
        }

        @Override
        public void onPageSelected(int position) {
            final InputMethodManager inputMethodManager =
                    (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);

            if (fragmentList.get(position).getClass() != ObjectFragment.class) {
                inputMethodManager.hideSoftInputFromWindow(MainActivity.this.mViewPager.getApplicationWindowToken(), 0);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    }
}
