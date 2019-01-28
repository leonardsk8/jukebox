package ilioncorp.com.jukebox.view.activity;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.view.fragment.TabHistoryLoads;
import ilioncorp.com.jukebox.view.fragment.TabHistorySongs;
import ilioncorp.com.jukebox.view.generic.GenericActivity;

public class HistoryActivity extends GenericActivity implements Handler.Callback,View.OnClickListener,Runnable {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container_history);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tab = findViewById(R.id.tabsHistory);
        tab.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void run() {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentList = new ArrayList<>();
            TabHistorySongs tab = new TabHistorySongs();
            TabHistoryLoads tab2 = new TabHistoryLoads();
            mFragmentList.add(tab);
            mFragmentList.add(tab2);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mFragmentList.size();
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Canciones";
                case 1:
                    return "Recargas";
            }
            return null;
        }
    }

}
