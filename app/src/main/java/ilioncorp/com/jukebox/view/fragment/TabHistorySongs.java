package ilioncorp.com.jukebox.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.HistorySongDAO;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.view.adapter.ReproductionListAdapter;

public class TabHistorySongs extends Fragment implements Runnable,Handler.Callback {

    public static Handler bridge;
    private android.support.v7.widget.RecyclerView listHistorySongs;
    private HistorySongDAO historySongDAO;
    private ArrayList<ReproductionListVO> listSongs;
    private String idBar;
    private ReproductionListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_reproducing, container, false);
        this.listHistorySongs = rootView.findViewById(R.id.listHistorySongs);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        historySongDAO= new HistorySongDAO();

        //listHistorySongs.setAdapter(adapter);
    }

    @Override
    public void run() {

    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }
}
