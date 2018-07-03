package ilioncorp.com.jukebox.model.dao;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.model.dto.MenuVO;
import ilioncorp.com.jukebox.model.generic.CRUD;

public class MenuDAO extends CRUD implements ValueEventListener{


    ArrayList<MenuVO> listMenu;
    public ArrayList<EstablishmentVO> listEstablishment;
    MenuVO menu;
    Handler bridge;
    int position;

    public MenuDAO(ArrayList<EstablishmentVO> listEstablishment, Handler bridgeMenu) {
        super();
        bridge = bridgeMenu;
        this.listEstablishment  = listEstablishment;
        position = 0;
    }

    //PRUEBA COMMIT
    public void getMenuBars() {
        Query query;
        for(EstablishmentVO b: listEstablishment) {
            query = myRef.child("menu").child("establishment").child(String.valueOf(b.getQrcontent()))
                    .child("product").orderByChild("establishment");
            query.addListenerForSingleValueEvent(this);
            position++;
        }

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.e("Entro","ENTRO MENU");
        listMenu= new ArrayList<>();
        if(dataSnapshot.exists()) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                Log.e("ID", ds.getKey());
                this.menu = ds.getValue(MenuVO.class);
                listMenu.add(menu);
            }
            for (int i=0;i<listEstablishment.size();i++)
                if(listEstablishment.get(i).getMenuList() == null) {
                    listEstablishment.get(i).setMenuList(listMenu);
                    break;
                }
            if(listEstablishment.get(listEstablishment.size()-1).getMenuList()!=null)
                sendMessage();


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
}
