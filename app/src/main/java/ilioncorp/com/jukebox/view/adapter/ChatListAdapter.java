package ilioncorp.com.jukebox.view.adapter;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.SessionVO;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>
implements View.OnClickListener{


    private ArrayList<SessionVO> listUsers;
    private ArrayList<Uri> listUri;
    private View.OnClickListener listener;
    private Context context;

    public ChatListAdapter(ArrayList<SessionVO> listUsers, Context context,ArrayList<Uri> listUri) {
        this.listUsers = listUsers;
        this.context = context;
        this.listUri = listUri;
    }

    public ArrayList<SessionVO> getListUsers() {
        return listUsers;
    }

    public void setListUsers(ArrayList<SessionVO> listUsers) {
        this.listUsers = listUsers;
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Fresco.initialize(context);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SessionVO session = listUsers.get(position);
        holder.imageProfile.setImageURI(listUri.get(position));
        holder.txtName.setText(session.getSessionUserName());

    }


    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private com.facebook.drawee.view.SimpleDraweeView imageProfile;
        private TextView txtName;
        public ViewHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.ImageChat);
            txtName = itemView.findViewById(R.id.tvNameUser);
        }
    }
}
