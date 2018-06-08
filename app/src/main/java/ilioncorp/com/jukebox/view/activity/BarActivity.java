package ilioncorp.com.jukebox.view.activity;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.fragment.TabBar;
import ilioncorp.com.jukebox.view.fragment.TabInfoBar;
import ilioncorp.com.jukebox.view.fragment.TabPromotions;
import ilioncorp.com.jukebox.view.fragment.TabReproducing;
import ilioncorp.com.jukebox.view.fragment.TabUsers;
import ilioncorp.com.jukebox.view.fragment.TabYoutube;
import ilioncorp.com.jukebox.view.generic.GenericActivity;

public class BarActivity extends GenericActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    TabLayout tab;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private EstablishmentVO establishment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        establishment = (EstablishmentVO) getIntent().getExtras().getSerializable("establishment");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tab = findViewById(R.id.tabs);
        tab.setupWithViewPager(mViewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

      /*  FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sesión Generada con el bar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
      */
        Constantes.idBar = String.valueOf(establishment.getId());
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

   /* @Override
    public void onBackPressed() {
        *//*if (TabBar.fm.getBackStackEntryCount() > 1 ) {
            TabBar.fm.popBackStack();
        }
        else
            super.onBackPressed();*//*
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Handler call = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                hideCharging();
                switch (message.arg1){
                    case 0:
                        mSectionsPagerAdapter.bar.changeFragment(new TabInfoBar(establishment));
                        break;
                    case 1:
                        mSectionsPagerAdapter.bar.changeFragment(new TabUsers());
                        break;
                    case 2:
                        mSectionsPagerAdapter.bar.changeFragment(new TabPromotions());
                        break;
                }

                return false;
            }
        });
        showCharging("Loading");
        mViewPager.setCurrentItem(2);
        switch (item.getItemId()) {
            case R.id.action_information:
                mSectionsPagerAdapter.bar.startFragment(call,0);
                break;
            case R.id.action_users:
                mSectionsPagerAdapter.bar.startFragment(call,1);
                break;
            case R.id.action_promotions:
                mSectionsPagerAdapter.bar.startFragment(call,2);
                break;


        }
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public TabBar bar;
        private List<Fragment> mFragmentList;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentList = new ArrayList<>();
            TabYoutube youtube = new TabYoutube();
            TabReproducing songs = new TabReproducing(String.valueOf(establishment.getId()));
            bar = new TabBar(establishment);
            mFragmentList.add(youtube);
            mFragmentList.add(songs);
            mFragmentList.add(bar);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Buscar";
                case 1:
                    return "Reproduciendo";
                case 2:
                    return "Bar";
            }
            return null;
        }
    }
}
