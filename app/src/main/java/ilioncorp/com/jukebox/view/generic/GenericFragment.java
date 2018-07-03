package ilioncorp.com.jukebox.view.generic;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.view.activity.LoginActivity;

/**
 * Created by Administrador on 03/04/2018.
 */

public abstract class GenericFragment extends Fragment {

    private AlertDialog alert;

    public void hideCharging(){
        if (alert == null){
            return;
        }
        alert.dismiss();
    }
    public void showCharging(String title, Context contexto){
        AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
        alert.setTitle(title);
        View item = LayoutInflater.from(getContext()).inflate(R.layout.item_cargando,null);
        alert.setView(item);
        alert.setCancelable(false);
        this.alert=alert.create();
        this.alert = alert.show();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
    private void goLoginScreen() {
        Intent intent = new Intent(getContext(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
