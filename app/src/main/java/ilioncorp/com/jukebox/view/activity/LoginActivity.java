package ilioncorp.com.jukebox.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Arrays;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.view.generic.GenericActivity;

public class LoginActivity extends GenericActivity implements Handler.Callback,View.OnClickListener,
        Runnable,FacebookCallback<LoginResult>,OnCompleteListener<AuthResult>, GoogleApiClient.OnConnectionFailedListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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
        this.tvRegisterHere.setOnClickListener(this);
        this.tvForgotPassword.setOnClickListener(this);
        this.cvBtnLogin.setOnClickListener(this);
//        guardar();
        this.btnLoginFacebook.registerCallback(callbackManager,this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = (firebaseAuth)->{
            FirebaseUser user =  firebaseAuth.getCurrentUser();
            if(user != null){
                goToMainScreen();
            }
        };

        btnLoginFacebook.setReadPermissions(Arrays.asList("email"));

    }
    /*private void guardar() {
        try {
            String ruta =Environment.getExternalStorageDirectory() + "/google-services.json";
            String information = "{"+
                    "\"type\": \"service_account\","+
                    "\"project_id\": \"dispositivo1-202101\","+
                    "\"private_key_id\": \"f66e325140e4f2e27816e63f741781655bfa330c\","+
                    "\"private_key\": \"-----BEGIN PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCu3BUnCiHcMlbXwC3t6m5CU05AFN9ZVaN4s8uGP/yVbhlHdeVb8Zg8MQb6PRg9UmPTA4nC8NGHwhIWk9dTm2ND9DxIupmotLqsWf87i5hu5LM3PqLAZebFw/lXPXRGEt9I0WDzq8PtbdfgVEBWbWmX83eYbq11hL01ox650EY0R74bF9z/SHqyKbXe7bWhoPBFJxUF74ZYG6NPDL9ClSUlICdfqFEhYDwPMF2rt6QpSXhvD0e6lpExFpzIWzqQ7Nn1UU0cOtIFwUJQK9D54n/P1LeH8oxmOvRK7iRV8ziFiqpaolpt7LZe/WllMI0eYt+deOvRTg/bYr3r38znVtnVAgMBAAECggEAJ/R0BOKk0ArUmyH3nLiHjzI33B6o1mHiI495vuhaD88Dibefu/2dWoudW4qgQkrUMoBJdzjNt+aZeX04o4sLCJkDOV8iA1GgogdCcdm48AXkCYiPacUfEStxiwSoz+Y6DQqCy8sAzvzdag32UYlYqnbgaf1IN2UoUtw+BhlYIVOtu8u2RLcSDTHSJJ1dRCWOGZWsX2SlPSrbCTj6UkkqBeV0n2MXMtw8Xk2ST/k0pyd6VBYW2sd2uIft3HSTArkHOciuhl1NBmp9Zq+8I4eIw3xJzeMGL9ZDfDDPnjdixS/W45r/dCFq2Be2JXtxGS1MC+Q179mzHF2FibTw6sp1YQKBgQDysCoWiN5TsQb5DCsX+8uZxYvI9Qo2HCu9RFUzYwO0CC6M/Zo8rFf6YtCUJWErLNQEPjplYj1cCibrB0OnjE9A2CpNrLfeL8g6lkgB6sDPa755rhlObZxavRVjKuhsidXiH+c/3ywdMK7CCc607g+28VkT+qFI+Vo/3A2IlhezHQKBgQC4c3gBbv1UHTDnWT05bV8AQEF/Ow1W54lkflBvqWth+5ma++qUkbGDBNe3YKJUiHumsoIgpGHdx2Fe1g92lW2UJHQb2NIYrkTeD+1vtwL7KjOoTK4B1TDPKCAc7znk6k9a8xZFbhpQbqk4RPF8NMwI8MYulA+wrNs+Qj5vMlUMGQKBgQCiAmQ2Yuw4ZodHHo5uFlNWtf+ZCavlb2zrjoBqK4E/X+7jhDDArDSXG/wtyb5SOjzXEEuffrODi+trHpBVe1805Jlo/RJJqxA5vDUZwoMwdLbVOBoAtz+Xe36Zkv1R57n2mGGhy/QBEIy5UB7DveCTug5DPh8JPhy3YMlQExKDKQKBgHGD9q4DLXrvwCIRBKHsEpRn9ycjI5GBS0gCiOQ/guu5WQ5NzU+D3phAibW8jgqyOE8d+wG1yNkhlGY2HSwgyGBsktx0ctBnp9B2Qu0G15nVnhcMoeZU2wYSrri/7YRZQ184zVMLcLxhbllH7MpA//TcoUsXKhTUPNWzFROACWE5AoGAVWQJsksNl8qsb29TWhIur9qtlppRellUKOZwdoyiF1eRUQ6dEFtjnQ7zTamW5Jpsleyg0myAPnAGWuU4/hHjL0SonPhscnOrlX0XhqRC592VwZZ+0uP2nkrD0gQDrvoxDk2gsYs0G79kvQz0BoKqIyUIPc3LTyQAv84SOee3xTY=-----END PRIVATE KEY-----\","+
                    "\"client_email\": \"firebase-adminsdk-0bilz@dispositivo1-202101.iam.gserviceaccount.com\","+
                    "\"client_id\": \"103616341977185591233\","+
                    "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\","+
                    "\"token_uri\": \"https://accounts.google.com/o/oauth2/token\","+
                    "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\","+
                    "\"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-0bilz%40dispositivo1-202101.iam.gserviceaccount.com\""+
                    "}";
            PrintStream escribe = new PrintStream(ruta);
            escribe.println(information);
            Toast t = Toast.makeText(this, "Los datos fueron grabados",
                    Toast.LENGTH_SHORT);
            t.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    private void handleFacebookAccessToken(AccessToken accessToken) {
        showCharging("Cargando");
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.cvBtnLogin:
                /*startActivity(new Intent(this,MainActivity.class));
                finish();
                */
                break;
            case R.id.tvForgotPassword:
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


    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    @Override
    public void run() {

    }

    //FACEBOOKSTARTMETHODS
    @Override
    public void onSuccess(LoginResult loginResult) {
        handleFacebookAccessToken(loginResult.getAccessToken());
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
        Log.e("Result Activity login","codigo: "+requestCode);
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
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this,this);
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
        if(!task.isSuccessful())
            Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
        hideCharging();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Error conection",connectionResult.getErrorMessage());
    }
}
