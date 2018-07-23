package ilioncorp.com.jukebox.model.dao;

import android.os.Handler;
import android.os.Message;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ilioncorp.com.jukebox.model.dto.CreditsVO;

import ilioncorp.com.jukebox.model.generic.CRUD;

public class CreditsDAO extends CRUD implements ValueEventListener{

    private CreditsVO credits;
    private Handler bridge;

    public CreditsDAO(Handler bridge) {
        super();
        this.bridge = bridge;
        super.listener =  myRef.child("credits").addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        credits = new CreditsVO();
        boolean flag = true;
        if(dataSnapshot.exists()){
            for (DataSnapshot ds:dataSnapshot.getChildren()) {
                this.credits = ds.getValue(CreditsVO.class);
                if(super.userID.contains(ds.getKey())) {
                    flag = false;
                    sendMessage();

                }
            }
            if(flag){
                credits.setCredits(""+0);
                sendMessage();
            }

        }
        else{
            credits.setCredits(00+"");
        }
        //sendMessage();
    }

    private void sendMessage() {
        Message msg = new Message();
        msg.obj = this.credits;
        bridge.sendMessage(msg);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void takeFromCredit(int credits) {
        CreditsVO creditsVO = new CreditsVO();
        creditsVO.setCredits(credits+"");
        myRef.child("credits").child(super.userID).setValue(creditsVO);
    }
}
