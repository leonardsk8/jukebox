package ilioncorp.com.jukebox.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.List;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.model.dto.VideoItemVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.activity.PlayerActivity;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private List<VideoItemVO> listVideos;
    private Context context;
    protected FirebaseUser user;

    private String idBar;

    public VideoListAdapter(List<VideoItemVO> listVideos, Context context,String idBar) {
        this.listVideos = listVideos;
        this.context = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
        this.idBar = idBar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final VideoItemVO vItem = listVideos.get(position);
        holder.tvVideoTitle.setText(vItem.getTitle());
        holder.tvVideoDescription.setText(vItem.getDescription());
        Picasso.with(context).load(vItem.getThumbnailURL()).into(holder.ivThumnails);
        holder.itemView.setOnClickListener(view -> {
                    ReproductionListVO song = new ReproductionListVO();
                    song.setName(listVideos.get(position).getTitle());
                    song.setThumbnail(listVideos.get(position).getThumbnailURL());
                    song.setUser(user.getDisplayName());
                    song.setVideo_id(listVideos.get(position).getId());
                    song.setToken(FirebaseInstanceId.getInstance().getToken());
                    song.setUserId(Constantes.userActive.getUserUID());
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("song",song);
                    intent.putExtra("idBar",idBar);
                    context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listVideos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvVideoTitle;
        TextView tvVideoDescription;
        ImageView ivThumnails;
        public ViewHolder(View itemView) {
            super(itemView);
            tvVideoTitle = itemView.findViewById(R.id.tv_video_title);
            tvVideoDescription = itemView.findViewById(R.id.tv_video_description);
            ivThumnails = itemView.findViewById(R.id.iv_video_thumbnail);
        }
    }
}
