package ilioncorp.com.jukebox.view.adapter;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ilioncorp.com.jukebox.R;

import ilioncorp.com.jukebox.model.dto.MenuVO;

@SuppressLint("ValidFragment")
public class MenuListAdapter extends Fragment {
    ArrayList<MenuVO> listitems;
    RecyclerView MyRecyclerView;


    public MenuListAdapter(ArrayList<MenuVO> menuList) {
        listitems = menuList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_menu, container, false);
        MyRecyclerView =  view.findViewById(R.id.listMenu);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new AdapterMenu(listitems,getContext()));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);
        return view;
    }
}
