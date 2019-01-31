package ilioncorp.com.jukebox.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.MenuVO;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.MyViewHolder> {
    private ArrayList<MenuVO> list;
    private Context context;

    public AdapterMenu(ArrayList<MenuVO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).placeholder(R.drawable.error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageItem);
        holder.nameItem.setText(list.get(position).getName());
        holder.priceItem.setText(String.valueOf(list.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageItem;
        TextView nameItem;
        TextView priceItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.ivImageMenuItem);
            nameItem = itemView.findViewById(R.id.tvNameMenuItem);
            priceItem = itemView.findViewById(R.id.tvPriceMenuItem);

        }
    }
}

