package ilioncorp.com.jukebox.view.activity;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.CreditsDAO;
import ilioncorp.com.jukebox.model.dao.SessionDAO;
import ilioncorp.com.jukebox.model.dto.CreditsVO;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.fragment.TabBar;
import ilioncorp.com.jukebox.view.fragment.TabInfoBar;
import ilioncorp.com.jukebox.view.fragment.TabPromotions;
import ilioncorp.com.jukebox.view.fragment.TabReproducing;
import ilioncorp.com.jukebox.view.fragment.TabUsers;
import ilioncorp.com.jukebox.view.fragment.TabYoutube;
import ilioncorp.com.jukebox.view.generic.GenericActivity;

public class BarActivity extends GenericActivity implements View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener
        ,Handler.Callback{
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
        private Handler bridge;
        private String answer;
        private View view;



        /**
         * The {@link ViewPager} that will host the section contents.
         */
    private ViewPager mViewPager;
    private EstablishmentVO establishment;



    private com.github.clans.fab.FloatingActionMenu fabMenu;
    private com.github.clans.fab.FloatingActionButton subFabOpenSesion;
    private com.github.clans.fab.FloatingActionButton subCredits;
    private com.github.clans.fab.FloatingActionButton subMaps;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bar);
        this.subFabOpenSesion = findViewById(R.id.subFabOpenSesion);
        this.subCredits= findViewById(R.id.subCredits);
        this.subMaps= findViewById(R.id.subMapa);
        this.fabMenu = findViewById(R.id.fabMenu);
        establishment = (EstablishmentVO) getIntent().getExtras().getSerializable("establishment");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container_bar);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tab = findViewById(R.id.tabs);
        answer="";
        tab.setupWithViewPager(mViewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bridge = new Handler(this);
        fabMenu.setClosedOnTouchOutside(true);
        Constantes.idBar = String.valueOf(establishment.getId());
        subFabOpenSesion.setOnClickListener(this::onClick);
        subCredits.setOnClickListener(this::onClick);
        subMaps.setOnClickListener(this::onClick);
        generateSession(view,"yes");
        }




    private void generateSession(View view,String onlyCheck) {
            SessionDAO session = new SessionDAO();
            session.checkSession(bridge,establishment.getId(),onlyCheck);
            this.view = view;
            showCharging("cargando");
    }


    @Override
        public boolean handleMessage(Message message) {
            hideCharging();
            String[] array = (String[]) message.obj;
            String option = array[1];
            answer = array[0];
            if(option.equals("no")) {
                SessionDAO session = new SessionDAO();
                if (answer.contains("inactive")) {
                    session.generatedSession(establishment.getId());
                    Constantes.establishmentVOActual = establishment;
                    messageSnackBar("Sesión generada con el bar", view);

                } else if (answer.contains("vetoed")) {
                    messageSnackBar("Te encuentras Vetado de este bar", view);


                } else if (answer.contains("active")) {

                    session.closeSession(establishment.getId());
                    Constantes.establishmentVOActual = null;
                    messageSnackBar("Sesión cerrada", view);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        subFabOpenSesion.setImageIcon(Icon.createWithResource(this, R.drawable.open_sesion));
                    }else
                    subFabOpenSesion.setImageResource(R.drawable.open_sesion);
                    subFabOpenSesion.setLabelText("Iniciar Sesión");

                } else if (answer.contains("")) {
                    session.closeSession(Constantes.idBarSessionActual);
                    Constantes.establishmentVOActual = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        subFabOpenSesion.setImageIcon(Icon.createWithResource(this, R.drawable.close_sesion));
                    else
                    subFabOpenSesion.setImageResource(R.drawable.close_sesion);
                    session.generatedSession(establishment.getId());
                    Constantes.establishmentVOActual = establishment;
                    messageSnackBar("Sesión generada con el bar", view);
                    subFabOpenSesion.setLabelText("Cerrar Sesión");
                }
            }else{
                if (answer.contains("active")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        subFabOpenSesion.setImageIcon(Icon.createWithResource(this, R.drawable.close_sesion));
                    else
                        subFabOpenSesion.setImageResource(R.drawable.close_sesion);
                subFabOpenSesion.setLabelText("Cerrar Sesión");
                }

            }
            return false;
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
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Handler call = new Handler(message -> {
                hideCharging();
                fabMenu.setVisibility(View.VISIBLE);
                switch (message.arg1){
                    case 0:
                        mSectionsPagerAdapter.bar.changeFragment(new TabInfoBar(establishment));
                        break;
                    case 1:
                        mSectionsPagerAdapter.bar.changeFragment(new TabUsers(establishment.getId()));
                        break;
                    case 2:
                        fabMenu.setVisibility(View.INVISIBLE);
                        mSectionsPagerAdapter.bar.changeFragment(new TabPromotions(establishment.getId()));
                        break;
                }

                return false;
            });
            showCharging("Loading");
            mViewPager.setCurrentItem(0);
            fabMenu.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.subFabOpenSesion:
                generateSession(view,"no");
                break;
            case R.id.subCredits:
                checkCredits();
                break;
            case R.id.subMapa:
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+establishment.getLatitude()+","
                        +establishment.getLenght());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;

        }

    }

    private void checkCredits() {
        Handler bridge = new Handler(message -> {
            CreditsVO vo = (CreditsVO) message.obj;
            Toast.makeText(this,"Creditos: "+vo.getCredits(),Toast.LENGTH_SHORT).show();
            return false;
        });
        new CreditsDAO(bridge,establishment.getId(), Constantes.userActive.getUserUID());

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
                TabReproducing songs = new TabReproducing(establishment);
                bar = new TabBar(establishment);
                mFragmentList.add(bar);
                mFragmentList.add(songs);
                mFragmentList.add(youtube);
            }

            @Override
            public Fragment getItem(int position) {
                fabMenu.setVisibility(View.VISIBLE);
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
                        return "Bar";
                    case 1:
                        return "Reproduciendo";
                    case 2:
                        return "Buscar";
                }
                return null;
            }
        }
}
