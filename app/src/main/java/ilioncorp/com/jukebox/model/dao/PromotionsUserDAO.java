package ilioncorp.com.jukebox.model.dao;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import ilioncorp.com.jukebox.model.dto.PromotionsUserVO;
import ilioncorp.com.jukebox.model.generic.CRUD;

import java.util.Random;

public class PromotionsUserDAO extends CRUD implements ValueEventListener {

    private Handler bridge;
    private String idBar;
    private PromotionsUserVO promotion;

    public PromotionsUserDAO(Handler bridge, String idBar, Context context) {
        this.bridge = bridge;
        this.idBar = idBar;
        this.context =context;
    }

    public void getPromotion(String idCode){
        Query query;
        query = myRef.child("promotions").child("establishment").child(idBar).child("promoUser")
                .orderByChild("proUCode")
        .equalTo(idCode);
        query.addListenerForSingleValueEvent(this);
    }

    public void promotionCreate(PromotionsUserVO vo){
        myRef.child("promotions").child("establishment").child(idBar).child("promoUser")
                .child(vo.getProUCode()).setValue(vo);
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
            for (DataSnapshot ds:dataSnapshot.getChildren()){
                this.promotion = ds.getValue(PromotionsUserVO.class);
            }
        }
        sendMessage();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void sendMessage() {
        Message msg = new Message();
        msg.obj = this.promotion;
        bridge.sendMessage(msg);
    }
}
