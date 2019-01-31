package ilioncorp.com.jukebox.model.dao;

import android.os.Handler;
import android.os.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import ilioncorp.com.jukebox.model.dto.HistoryLoadVO;
import ilioncorp.com.jukebox.model.dto.HistorySongVO;
import ilioncorp.com.jukebox.model.generic.CRUD;

import java.util.ArrayList;

public class HistorySongDAO extends CRUD implements ValueEventListener {

    private Handler bridge;
    private ArrayList<HistorySongVO> listSongs;
    private String idUser;

    public HistorySongDAO(Handler bridge, String idUser) {
        this.bridge = bridge;
        this.idUser = idUser;
        listSongs = new ArrayList<>();
    }
    public void getSongs(){
        myRef.child("history").child("users").child("song").child(idUser)
                .addListenerForSingleValueEvent(this);
    }
    public void getSongsRealTime(){
        myRef.child("history").child("users").child("song").child(idUser)
                .addValueEventListener(this);
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        this.listSongs.clear();
        if(dataSnapshot.exists()){
            for (DataSnapshot ds:dataSnapshot.getChildren()){
                HistorySongVO vo = ds.getValue(HistorySongVO.class);
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

    public void putSong(HistorySongVO history) {
        myRef.child("history").child("users").child("song").child(idUser).child(history.getVideoIdSong())
                .setValue(history);
    }
}
