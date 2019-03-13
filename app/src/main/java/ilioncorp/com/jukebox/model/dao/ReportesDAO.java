package ilioncorp.com.jukebox.model.dao;

import android.os.Handler;
import android.os.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import ilioncorp.com.jukebox.model.dto.CreditsVO;
import ilioncorp.com.jukebox.model.dto.ReportVO;
import ilioncorp.com.jukebox.model.generic.CRUD;

import java.util.Calendar;
import java.util.Date;

public class ReportesDAO extends CRUD implements ValueEventListener{

    private Handler bridge;
    private String idBar;
    Calendar c;

    public ReportesDAO(Handler bridge, String idBar) {
        super();
        this.bridge = bridge;
        this.idBar = idBar;
//        myRef.child("credits").child(idBar).child("creditos").orderByChild("idUser")
//                .equalTo(idUser).addListenerForSingleValueEvent(this);
    }

    public ReportesDAO(String idBar) {
        this.idBar = idBar;
        c  = Calendar.getInstance();
    }

    public void putVisit(String user, String hour){
        ReportVO reportVO = new ReportVO(user,hour);
        reportVO.setFecha(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"
                +c.get(Calendar.DAY_OF_MONTH));
        String key = myRef.child("reports").child("establishment")
                .child(this.idBar).child(c.get(Calendar.YEAR)+"")
                .child("visit")
                .child((c.get(Calendar.MONTH)+1)+"")
                .child(c.get(Calendar.DAY_OF_MONTH)+"").push().getKey();
        myRef.child("reports").child("establishment").child(this.idBar)
                .child("visit")
                .child(c.get(Calendar.YEAR)+"")
                .child((c.get(Calendar.MONTH)+1)+"")
//                .child(c.get(Calendar.DAY_OF_MONTH)+"")
                .child("day")
                .setValue(c.get(Calendar.DAY_OF_WEEK)+"");
        myRef.child("reports").child("establishment").child(this.idBar)
                .child("visit")
                .child(c.get(Calendar.YEAR)+"")
                .child((c.get(Calendar.MONTH)+1)+"")
//                .child(c.get(Calendar.DAY_OF_MONTH)+"")
                .child("values")
                .child(key)
                .setValue(reportVO);

    }
    public void putLogin(String user,String hour){
        ReportVO reportVO = new ReportVO(user,hour);
        reportVO.setFecha(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"
                +c.get(Calendar.DAY_OF_MONTH));
        String key = myRef.child("reports").child("establishment").child(this.idBar)
                .child("connect")
                .child(c.get(Calendar.YEAR)+"")
                .child((c.get(Calendar.MONTH)+1)+"")
                .child(c.get(Calendar.DAY_OF_MONTH)+"").push().getKey();
        myRef.child("reports").child("establishment").child(this.idBar)
                .child("connect")
                .child(c.get(Calendar.YEAR)+"")
                .child((c.get(Calendar.MONTH)+1)+"")
//                .child(c.get(Calendar.DAY_OF_MONTH)+"")
                .child("day")
                .setValue(c.get(Calendar.DAY_OF_WEEK)+"");
        myRef.child("reports").child("establishment").child(this.idBar)
                .child("connect")
                .child(c.get(Calendar.YEAR)+"")
                .child((c.get(Calendar.MONTH)+1)+"")
//                .child(c.get(Calendar.DAY_OF_MONTH)+"")
                .child("values")
                .child(key)
                .setValue(reportVO);

    }
    /*
    * EL LUGAR HACE REFERENCIA APROBADAS o ENVIADAS*/
    public void putCanciones(String user,String song,String lugar){
        ReportVO reportVO = new ReportVO();
        reportVO.setUserId(user);reportVO.setSongId(song);
        reportVO.setFecha(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"
                +c.get(Calendar.DAY_OF_MONTH));
        String key = myRef.child("reports").child("establishment").child(this.idBar)
                .child("songs")
                .child(lugar)
                .push().getKey();
        myRef.child("reports").child("establishment").child(this.idBar)
                .child("songs")
                .child(lugar)
                .child(key)
                .setValue(reportVO);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
//        credits = new CreditsVO();
//        if(dataSnapshot.exists())
//            for (DataSnapshot ds : dataSnapshot.getChildren())
//                this.credits = ds.getValue(CreditsVO.class);
//        else
//                credits.setCredits(0+"");
//        sendMessage();
    }

    private void sendMessage() {
        Message msg = new Message();
        bridge.sendMessage(msg);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
