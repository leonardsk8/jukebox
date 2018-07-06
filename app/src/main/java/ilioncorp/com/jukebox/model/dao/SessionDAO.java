package ilioncorp.com.jukebox.model.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ilioncorp.com.jukebox.model.dto.SessionVO;
import ilioncorp.com.jukebox.model.generic.CRUD;
import ilioncorp.com.jukebox.utils.constantes.Constantes;


public class SessionDAO extends CRUD implements ValueEventListener,Runnable {

    SessionVO sessionObj;
    private Handler bridge;
    private String idBar;
    private SessionVO usersChat;
    private ArrayList<SessionVO> listUsers;
    private ArrayList<Uri> listUris;

    public SessionDAO(String idBar,Handler bridge,Context context) {
        super();
        this.context = context;
        this.bridge = bridge;
        this.idBar = idBar;
        listener = myRef.child("session").child("establishment").child(idBar).child("users").addValueEventListener(this);
        this.sessionObj = new SessionVO();
        this.listUsers = new ArrayList<>();
        this.listUris = new ArrayList<>();
    }

    public SessionDAO() {
    }

    public void generatedSession(String idBar){
        sessionObj = new SessionVO();
        Date date = new Date();
        String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm";
        DateFormat formatter = new SimpleDateFormat(DEFAULT_PATTERN);
        sessionObj.setSessionDateStart(formatter.format(date));
        sessionObj.setSessionState("active");
        sessionObj.setSessionUserId(Constantes.userActive.getUserUID());
        sessionObj.setSessionUserToken(FirebaseInstanceId.getInstance().getToken());
        sessionObj.setSessionUserName(Constantes.userActive.getUserName());
        sessionObj.setSessionUserImage(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()+"?type=large");
        myRef.child("session").child("establishment").child(idBar).child("users")
                .child(sessionObj.getSessionUserId()).setValue(sessionObj);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        listUsers.clear();
        if (dataSnapshot.exists()) {
            for(DataSnapshot ds:dataSnapshot.getChildren()){
                usersChat = ds.getValue(SessionVO.class);
                if(usersChat.getSessionState().contains("active")){
                    listUsers.add(usersChat);
                }
            }
            new Thread(this).start();
        }
    }


    private void sendMessage() {
        Object[] objArrays = {this.listUsers,this.listUris};
        Message msg = new Message();
        msg.obj = objArrays;
        bridge.sendMessage(msg);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void checkSession(Handler bridge,String idBar) {
        final String[] state = {""};
        Query query = myRef.child("session").child("establishment").child(idBar).child("users").
                orderByChild("sessionUserId").equalTo(userID).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        sessionObj = ds.getValue(SessionVO.class);
                        if(sessionObj.getSessionState().contains("active"))
                            state[0] = "active";
                        else if(sessionObj.getSessionState().contains("vetoed"))
                            state[0] = "vetoed";
                        else if(sessionObj.getSessionState().contains("inactive"))
                            state[0] = "inactive";
                    }
                else
                    state[0] = "";
                Message msg  = new Message();
                msg.obj = state[0];
                bridge.sendMessage(msg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void run() {
        try {
            int x=0;
            for (SessionVO s:listUsers) {
                URL ima_value = new URL(s.getSessionUserImage());
                Bitmap img = BitmapFactory.decodeStream(ima_value.openConnection().getInputStream());
                listUris.add(getImageUri(context, img));
                x++;
            }
            sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}