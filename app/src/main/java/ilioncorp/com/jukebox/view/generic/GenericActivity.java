package ilioncorp.com.jukebox.view.generic;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.view.activity.LoginActivity;
import ilioncorp.com.jukebox.view.activity.MainActivity;

public abstract class GenericActivity  extends AppCompatActivity {
    private AlertDialog alert;
    AlertDialog.Builder alert2;
    public static final int SIGN_IN_CODE = 777;
    public void showCharging(String title){
        dialogBuilder(title);
        alert2.setCancelable(false);
        this.alert=alert2.create();
        this.alert = alert2.show();
    }
    public void showCharging(String title,boolean cancelable){
        dialogBuilder(title);
        alert2.setCancelable(cancelable);
        this.alert=alert2.create();
        this.alert = alert2.show();
    }

    private void dialogBuilder(String title) {
        alert2 = new AlertDialog.Builder(this);
        alert2.setTitle(title);
        View item = LayoutInflater.from(this).inflate(R.layout.item_cargando,null);
        alert2.setView(item);
    }

    public void hideCharging(){
        if (alert == null){
            return;
        }
        alert.dismiss();

    }
    public void messageToast (String message){
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


    public void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.create();
        builder.show();
    }

    protected void handleSignInResult(GoogleSignInResult result) {


    }



}
