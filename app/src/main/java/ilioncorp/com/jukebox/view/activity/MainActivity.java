package ilioncorp.com.jukebox.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.UserDAO;
import ilioncorp.com.jukebox.model.dto.UserVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.fragment.FragmentMap;
import ilioncorp.com.jukebox.view.fragment.FragmentOptions;
import ilioncorp.com.jukebox.view.generic.GenericActivity;


public class MainActivity extends GenericActivity implements View.OnClickListener,Runnable,GoogleApiClient.OnConnectionFailedListener{

    private android.widget.ImageView imOptions;

    public FragmentManager administrator;
    private FragmentMap maps;
    private FragmentOptions options;
    private int hilo;
    LocationManager mlocManager;
    UserDAO userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.imOptions =  findViewById(R.id.imOptions);
        imOptions.setOnClickListener(this);
        maps = new FragmentMap();
        mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        administrator = getSupportFragmentManager();
        options = new FragmentOptions(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userdao = new UserDAO(this);
        userdao.checkProfileUser();
        if (user != null) {
            checkPer();
        } else {
            goLoginScreen();
        }





    }



    private void checkPer() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }else {
            start();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Result Activity","codigo: "+requestCode+" resul code: "+resultCode);
        if(requestCode==65637){
            final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gpsEnabled) {
               Toast.makeText(this,"Debe activar el GPS",Toast.LENGTH_SHORT).show();
               finish();
            }else {
                startActivity(new Intent(this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }
    }
    private void start(){
        administrator.beginTransaction().add(R.id.content,maps).addToBackStack(null).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        Log.e("ResultPermisos","entro: "+requestCode);
        switch (requestCode) {
            case 1000: {
                Log.e("requestCode","entro: "+requestCode);
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hilo = 1;

                } else {
                    hilo = 0;
                    Toast.makeText(this,"No se garantizo el permiso la aplicación se cerrara",Toast.LENGTH_SHORT).show();

                }
                new Thread(this).start();
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.imOptions:
                if (options != null) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
                    if (!currentFragment.getClass().getName().equalsIgnoreCase(options.getClass().getName())) {
                        //currentFragment no concide con maps
                        loadFragment(options);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (administrator.getBackStackEntryCount() > 1 ) {
            administrator.popBackStack();
        }
        else
            super.onBackPressed();
    }
    private void loadFragment(Fragment newFragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, newFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void run() {
        switch (hilo) {
            case 1:
                start();
                break;
            case 0:

            finish();
            break;
        }
    }
}
