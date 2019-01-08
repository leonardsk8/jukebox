package ilioncorp.com.jukebox.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private static final int REQUESTCODE_WRITE = 2;
    private static final int REQUESTCODE_READ = 3;
    private static final String TAG = "TabUSERS";
    private android.support.v7.widget.RecyclerView listRecycleUsers;
    private ArrayList<SessionVO> listUsers;
    private ArrayList<Uri> listUri;
    private SessionDAO sessionDAO;
    private BarListAdapter aux;
    private ChatListAdapter adapter;
    private String idBar;
    private Handler bridge;
    private android.widget.TextView tvUSERSTITLE;
    private android.widget.TextView tvNoUsers;

    public TabUsers(String idBar) {
        this.idBar = idBar;
        bridge = new Handler(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_users, container, false);
        this.tvNoUsers = rootView.findViewById(R.id.tvNoUsers);
        this.listRecycleUsers = rootView.findViewById(R.id.listUsers);
        this.tvNoUsers.setVisibility(View.VISIBLE);
        listRecycleUsers.setVisibility(View.INVISIBLE);
        checkPermissionWriteReadExternalStorage();
        return rootView;
    }

    private void checkPermissionWriteReadExternalStorage() {
        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

    }

    public void isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");

            } else {

                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, REQUESTCODE_READ);

            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1");

        }
    }

    private void callUsers() {
        showCharging("Cargando usuarios",getContext(),true);
        sessionDAO = new SessionDAO(this.idBar,bridge,getContext());
    }

    public void isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
                callUsers();
            } else {
                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(getActivity(), new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_WRITE);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTCODE_WRITE:
                Log.d(TAG, "External storage2");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    callUsers();
                } else {
                    messageToast("No se garantizo el permiso");

                }
                break;

            case REQUESTCODE_READ:
                Log.d(TAG, "External storage1");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission

                } else {
                    messageToast("No se garantizo el permiso");

                }
                break;
        }
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
        if(listUsers.size()>=0) {
            this.tvNoUsers.setVisibility(View.INVISIBLE);
            listRecycleUsers.setVisibility(View.VISIBLE);
        }
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
