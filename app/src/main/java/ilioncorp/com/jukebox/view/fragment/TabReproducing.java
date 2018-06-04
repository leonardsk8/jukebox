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

import java.util.ArrayList;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.ReproductionListDAO;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.view.adapter.ReproductionListAdapter;

@SuppressLint("ValidFragment")
public class TabReproducing extends Fragment implements Handler.Callback{

    public static Handler bridge;
    private android.support.v7.widget.RecyclerView listReproductionBar;
    private ReproductionListDAO songDAO;
    private ArrayList<ReproductionListVO> listSongs;
    private String idBar;
    private ReproductionListAdapter adapter;

    public TabReproducing(String idBar) {
        this.idBar = idBar;
        bridge = new Handler(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_reproducing, container, false);
        this.listReproductionBar = rootView.findViewById(R.id.listReproductionBar);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        songDAO = new ReproductionListDAO("",idBar,bridge,true);
    }

    @Override
    public boolean handleMessage(Message message) {
        listSongs = (ArrayList<ReproductionListVO>) message.obj;
        listReproductionBar.setHasFixedSize(true);
        listReproductionBar.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReproductionListAdapter(listSongs,getContext());
        listReproductionBar.setAdapter(adapter);
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        songDAO.stopListener();
    }
}
