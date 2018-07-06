package ilioncorp.com.jukebox.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.SessionDAO;
import ilioncorp.com.jukebox.model.dto.SessionVO;
import ilioncorp.com.jukebox.view.activity.ChatActivity;
import ilioncorp.com.jukebox.view.adapter.BarListAdapter;
import ilioncorp.com.jukebox.view.adapter.ChatListAdapter;
import ilioncorp.com.jukebox.view.generic.GenericFragment;


@SuppressLint("ValidFragment")
public class TabUsers extends GenericFragment implements Handler.Callback,View.OnClickListener{

    private android.support.v7.widget.RecyclerView listRecycleUsers;
    private ArrayList<SessionVO> listUsers;
    private ArrayList<Uri> listUri;
    private SessionDAO sessionDAO;
    private BarListAdapter aux;
    private ChatListAdapter adapter;
    private String idBar;
    private Handler bridge;

    public TabUsers(String idBar) {
        this.idBar = idBar;
        bridge = new Handler(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_users, container, false);
        this.listRecycleUsers = rootView.findViewById(R.id.listUsers);
        showCharging("Cargando usuarios",getContext());
        sessionDAO = new SessionDAO(this.idBar,bridge,getContext());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public boolean handleMessage(Message message) {
        hideCharging();
        Object[] objArrays = (Object[]) message.obj;
        listUsers = (ArrayList<SessionVO>) objArrays[0];
        listUri = (ArrayList<Uri>) objArrays[1];
        adapter = new ChatListAdapter(listUsers,getContext(),listUri);
        adapter.setOnClickListener(this);
        listRecycleUsers.setAdapter(adapter);
        listRecycleUsers.setHasFixedSize(true);
        listRecycleUsers.setLayoutManager(new LinearLayoutManager(getContext()));



        return false;
    }

    @Override
    public void onClick(View view) {
        Intent pantalla = new Intent(getContext(),ChatActivity.class);
        SessionVO sesion= listUsers.get(listRecycleUsers.getChildAdapterPosition(view));
        pantalla.putExtra("userSession",sesion);
        pantalla.putExtra("idBar",idBar);
        startActivity(pantalla);
    }
}
