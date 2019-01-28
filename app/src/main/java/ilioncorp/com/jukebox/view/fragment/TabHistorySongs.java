package ilioncorp.com.jukebox.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.HistorySongDAO;
import ilioncorp.com.jukebox.model.dto.HistorySongVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.adapter.HistorySongListAdapter;
import ilioncorp.com.jukebox.view.adapter.ReproductionListAdapter;

public class TabHistorySongs extends Fragment implements Runnable,Handler.Callback {

    private Handler bridge;
    private android.support.v7.widget.RecyclerView listHistorySongs;
    private HistorySongDAO historySongDAO;
    private ArrayList<HistorySongVO> listSongs;
    private String idBar;
    private HistorySongListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_history_songs, container, false);
        this.listHistorySongs = rootView.findViewById(R.id.listHistorySongs);
        listHistorySongs.setHasFixedSize(true);
        listHistorySongs.setLayoutManager(new LinearLayoutManager(getContext()));
        bridge = new Handler(this::handleMessage);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        historySongDAO= new HistorySongDAO(bridge, Constantes.userActive.getUserUID());
        historySongDAO.getSongs();
        //listHistorySongs.setAdapter(adapter);
    }

    @Override
    public void run() {

    }

    @Override
    public boolean handleMessage(Message message) {
        listSongs = (ArrayList<HistorySongVO>) message.obj;
        adapter = new HistorySongListAdapter(listSongs,getContext());
        listHistorySongs.setAdapter(adapter);
        return false;
    }
}
