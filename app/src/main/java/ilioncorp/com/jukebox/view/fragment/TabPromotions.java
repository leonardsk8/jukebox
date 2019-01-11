package ilioncorp.com.jukebox.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.PromotionsVO;
import ilioncorp.com.jukebox.view.adapter.PromotionsListAdapter;
import ilioncorp.com.jukebox.view.generic.GenericFragment;

@SuppressLint("ValidFragment")
public class TabPromotions extends GenericFragment {


    private String idBar;
    public TabPromotions(String idBar) {
        this.idBar = idBar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_promotions, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragmentPromotions;
        fragmentPromotions = new PromotionsListAdapter(idBar);
        fm.beginTransaction().add(R.id.fragmentPromotions, fragmentPromotions).commit();

    }

}
