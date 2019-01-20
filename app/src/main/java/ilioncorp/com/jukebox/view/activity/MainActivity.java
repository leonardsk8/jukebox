package ilioncorp.com.jukebox.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.CreditsDAO;
import ilioncorp.com.jukebox.model.dao.EstablishmentDAO;
import ilioncorp.com.jukebox.model.dao.SessionDAO;
import ilioncorp.com.jukebox.model.dao.UserDAO;
import ilioncorp.com.jukebox.model.dto.CreditsVO;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.model.dto.SessionUserVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.fragment.FragmentMap;
import ilioncorp.com.jukebox.view.fragment.FragmentOptions;
import ilioncorp.com.jukebox.view.generic.GenericActivity;

import java.util.ArrayList;


public class MainActivity extends GenericActivity implements
        Handler.Callback,
        View.OnClickListener,
        Runnable,
        GoogleApiClient.OnConnectionFailedListener {

    private android.widget.ImageView imOptions;
    public FragmentManager administrator;
    private FragmentMap maps;
    private FragmentOptions options;
    private int hilo;
    private LocationManager mlocManager;
    private UserDAO userdao;
    private Handler bridge;
    private SessionDAO session;
    private SessionUserVO sessionUserVO ;
    private ArrayList<EstablishmentVO> listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.imOptions =  findViewById(R.id.imOptions);
        imOptions.setOnClickListener(this::onClick);
        maps = new FragmentMap();
        mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        bridge = new Handler(this::handleMessage);

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
        if(Constantes.establishmentVOActual == null) {
            Handler puente = new Handler(message -> {
                sessionUserVO = (SessionUserVO) message.obj;
                Constantes.idBarSessionActual = sessionUserVO.getEstablishmentId();
                getEstablishment();
                return false;
            });
            session = new SessionDAO(puente, this);
            session.checkSessionUser(user.getUid());
        }




    }

    /**METODO QUE TRAE EL BAR EN EL CUAL SE HA INICIADO SESIÓN PARA POSTERIORMENTE PODER ACCEDER DESDE EL MENU DE INICIO
     *
     * */
    private void getEstablishment() {
        EstablishmentDAO dao = new EstablishmentDAO(new Handler(message -> {
            listItems = (ArrayList<EstablishmentVO>) message.obj;
            Constantes.establishmentVOActual = listItems.get(0);
            return false;
        }));
        dao.getBar(Constantes.idBarSessionActual);
    }


    /**PERMISOS DE UBICACIÓN*/
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
    /**AGREGA EL FRAGMENT DEL MAPA*/
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
                    } else
                        this.administrator.popBackStack();

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
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (currentFragment == null)
            finish();
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

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }
}
