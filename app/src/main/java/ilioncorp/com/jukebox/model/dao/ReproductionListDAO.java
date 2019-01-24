package ilioncorp.com.jukebox.model.dao;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.model.dto.UserLikeVO;
import ilioncorp.com.jukebox.model.generic.CRUD;

public class ReproductionListDAO extends CRUD implements ValueEventListener {

    private ArrayList<ReproductionListVO> listSongs;
    private ArrayList<UserLikeVO>  likes;
    private String idBar;
    private ReproductionListVO song;
    private UserLikeVO like;
    private Handler bridge;
    private boolean handlerActive;

    public ReproductionListDAO(String idBar,Handler bridge,boolean handlerActive) {
        super();
        this.idBar = idBar;
        this.bridge = bridge;
        this.handlerActive = handlerActive;
        listSongs = new ArrayList<>();
        listener = myRef.child("reproduction_list").child("establishment").child(this.idBar)
                .child("songs").addValueEventListener(this);

    }
    public ReproductionListDAO(String idBar,Handler bridge) {
        super();
        this.idBar = idBar;
        this.bridge = bridge;
        listSongs = new ArrayList<>();


    }

    public void setLikeSong(UserLikeVO obj){
        myRef.child("reproduction_list").child("establishment").child(idBar).child("songs")
                .child(obj.getVideoId()).child("listLikes").child(obj.getUserId()).setValue(obj);

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        this.listSongs.clear();
        if(dataSnapshot.exists()){
            song = new ReproductionListVO();
            int x=0;
            for (DataSnapshot ds: dataSnapshot.getChildren()){
                this.song = ds.getValue(ReproductionListVO.class);
                if(this.song.isApproved()) {
                    listSongs.add(this.song);
                    /*for (Map.Entry m:listSongs.get(x).getListLikes().entrySet()){
                        UserLikeVO vo = (UserLikeVO) m.getValue();
                        Log.e("list likes",m.getKey()+"");
                        Log.e("list likes",vo.getUserId()+"");
                        Log.e("list likes",vo.getVideoId()+"");
                        Log.e("list likes",vo.isLike()+"");
                    }*/
                }
                x++;
            }


            if(handlerActive)
            sendMessage();
        }
    }
    public void stopListener(){
        if (myRef != null && listener != null) {
            myRef.child("reproduction_list").child("establishment").child(this.idBar)
                    .child("songs").removeEventListener(listener);
            Log.e("STOP","Listener Songs Stoped");
        }
    }
    private void sendMessage() {
        Message msg = new Message();
        msg.obj = this.listSongs;
        bridge.sendMessage(msg);
    }
    public void deleteSong(ReproductionListVO song){
        myRef.child("reproduction_list").child("establishment").child(this.idBar)
                .child("songs").child(song.getVideo_id()).removeValue();
    }
    public void sendSong(ReproductionListVO song){
        myRef.child("reproduction_list").child("establishment").child(this.idBar)
                .child("songs").child(song.getVideo_id()).setValue(song);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void findSong(String videoId){
        myRef.child("reproduction_list").child("establishment").child(this.idBar)
                .child("songs").orderByChild("video_id").equalTo(videoId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Message message = new Message();
                        if(dataSnapshot.exists())
                            message.obj=true;
                        else
                            message.obj=false;
                        bridge.sendMessage(message);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
