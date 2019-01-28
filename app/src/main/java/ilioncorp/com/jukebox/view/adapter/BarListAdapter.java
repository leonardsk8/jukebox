package ilioncorp.com.jukebox.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;

public class BarListAdapter extends RecyclerView.Adapter<BarListAdapter.ViewHolder> implements View.OnClickListener{

    private List<EstablishmentVO> listBares;
    private View.OnClickListener listener;
    private Context mContext;

    public BarListAdapter(List<EstablishmentVO> listItems,Context mContext) {
        this.listBares = listItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_preview_bar,parent,false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final EstablishmentVO establishment= listBares.get(position);
        Glide.with(mContext)
                .load(establishment.getImagesBar()[0])
                .placeholder(R.drawable.error)
                .into(holder.imageBar);
        holder.nameBar.setText(establishment.getName());
        holder.raitingBar.setRating(establishment.getRaiting());
        holder.addressBar.setText(establishment.getAddress());
        holder.gendersBar.setText(establishment.getGenders());
    }

    @Override
    public int getItemCount() {
        return listBares.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageBar;
        TextView nameBar;
        RatingBar raitingBar;
        TextView addressBar;
        TextView gendersBar;
        public ViewHolder(View itemView) {
            super(itemView);
            imageBar = itemView.findViewById(R.id.imagePreviewBar);
            nameBar = itemView.findViewById(R.id.tvNameBarPreview);
            raitingBar = itemView.findViewById(R.id.ratingBarPreview);
            addressBar = itemView.findViewById(R.id.tvAddressBarPreview);
            gendersBar= itemView.findViewById(R.id.tvGendersBarPreview);

        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(final View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }
    public void setFilter(ArrayList<EstablishmentVO> listorder){
        this.listBares = new ArrayList<>();
        this.listBares.addAll(listorder);
        notifyDataSetChanged();
    }
}
