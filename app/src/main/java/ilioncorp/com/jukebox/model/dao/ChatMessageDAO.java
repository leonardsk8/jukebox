package ilioncorp.com.jukebox.model.dao;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ilioncorp.com.jukebox.model.dto.ChannelChatVO;
import ilioncorp.com.jukebox.model.dto.ChatMessageVO;
import ilioncorp.com.jukebox.model.dto.SessionVO;
import ilioncorp.com.jukebox.model.generic.CRUD;
import ilioncorp.com.jukebox.model.notifications.ClientNotificationViaFCMServerHelper;

public class ChatMessageDAO extends CRUD implements ValueEventListener{


    private String idBar;
    private String idToUser;
    private String idUser;
    private String channelId;
    private ChannelChatVO channelChat;
    private Handler bridge;

    public ChatMessageDAO(String idBar, String idToUser, String idUser, Handler bridge) {
        super();
        this.idBar = idBar;
        this.idUser = idUser;
        this.idToUser = idToUser;
        this.channelId="";
        this.bridge = bridge;
    }

    public void checkChannel(){
        Query query = myRef.child("chat").child("channels");
        query.addListenerForSingleValueEvent(this);

    }

    public void notifyUserOkHttp(SessionVO user,String body,String name){
        ClientNotificationViaFCMServerHelper notify = new
                ClientNotificationViaFCMServerHelper(user,"NUEVO MENSAJE DE: "+name,body);
        notify.sendMessage();
    }

    public void sendMessage(ChatMessageVO message){
        myRef.child("chat").child("establishment").child(this.idBar).child("users").child(channelId).push().setValue(message);
    }
    public Query getReferenceMessage(){
        Query query = myRef.child("chat").child("establishment").child(this.idBar).child("users").child(channelId);
        return query;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        int check = 0;
        if(dataSnapshot.exists()){
            for (DataSnapshot ds:dataSnapshot.getChildren()){
                channelChat = ds.getValue(ChannelChatVO.class);
                if((channelChat.getUser1().contains(idUser) || channelChat.getUser1().contains(idToUser))
                        && (channelChat.getUser2().contains(idUser)||channelChat.getUser2().contains(idToUser))){
                    channelId = channelChat.getKey();
                    check = 1;
                    break;
                }
            }
            if (check == 0)
                generateChannel();
        }
        else
            generateChannel();
        sendMessageChat();
    }

    private void sendMessageChat() {
        Message msg = new Message();
        bridge.sendMessage(msg);
    }

    private void generateChannel() {
        ChannelChatVO objChannel = new ChannelChatVO();
        String key = myRef.child("chat").child("channels").push().getKey();
        objChannel.setUser1(idUser);
        objChannel.setUser2(idToUser);
        objChannel.setKey(key);
        channelId = key;
        myRef.child("chat").child("channels").child(key).setValue(objChannel);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
