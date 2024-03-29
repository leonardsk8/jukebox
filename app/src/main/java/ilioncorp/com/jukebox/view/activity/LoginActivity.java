package ilioncorp.com.jukebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.view.generic.GenericActivity;

public class LoginActivity extends GenericActivity implements Handler.Callback,View.OnClickListener,
        Runnable,FacebookCallback<LoginResult>,OnCompleteListener<AuthResult>, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LOGIN_ACTIVITY";
    private android.widget.EditText etUser;
    private android.widget.EditText etPassword;
    private android.support.v7.widget.CardView cvBtnLogin;
    private android.widget.TextView tvForgotPassword;
    private android.widget.TextView tvRegisterHere;
    private LoginButton btnLoginFacebook;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private boolean byFacebook = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        callbackManager = CallbackManager.Factory.create();
        this.tvRegisterHere =  findViewById(R.id.tvRegisterHere);
        this.tvForgotPassword =  findViewById(R.id.tvForgotPassword);
        this.cvBtnLogin =  findViewById(R.id.cvBtnLogin);
        this.etPassword = findViewById(R.id.etPassword);
        this.signInButton = findViewById(R.id.btnLoginGoogle);
        this.signInButton.setOnClickListener(this);
        this.btnLoginFacebook = findViewById(R.id.login_buttonFB);
        this.etUser =  findViewById(R.id.etUser);
        this.tvRegisterHere.setOnClickListener(this::onClick);
        //this.tvRegisterHere.setVisibility(View.INVISIBLE);

        this.tvForgotPassword.setOnClickListener(this::onClick);
        this.cvBtnLogin.setOnClickListener(this);
        //        guardar();
        this.btnLoginFacebook.registerCallback(callbackManager,this);
        firebaseAuth = FirebaseAuth.getInstance();
        byFacebook = isLoggedIn();
        firebaseAuthListener = (firebaseAuth)->{
            FirebaseUser user =  firebaseAuth.getCurrentUser();
            if(user != null ){
                if(user.isEmailVerified())
                goToMainScreen();
                else if(byFacebook)
                    goToMainScreen();
            }
        };

        btnLoginFacebook.setReadPermissions("email", "public_profile");


    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.cvBtnLogin:
                checkLogin();
                break;
            case R.id.tvForgotPassword:
                startActivity(new Intent(this,RecuperarActivity.class));
                break;
            case R.id.tvRegisterHere:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.btnLoginGoogle:
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
                break;

        }
    }

    private void checkLogin() {
        showCharging("Iniciando Sesión",false);
        String email = etUser.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(!email.isEmpty() & !password.isEmpty())
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        messageToast("EXITO");
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if(firebaseUser.isEmailVerified()) {
                            goToMainScreen();
                            hideCharging();
                        }
                        else {
                            hideCharging();
                            dialog("se ha enviado un correo de verificación a: " + firebaseUser.getEmail());
                            dialog("Debes verificar tu correo electronico\n o puedes iniciar sesión con google o facebook");
                            firebaseUser.sendEmailVerification();

                        }
                    } else {
                        hideCharging();
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        messageToast("Error al iniciar sesión"+task.getException());

                    }

                });
        else {
            messageToast("Complete los campos");
            hideCharging();
        }
    }



    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    @Override
    public void run() {

    }

    //FACEBOOKSTARTMETHODS

    private void handleFacebookAccessToken(AccessToken accessToken) {
        showCharging("Cargando");
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this,this::onComplete);
    }
    @Override
    public void onSuccess(LoginResult loginResult) {
        handleFacebookAccessToken(loginResult.getAccessToken());
        byFacebook=true;
    }

    @Override
    public void onCancel() {
        Toast.makeText(this,"CANCEL",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();
    }
    //FACEBOOKEND

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SIGN_IN_CODE:
                if (requestCode == SIGN_IN_CODE){
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result);
                }
                break;
            default:
                callbackManager.onActivityResult(requestCode,resultCode,data);
                break;
        }

    }

    @Override
    protected void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            showCharging("Cargando");
            firebaseAuthWithGoogle(result.getSignInAccount());
        } else {
            Log.e("ERROR FIREBASE",result.toString());
            Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this,this::onComplete);
    }

    private void goToMainScreen() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuthListener != null)
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(!task.isSuccessful()) {
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            Log.e("Error de inicio",task.getException()+" \n "+task.toString());
        }
        hideCharging();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Error conection",connectionResult.getErrorMessage());
        messageToast("Error de conección"+connectionResult.getErrorMessage()+
                " "+connectionResult.toString());
    }
}
