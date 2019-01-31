package ilioncorp.com.jukebox.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.PromotionsVO;

import ilioncorp.com.jukebox.view.activity.PromotionActivity;

public class AdapterPromotion extends RecyclerView.Adapter<AdapterPromotion.MyViewHolder>{

    private ArrayList<PromotionsVO> listPromotions;
    private Context context;
    private String idBar;


    public AdapterPromotion(ArrayList<PromotionsVO> listPromotions, Context context,String idBar) {
        this.listPromotions = listPromotions;
        this.context = context;
        this.idBar = idBar;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_promotions, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(listPromotions.get(position).
                getPro_image()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.error)
                .into(holder.ivImageMenuItem);
        holder.tvTitlePromotion.setText(listPromotions.get(position).getPro_name());
        holder.tvDescriptionPromotion.setText(listPromotions.get(position).getPro_description());

    }

    @Override
    public int getItemCount() {
        return listPromotions.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private android.widget.ImageView ivImageMenuItem;
        private android.widget.TextView tvTitlePromotion;
        private android.widget.TextView tvDescriptionPromotion;
        private android.support.v7.widget.CardView cvBtnGetCoupon;


        public MyViewHolder(View view) {
            super(view);
            this.cvBtnGetCoupon= view.findViewById(R.id.cvBtnGetCoupon);
            this.tvDescriptionPromotion = view.findViewById(R.id.tvDescriptionPromotion);
            this.tvTitlePromotion = view.findViewById(R.id.tvTitlePromotion);
            this.ivImageMenuItem = view.findViewById(R.id.ivImageMenuItem);
            this.cvBtnGetCoupon.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            PromotionsVO promotionsVO = listPromotions.get(position);
            Intent intent = new Intent(context,PromotionActivity.class);
            intent.putExtra("promotions",promotionsVO);
            intent.putExtra("idBar", idBar);
            context.startActivity(intent);
        }

    }
}
