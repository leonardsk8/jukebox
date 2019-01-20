package ilioncorp.com.jukebox.model.dao;

import android.os.Handler;
import android.os.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import ilioncorp.com.jukebox.model.dto.CommentsVO;
import ilioncorp.com.jukebox.model.dto.PromotionsVO;
import ilioncorp.com.jukebox.model.generic.CRUD;

import java.util.ArrayList;

public class CommentsDAO extends CRUD implements ValueEventListener {

    private Handler bridge;
    private String idBar;
    private CommentsVO comment;
    private ArrayList<CommentsVO> listComments;

    public CommentsDAO(Handler bridge, String idBar) {
        this.bridge = bridge;
        this.idBar = idBar;
        listComments = new ArrayList<>();
    }

    public void getComments(){
        myRef.child("comments").child(this.idBar).addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        listComments.clear();
        if(dataSnapshot.exists()){
            for (DataSnapshot ds:dataSnapshot.getChildren()){
                this.comment = ds.getValue(CommentsVO.class);
                listComments.add(this.comment);
            }
        }
        sendMessage();
    }

    private void sendMessage() {
        Message msg = new Message();
        msg.obj = listComments;
        bridge.sendMessage(msg);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
