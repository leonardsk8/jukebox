package ilioncorp.com.jukebox.model.dao;

import android.os.Handler;
import android.os.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import ilioncorp.com.jukebox.model.dto.HistoryLoadVO;
import ilioncorp.com.jukebox.model.generic.CRUD;

import java.util.ArrayList;

public class HistoryLoadDAO extends CRUD implements ValueEventListener {

    private Handler bridge;
    private ArrayList<HistoryLoadVO> listSongs;
    private String idUser;

    public HistoryLoadDAO(Handler bridge, String idUser) {
        this.bridge = bridge;
        this.idUser = idUser;
        listSongs = new ArrayList<>();
    }
    public void getLoads(){
        myRef.child("history").child("users").child("load").child(idUser)
                .addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if(dataSnapshot.exists()){
            for (DataSnapshot ds:dataSnapshot.getChildren()){
                HistoryLoadVO vo = ds.getValue(HistoryLoadVO.class);
                listSongs.add(vo);
            }
        }
        sendMessage();


    }

    private void sendMessage() {
        Message msg = new Message();
        msg.obj = listSongs;
        bridge.sendMessage(msg);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
