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
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.HistoryLoadDAO;
import ilioncorp.com.jukebox.model.dto.HistoryLoadVO;

import java.util.ArrayList;

public class HistoryLoadsListAdapter extends RecyclerView.Adapter<HistoryLoadsListAdapter.ViewHolder> {

    private ArrayList<HistoryLoadVO> listLoads;

    private Context context;


    public HistoryLoadsListAdapter(ArrayList<HistoryLoadVO> listLoads, Context context) {
        this.listLoads = listLoads;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_load, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final HistoryLoadVO historyLoadVO = listLoads.get(position);
        Glide.with(context)
                .load(historyLoadVO.getThumnailBar())
                .placeholder(R.drawable.error)
                .into(holder.ivhistoryloadthumbnail);
        holder.tvhistoryloaddate.setText(historyLoadVO.getDate());
        holder.tvhistoryloadnamebar.setText("Bar: "+historyLoadVO.getNameBar());
        holder.tvhistoryloadcredits.setText("Creditos: "+historyLoadVO.getCredits());

    }

    @Override
    public int getItemCount() {
        return listLoads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private android.widget.ImageView ivhistoryloadthumbnail;
        private android.widget.TextView tvhistoryloaddate;
        private android.widget.TextView tvhistoryloadcredits;
        private android.widget.TextView tvhistoryloadnamebar;
        public ViewHolder(View view) {
            super(view);
            this.tvhistoryloadnamebar =  view.findViewById(R.id.tv_history_load_nameBar);
            this.tvhistoryloadcredits =  view.findViewById(R.id.tv_history_load_credits);
            this.tvhistoryloaddate =  view.findViewById(R.id.tv_history_load_date);
            this.ivhistoryloadthumbnail =  view.findViewById(R.id.iv_history_load_thumbnail);

        }
    }
}
