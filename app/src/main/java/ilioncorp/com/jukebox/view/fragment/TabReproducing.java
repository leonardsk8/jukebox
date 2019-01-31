package ilioncorp.com.jukebox.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.ReproductionListDAO;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.model.dto.UserLikeVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.activity.BarActivity;
import ilioncorp.com.jukebox.view.adapter.ReproductionListAdapter;

@SuppressLint("ValidFragment")
public class TabReproducing extends Fragment {


    private TabReproducing.SectionsPagerAdapter mSectionsPagerAdapter;
    TabLayout tabReproducing;
    private ViewPager mViewPager;


    private EstablishmentVO establishmentVO;

    public TabReproducing(EstablishmentVO establishmentVO) {
        this.establishmentVO = establishmentVO;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_tabs_reproducing, container, false);
        mSectionsPagerAdapter = new TabReproducing.SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = rootView.findViewById(R.id.container_reproducing);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabReproducing = rootView.findViewById(R.id.tabsReproducing);
        tabReproducing.setupWithViewPager(mViewPager);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public TabBar bar;
        private List<Fragment> mFragmentList;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentList = new ArrayList<>();
            TabListReproducing songs = new TabListReproducing(establishmentVO.getId());
            TabHistorySongs history = new TabHistorySongs(true,establishmentVO.getId());
            mFragmentList.add(songs);
            mFragmentList.add(history);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0:
                    return "Reproduciendo";
                case 1:
                    return "Canciones Enviadas";
            }
            return null;
        }
    }
}
