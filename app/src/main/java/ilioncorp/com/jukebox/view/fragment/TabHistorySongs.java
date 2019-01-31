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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.widget.TextView;
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
    private boolean onlyLast = false;
    private android.widget.TextView tvNoHistorySongs;
    public TabHistorySongs() {

    }

    @SuppressLint("ValidFragment")
    public TabHistorySongs(boolean onlyLast,String idBar) {
        this.onlyLast = onlyLast;
        this.idBar = idBar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_history_songs, container, false);
        this.listHistorySongs = rootView.findViewById(R.id.listHistorySongs);
        this.tvNoHistorySongs = rootView.findViewById(R.id.tvNoHistorySongs);
        listHistorySongs.setVisibility(View.INVISIBLE);
        tvNoHistorySongs.setVisibility(View.VISIBLE);
        listHistorySongs.setHasFixedSize(true);
        listHistorySongs.setLayoutManager(new LinearLayoutManager(getContext()));
        bridge = new Handler(this::handleMessage);
        listSongs = new ArrayList<>();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        historySongDAO= new HistorySongDAO(bridge, Constantes.userActive.getUserUID());
        if(onlyLast)
            historySongDAO.getSongsRealTime();
        else
            historySongDAO.getSongs();
        //listHistorySongs.setAdapter(adapter);
    }

    @Override
    public void run() {

    }

    @Override
    public boolean handleMessage(Message message) {

        listSongs = (ArrayList<HistorySongVO>) message.obj;
        if (listSongs.size()>0) {

            ArrayList<HistorySongVO> aux = new ArrayList<>();
            if (onlyLast) {
                try {
                    filterSongs();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (HistorySongVO vo : listSongs) {
                    if (vo.getIdBar().equals(idBar))
                        aux.add(vo);
                }
                listSongs = aux;
            }
            visibility(listSongs.size(), listHistorySongs, tvNoHistorySongs);
            Collections.sort(listSongs);

        }
        else {
            listHistorySongs.setVisibility(View.INVISIBLE);
            tvNoHistorySongs.setVisibility(View.VISIBLE);
        }
        adapter = new HistorySongListAdapter(listSongs, getContext());
        listHistorySongs.setAdapter(adapter);
        return false;
    }

    static void visibility(int size, RecyclerView listHistorySongs, TextView tvNoHistorySongs) {
        if(size >0) {
            listHistorySongs.setVisibility(View.VISIBLE);
            tvNoHistorySongs.setVisibility(View.INVISIBLE);
        }
        else {
            listHistorySongs.setVisibility(View.INVISIBLE);
            tvNoHistorySongs.setVisibility(View.VISIBLE);
        }
    }

    private void filterSongs() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date date = new Date();
        Date fechaFinal=dateFormat.parse(dateFormat.format(date));
        ArrayList<HistorySongVO> list = new ArrayList<>();
        for (HistorySongVO vo:listSongs){
            int dias=0;
            int horas=0;
            int minutos=0;
            Date fechaInicial=dateFormat.parse(vo.getDateSong());
            int diferencia=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/1000);
            if(diferencia>86400) {
                dias=(int)Math.floor(diferencia/86400);
                diferencia=diferencia-(dias*86400);
            }
            if(diferencia>3600) {
                horas=(int)Math.floor(diferencia/3600);
            }
            if(dias<1)
                if(horas<9)
                    list.add(vo);
        }
        listSongs = list;

    }
}
