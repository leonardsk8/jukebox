package ilioncorp.com.jukebox.model.dao;

import android.os.Message;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.os.Handler;

import java.util.ArrayList;

import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.model.generic.CRUD;

public class EstablishmentDAO extends CRUD implements ValueEventListener,Handler.Callback {

    public EstablishmentVO establishment;
    public ArrayList<EstablishmentVO> listEstablishment;
    Handler bridge;
    Handler bridgeMenu;
    public EstablishmentDAO(Handler bridge) {
        super();
        this.bridge = bridge;
        listEstablishment = new ArrayList<>();
        bridgeMenu = new Handler(this);
    }



    public void getBar(String id){
        Query query = myRef.child("establishment").child("bares").orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(this);
    }

    public void getAllBars(){
        Query query = myRef.child("establishment").child("bares").orderByChild("name");
        query.addListenerForSingleValueEvent(this);
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                Log.e("value",ds.getKey());
                this.establishment = ds.getValue(EstablishmentVO.class);
                this.establishment.setImagesBar(this.establishment.getImages().split("~"));
                listEstablishment.add(this.establishment);
            }
            MenuDAO menu = new MenuDAO(listEstablishment,bridgeMenu);
            menu.getMenuBars();
        }

    }

    private void sendMessage() {
        Message msg = new Message();
        msg.obj = listEstablishment;
        bridge.sendMessage(msg);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    @Override
    public boolean handleMessage(Message message) {
        listEstablishment = (ArrayList<EstablishmentVO>) message.obj;
        sendMessage();
        return false;
    }
}
