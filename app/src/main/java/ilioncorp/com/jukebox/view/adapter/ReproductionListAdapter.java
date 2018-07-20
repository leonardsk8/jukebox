package ilioncorp.com.jukebox.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.ReproductionListDAO;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.model.dto.UserLikeVO;
import ilioncorp.com.jukebox.model.generic.ItemClickListener;
import ilioncorp.com.jukebox.utils.constantes.Constantes;

public class ReproductionListAdapter extends RecyclerView.Adapter<ReproductionListAdapter.ViewHolder>
        implements ItemClickListener {

    private ArrayList<ReproductionListVO> listSongs;
    private Context context;
    private ReproductionListDAO dao;

    public ReproductionListAdapter(ArrayList<ReproductionListVO> listSongs, Context context,ReproductionListDAO dao) {
        this.listSongs = listSongs;
        this.context = context;
        this.dao = dao;

    }

    public ArrayList<ReproductionListVO> getListSongs() {
        return listSongs;
    }

    public void setListSongs(ArrayList<ReproductionListVO> listSongs) {
        this.listSongs = listSongs;
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
        if(listSongs.get(position).isReproducing()) {
            holder.btnLike.setImageResource(R.drawable.disco_music);
            holder.tvLikes.setText("SONANDO");
        }
        else {
            holder.tvLikes.setText("Likes: " + listSongs.get(position).getListLikes().size());
            holder.btnLike.setImageResource(R.drawable.like);
        }
        holder.setItemClickListener(this);
    }

    @Override
    public int getItemCount() {
        return listSongs.size();
    }


    @Override
    public void onItemClick(View v, int pos,ImageView btn) {
        if(!listSongs.get(pos).isReproducing() & onlyLike(pos)) {
            Snackbar.make(v, "Like song:" + listSongs.get(pos).getName(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            listSongs.get(pos).setLikes(listSongs.get(pos).getListLikes().size()+1);
            UserLikeVO user=new UserLikeVO();
            user.setLike(true);
            user.setUserId(Constantes.userActive.getUserUID());
            user.setVideoId(listSongs.get(pos).getVideo_id());
            ReproductionListVO song = listSongs.get(pos);
            song.setLikes(listSongs.get(pos).getListLikes().size()+1);
            //dao.deleteSong(song);
            dao.sendSong(song);
            dao.setLikeSong(user);

            Constantes.positionLike = pos;
        }
        else if (!listSongs.get(pos).isReproducing()){
            Snackbar.make(v, "Solo puedes dar un like", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if (listSongs.get(pos).isReproducing()){
            Snackbar.make(v, "La canción ya esta sonando", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private boolean onlyLike(int pos) {
        for (Map.Entry m:listSongs.get(pos).getListLikes().entrySet()){
            UserLikeVO v = (UserLikeVO) m.getValue();
            if (v.getUserId().contains(Constantes.userActive.getUserUID()))
                   return false;
        }

        return true;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnail;
        ImageView btnLike;
        TextView tvUser;
        TextView tvTitle;
        TextView tvLikes;
        ItemClickListener itemClickListener;
        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.ivThumbnailSongReproducing);
            tvTitle = itemView.findViewById(R.id.tvTitleSong);
            tvUser = itemView.findViewById(R.id.tvUserSong);
            btnLike = itemView.findViewById(R.id.btnLike);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            btnLike.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(view,getLayoutPosition(),this.btnLike);
        }
        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }
    }
}
