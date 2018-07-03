package ilioncorp.com.jukebox.view.generic;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.view.activity.LoginActivity;
import ilioncorp.com.jukebox.view.activity.MainActivity;

public abstract class GenericActivity  extends AppCompatActivity {
    private AlertDialog alert;
    public static final int SIGN_IN_CODE = 777;
    public void showCharging(String title){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        View item = LayoutInflater.from(this).inflate(R.layout.item_cargando,null);
        alert.setView(item);
        alert.setCancelable(false);
        this.alert=alert.create();
        this.alert = alert.show();
    }
    public void hideCharging(){
        if (alert == null){
            return;
        }
        alert.dismiss();

    }
    public void message (String message){
        Toast.makeText(this,message + "" , Toast.LENGTH_SHORT).show();
    }
    protected void messageSnackBar(String s, View view) {
        Snackbar.make(view,s,Snackbar.LENGTH_SHORT).show();
    }
    public void goLoginScreen() {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void goMainScreen() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }





    protected void handleSignInResult(GoogleSignInResult result) {


    }



}
