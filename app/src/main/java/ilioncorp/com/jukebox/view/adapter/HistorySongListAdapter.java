package ilioncorp.com.jukebox.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.HistorySongDAO;
import ilioncorp.com.jukebox.model.dto.HistorySongVO;

import java.util.ArrayList;

public class HistorySongListAdapter extends RecyclerView.Adapter<HistorySongListAdapter.ViewHolder> {

    private ArrayList<HistorySongVO> listSongs;
    private Context context;




    public HistorySongListAdapter(ArrayList<HistorySongVO> listSongs, Context context) {
        this.listSongs = listSongs;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_song, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final HistorySongVO song = listSongs.get(position);
        Glide.with(context)
                .load(song.getThumnailSong())
                .placeholder(R.drawable.error)
                .into(holder.ivhistorysongthumbnail);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(song.getStateSong().equals("Aprobada"))
                    holder.tvhistorysongstate.setTextColor(holder.tvhistorysongstate.getContext().getResources()
                .getColor(R.color.VVIVO,context.getTheme()));
                else
                    holder.tvhistorysongstate.setTextColor(holder.tvhistorysongstate.getContext().getResources()
                            .getColor(R.color.ROJO,context.getTheme()));
            }else {
                if(song.getStateSong().equals("Aprobada"))
                holder.tvhistorysongstate.setTextColor(holder.tvhistorysongstate.getContext().getResources()
                        .getColor(R.color.VVIVO));
                else
                    holder.tvhistorysongstate.setTextColor(holder.tvhistorysongstate.getContext().getResources()
                            .getColor(R.color.ROJO));
            }

        holder.tvhistorysongtitle.setText(song.getNameSong());
        holder.tvhistorysongdate.setText(song.getDateSong());
        holder.tvhistorysongnamebar.setText(song.getNameBar());
        holder.tvhistorysongstate.setText(song.getStateSong());
    }

    @Override
    public int getItemCount() {
        return listSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivhistorysongthumbnail;
        private TextView tvhistorysongtitle;
        private TextView tvhistorysongdate;
        private TextView tvhistorysongstate;
        private TextView tvhistorysongnamebar;
        public ViewHolder(View view) {
            super(view);
            this.tvhistorysongnamebar =  view.findViewById(R.id.tv_history_song_name_bar);
            this.tvhistorysongstate =  view.findViewById(R.id.tv_history_song_state);
            this.tvhistorysongdate =  view.findViewById(R.id.tv_history_song_date);
            this.tvhistorysongtitle =  view.findViewById(R.id.tv_history_song_title);
            this.ivhistorysongthumbnail = view.findViewById(R.id.iv_history_song_thumbnail);

        }
    }
}
