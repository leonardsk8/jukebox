package ilioncorp.com.jukebox.model.dao;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import ilioncorp.com.jukebox.model.dto.PromotionsVO;
import ilioncorp.com.jukebox.model.generic.CRUD;

public class PromotionsDAO extends CRUD implements ValueEventListener,Runnable  {

    private Handler bridge;
    private String idBar;
    private PromotionsVO promotion;
    private ArrayList<PromotionsVO> listPromotions;
    private boolean handlerActive;



    public PromotionsDAO(Handler bridge,boolean handlerActive, String idBar, Context context) {
        this.bridge = bridge;
        this.handlerActive = handlerActive;
        this.context =context;
        listPromotions =new ArrayList<>();
        myRef.child("promotions").child("establishment").child(idBar).child("create").addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        listPromotions.clear();
        if(dataSnapshot.exists()){
            for (DataSnapshot ds:dataSnapshot.getChildren()){
                this.promotion = ds.getValue(PromotionsVO.class);
                listPromotions.add(this.promotion);
            }
        }
        if(handlerActive)
        sendMessage();

    }


    private void sendMessage() {
        Message msg = new Message();
        msg.obj = listPromotions;
        bridge.sendMessage(msg);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void run() {

    }

    public void setHandlerActive(boolean handlerActive) {
        this.handlerActive = handlerActive;
    }
}
