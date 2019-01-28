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



}
