package hu.bme.aut.nightshaderemote.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.nightshaderemote.R;
import hu.bme.aut.nightshaderemote.connectivity.CommandHandler;
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
        mSectionsPagerAdapter = new SectionsPagerAdapter(this);

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
                //TODO Kedves Ákos! Ide jön a resetelő metódus ! :)
                Toast.makeText(this,"Rátenyereltél a Resetre",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        private final MainActivity mActivity;
        private List<PageInfo> fragmentList;

        public SectionsPagerAdapter(MainActivity activity) {
            super(activity.getSupportFragmentManager());
            this.mActivity = activity;
            this.fragmentList = new ArrayList<>();
            addPage(ButtonsFragment.class, null, getString(R.string.title_buttons));
            addPage(ScriptsFragment.class, null, getString(R.string.title_scripts));
            addPage(CustomButtonsFragment.class, null, getString(R.string.title_custombuttons));
            addPage(NoteListFragment.class, null, getString(R.string.title_notes));
        }

        public void addPage(Class<? extends Fragment> fragmentClass, Bundle args, String title) {
            final PageInfo pi = new PageInfo(fragmentClass, args, title);
            fragmentList.add(pi);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            final PageInfo pi = fragmentList.get(position);
            return Fragment.instantiate(mActivity, pi.fragmentClass.getName(), pi.args);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void onPageSelected(int position) {
            mActivity.setTitle(fragmentList.get(position).title);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}

        private class PageInfo {
            public final Class<? extends Fragment> fragmentClass;
            public final Bundle args;
            public final String title;

            private PageInfo(Class<? extends Fragment> fragmentClass, Bundle args, String title) {
                this.fragmentClass = fragmentClass;
                this.args = args;
                this.title = title;
            }
        }
    }
}
