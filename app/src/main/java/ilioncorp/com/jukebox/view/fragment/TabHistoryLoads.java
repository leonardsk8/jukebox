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
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.HistoryLoadDAO;
import ilioncorp.com.jukebox.model.dao.HistorySongDAO;
import ilioncorp.com.jukebox.model.dto.HistoryLoadVO;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.adapter.HistoryLoadsListAdapter;
import ilioncorp.com.jukebox.view.adapter.ReproductionListAdapter;

import java.util.ArrayList;

public class TabHistoryLoads extends Fragment implements Runnable,Handler.Callback {

    public static Handler bridge;
    private android.support.v7.widget.RecyclerView listHistoryLoads;
    private ArrayList<HistoryLoadVO> listLoads;
    private String idBar;
    private HistoryLoadDAO historyLoadDAO;
    private HistoryLoadsListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_history_loads, container, false);
        this.listHistoryLoads = rootView.findViewById(R.id.listHistoryLoads);
        this.listHistoryLoads.setHasFixedSize(true);
        this.listHistoryLoads.setLayoutManager(new LinearLayoutManager(getContext()));
        bridge = new Handler(this::handleMessage);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        historyLoadDAO = new HistoryLoadDAO(bridge, Constantes.userActive.getUserUID());
        historyLoadDAO.getLoads();
    }

    @Override
    public void run() {

    }

    @Override
    public boolean handleMessage(Message message) {
        listLoads = (ArrayList<HistoryLoadVO>) message.obj;
        adapter = new HistoryLoadsListAdapter(listLoads,getContext());
        listHistoryLoads.setAdapter(adapter);
        return false;
    }
}
