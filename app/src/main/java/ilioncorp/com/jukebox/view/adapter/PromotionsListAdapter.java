package ilioncorp.com.jukebox.view.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    private ArrayList<PromotionsVO> listPromotions;
    private RecyclerView MyRecyclerView;
    private PromotionsDAO promotions;
    private String idBar;
    private Handler bridge;
    private android.widget.TextView tvNoPromotions;
    public PromotionsListAdapter(String idBar) {
        this.idBar = idBar;
        bridge = new Handler(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_promotions, container, false);
        this.MyRecyclerView =  view.findViewById(R.id.listPromotions);
        this.tvNoPromotions = view.findViewById(R.id.tvNoPromotions);
        MyRecyclerView.setVisibility(View.INVISIBLE);
        tvNoPromotions.setVisibility(View.VISIBLE);
        showCharging("Cargando Promociones",getContext(),true);
        promotions = new PromotionsDAO(bridge,true,this.idBar,getContext());
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
        if(listPromotions.size()>0) {
            MyRecyclerView.setVisibility(View.VISIBLE);
            tvNoPromotions.setVisibility(View.INVISIBLE);
            MyRecyclerView.setHasFixedSize(true);
            LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
            MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            if (MyRecyclerView != null) {
                MyRecyclerView.setAdapter(new AdapterPromotion(listPromotions, getContext(), idBar));
            }
            MyRecyclerView.setLayoutManager(MyLayoutManager);
        }else{
            MyRecyclerView.setVisibility(View.INVISIBLE);
            tvNoPromotions.setVisibility(View.VISIBLE);
        }
        return false;
    }
}


