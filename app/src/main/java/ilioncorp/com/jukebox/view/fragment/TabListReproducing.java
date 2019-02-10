package ilioncorp.com.jukebox.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.ReproductionListDAO;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.view.adapter.ReproductionListAdapter;

import java.util.ArrayList;
import java.util.Collections;

@SuppressLint("ValidFragment")
public class TabListReproducing extends Fragment implements Handler.Callback{

    public static Handler bridge;
    public static RecyclerView listReproductionBar;
    private ReproductionListDAO songDAO;
    public static ArrayList<ReproductionListVO> listSongs;
    private String idBar;
    private ReproductionListAdapter adapter;
    private android.widget.TextView tvNoSongReproducing;

    public TabListReproducing(String idBar) {
        this.idBar = idBar;
        bridge = new Handler(this);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_reproducing, container, false);
        this.listReproductionBar = rootView.findViewById(R.id.listReproductionBar);
        this.tvNoSongReproducing= rootView.findViewById(R.id.tvNoSongReproducing);
        tvNoSongReproducing.setVisibility(View.VISIBLE);
        listReproductionBar.setVisibility(View.INVISIBLE);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        songDAO = new ReproductionListDAO(idBar,bridge,true);
        adapter = new ReproductionListAdapter(listSongs,getContext(),songDAO);
        listReproductionBar.setAdapter(adapter);
    }

    @Override
    public boolean handleMessage(Message message) {
        listSongs = (ArrayList<ReproductionListVO>) message.obj;
        if(listSongs.size()>0) {
            listReproductionBar.scrollToPosition(0);
            listReproductionBar.setHasFixedSize(true);
            listReproductionBar.setLayoutManager(new LinearLayoutManager(getContext()));
            listReproductionBar.setVisibility(View.VISIBLE);
            tvNoSongReproducing.setVisibility(View.INVISIBLE);
            Collections.sort(listSongs);
            Collections.sort(listSongs, (ReproductionListVO p1, ReproductionListVO p2) -> new Integer(p2.getLikes()).compareTo(p1.getLikes()));

        }else{
            listReproductionBar.setVisibility(View.INVISIBLE);
            tvNoSongReproducing.setVisibility(View.VISIBLE);
        }
        adapter.setListSongs(listSongs);
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
