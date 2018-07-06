package ilioncorp.com.jukebox.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.PromotionsVO;

public class AdapterPromotion extends RecyclerView.Adapter<AdapterPromotion.MyViewHolder> implements View.OnClickListener{

    private ArrayList<PromotionsVO> listPromotions;
    private Context context;


    private AdapterPromotion(ArrayList<PromotionsVO> listPromotions, Context context) {
        this.listPromotions = listPromotions;
        this.context = context;
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
        Glide.with(context).load(listPromotions.get(position).getPro_image()).placeholder(R.drawable.error)
                .into(holder.ivImageMenuItem);
        holder.tvTitlePromotion.setText(listPromotions.get(position).getPro_name());
        holder.tvDescriptionPromotion.setText(listPromotions.get(position).getPro_description());
        holder.cvBtnGetCoupon.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return listPromotions.size();
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(context,"Disponible en versiones posteriores",Toast.LENGTH_SHORT).show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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
        }
    }
}
