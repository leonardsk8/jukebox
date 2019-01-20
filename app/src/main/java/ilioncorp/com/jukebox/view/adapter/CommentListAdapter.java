package ilioncorp.com.jukebox.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.CommentsDAO;
import ilioncorp.com.jukebox.model.dto.CommentsVO;


import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> implements View.OnClickListener{

    private List<CommentsVO> listComments;
    private Context mContext;
    private CommentsDAO commentsDAO;

    public CommentListAdapter(List<CommentsVO> listComments, Context mContext) {
        this.listComments = listComments;
        this.mContext = mContext;
//        commentsDAO = new CommentsDAO(null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CommentsVO comment = listComments.get(position);
        holder.tvNamePersonComment.setText(comment.getNameUser());
        holder.rbLeaveStars.setRating(comment.getRating());
        holder.etLeaveComment.setText(comment.getComment());
        holder.etLeaveComment.setEnabled(false);
        holder.btnAccept.setVisibility(View.INVISIBLE);
        holder.rbLeaveStars.setIsIndicator(true);
        holder.btnAccept.setOnClickListener(this::onClick);
        holder.tvDateComment.setText(comment.getDate());
    }

    @Override
    public int getItemCount() {
        return listComments.size();
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(mContext,"Proximamente",Toast.LENGTH_SHORT).show();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private android.widget.EditText etLeaveComment;
        private android.widget.TextView tvNamePersonComment;
        private RatingBar rbLeaveStars;
        private ImageView btnAccept;
        private TextView tvDateComment;
        public ViewHolder(View itemView) {
            super(itemView);
            this.tvDateComment = itemView.findViewById(R.id.tvDateComment);
            this.btnAccept =  itemView.findViewById(R.id.btnAcceptComment);
            this.rbLeaveStars =  itemView.findViewById(R.id.rbLeaveStars);
            this.etLeaveComment =  itemView.findViewById(R.id.etLeaveComment);
            this.tvNamePersonComment =  itemView.findViewById(R.id.tvNamePersonComment);

        }


    }
}
