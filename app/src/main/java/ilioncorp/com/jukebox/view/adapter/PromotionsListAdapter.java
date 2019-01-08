package ilioncorp.com.jukebox.view.adapter;

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
import ilioncorp.com.jukebox.model.dao.PromotionsDAO;
import ilioncorp.com.jukebox.model.dto.PromotionsVO;
import ilioncorp.com.jukebox.view.generic.GenericFragment;

@SuppressLint("ValidFragment")
public class PromotionsListAdapter extends GenericFragment implements Handler.Callback {
    ArrayList<PromotionsVO> listPromotions;
    RecyclerView MyRecyclerView;
    private PromotionsDAO promotions;
    private String idBar;
    private Handler bridge;

    public PromotionsListAdapter(String idBar) {
        this.idBar = idBar;
        bridge = new Handler(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_promotions, container, false);
        MyRecyclerView =  view.findViewById(R.id.listPromotions);
        showCharging("Cargando usuarios",getContext(),true);
        promotions = new PromotionsDAO(bridge,this.idBar,getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean handleMessage(Message message) {
        hideCharging();
        listPromotions = (ArrayList<PromotionsVO>) message.obj;

        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (listPromotions.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new AdapterPromotion(listPromotions,getContext()));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);
        return false;
    }
}


