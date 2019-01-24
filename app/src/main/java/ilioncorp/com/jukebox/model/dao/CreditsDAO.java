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
    private String idBar;

    public CreditsDAO(Handler bridge,String idBar,String idUser) {
        super();
        this.bridge = bridge;
        this.idBar = idBar;
        myRef.child("credits").child(idBar).child("creditos").orderByChild("idUser")
                .equalTo(idUser).addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        credits = new CreditsVO();
        if(dataSnapshot.exists())
            for (DataSnapshot ds : dataSnapshot.getChildren())
                this.credits = ds.getValue(CreditsVO.class);
        else
                credits.setCredits(0+"");
        sendMessage();
    }

    private void sendMessage() {
        Message msg = new Message();
        msg.obj = this.credits;
        bridge.sendMessage(msg);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void takeFromCredit(int credits,String idUser) {
        CreditsVO creditsVO = new CreditsVO();
        creditsVO.setCredits(credits+"");
        creditsVO.setIdUser(idUser);
        myRef.child("credits").child(idBar).child("creditos").child(idUser).setValue(creditsVO);
    }
}
