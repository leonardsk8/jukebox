package ilioncorp.com.jukebox.model.generic;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import ilioncorp.com.jukebox.utils.constantes.ERoutes;

public abstract class CRUD {
    public String json;

    protected ValueEventListener listener;
    protected FirebaseDatabase mFirebaseDatabase;
    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    protected DatabaseReference myRef;
    protected String userID;
    protected FirebaseUser user;
    protected Context context;

    public CRUD() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

    }

    public String Read(String parameter,Handler bridge,boolean handlerActive)  {
        try {
            String ruta = ERoutes.DOMAIN.getUrl() + ERoutes.NOTIFY.getUrl();
            URL address = new URL(ruta);
            HttpURLConnection cnn = (HttpURLConnection) address.openConnection();
            cnn.setRequestMethod("POST");
            cnn.setDoInput(true);
            cnn.setDoOutput(true);
            PrintStream out = new PrintStream(cnn.getOutputStream());
            out.print(parameter);
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
            String line = reader.readLine();
            String content = "";
            while (line != null) {
                content += line;
                line = reader.readLine();
            }
            reader.close();
            if(handlerActive) {
                Message message = new Message();
                message.obj = content;
                bridge.sendMessage(message);
            }
            return content;
        }catch (IOException e){
            Message message = new Message();
            message.obj = e.getMessage();
            bridge.sendMessage(message);
            return  "";
        }
    }


}
