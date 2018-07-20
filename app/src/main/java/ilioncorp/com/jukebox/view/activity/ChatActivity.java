package ilioncorp.com.jukebox.view.activity;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.ChatMessageDAO;
import ilioncorp.com.jukebox.model.dto.ChatMessageVO;
import ilioncorp.com.jukebox.model.dto.SessionVO;
import ilioncorp.com.jukebox.model.dto.UserVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener,Handler.Callback,Runnable{
    SessionVO user;
    ChatMessageDAO userDao;
    String idBar;
    private FirebaseListAdapter<ChatMessageVO> adapter;
    private android.widget.EditText input;
    Date date;
    String DEFAULT_PATTERN;
    DateFormat formatter;
    private Handler bridge;
    private ListView listOfMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        date = new Date();
        input = findViewById(R.id.input);
        DEFAULT_PATTERN = "yyyy-MM-dd HH:mm";
        formatter = new SimpleDateFormat(DEFAULT_PATTERN);
        user = (SessionVO) Objects.requireNonNull(getIntent().getExtras()).getSerializable("userSession");
        idBar = getIntent().getExtras().getString("idBar");
        FloatingActionButton fab = findViewById(R.id.fabChat);
        bridge = new Handler(this);
        userDao = new ChatMessageDAO(this.idBar,user.getSessionUserId(), Constantes.userActive.getUserUID(),bridge);
        userDao.checkChannel();
        fab.setOnClickListener(this::onClick);

    }
    private void scrollMyListViewToBottom() {
        listOfMessages.post(new Runnable() {
            @Override
            public void run() {
                listOfMessages.setSelection(adapter.getCount() - 1);
            }
        });
    }

    private void displayChatMessages() {
        listOfMessages =findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<ChatMessageVO>(this, ChatMessageVO.class,
                R.layout.item_chat, userDao.getReferenceMessage()) {
            @Override
            protected void populateView(View v, ChatMessageVO model, int position) {
                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMensaje());
                messageUser.setText(model.getNombre());

                // Format the date before showing it
                messageTime.setText(model.getFecha());
                //scrollMyListViewToBottom();
            }
        };
        //scrollMyListViewToBottom();
        listOfMessages.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        ChatMessageVO message = new ChatMessageVO();
        UserVO us = Constantes.userActive;
        message.setNombre(us.getUserName());
        message.setImagen(us.getUserUrlImage());
        message.setMensaje(input.getText().toString());
        message.setFecha(formatter.format(date));

        userDao.sendMessage(message);
        //notifyUser(input.getText().toString(),"Nuevo mensaje de: "+Constantes.userActive.getUserName());
        //notifyUserOkHttp(input.getText().toString(),"Nuevo mensaje de: "+Constantes.userActive.getUserName());
        // Clear the input
        input.setText("");



    }

    private void notifyUserOkHttp(String body,String title) {
        userDao.notifyUserOkHttp(user,body,title);
    }

    private void notifyUser(String body,String title) {
        new Thread(()->{
            userDao.notifyUser(user,body,title);
        }).start();
    }

    @Override
    public boolean handleMessage(Message message) {
        displayChatMessages();
        return false;
    }

    @Override
    public void run() {

    }
}
