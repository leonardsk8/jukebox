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

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;

public class ReproductionListAdapter extends RecyclerView.Adapter<ReproductionListAdapter.ViewHolder> {

    private ArrayList<ReproductionListVO> listSongs;
    private Context context;

    public ReproductionListAdapter(ArrayList<ReproductionListVO> listSongs, Context context) {
        this.listSongs = listSongs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ReproductionListVO song = listSongs.get(position);
        Glide.with(context).load(song.getThumbnail()).placeholder(R.drawable.error)
        .into(holder.thumbnail);
        holder.tvTitle.setText(song.getName());
        holder.tvUser.setText(song.getUser());
    }

    @Override
    public int getItemCount() {
        return listSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView tvTitle;
        TextView tvUser;
        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.ivThumbnailSongReproducing);
            tvTitle = itemView.findViewById(R.id.tvTitleSong);
            tvUser = itemView.findViewById(R.id.tvUserSong);
        }
    }
}
