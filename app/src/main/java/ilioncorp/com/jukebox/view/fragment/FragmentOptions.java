package ilioncorp.com.jukebox.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.activity.*;
import ilioncorp.com.jukebox.view.generic.GenericFragment;

@SuppressLint("ValidFragment")
public class FragmentOptions extends GenericFragment implements View.OnClickListener,Handler.Callback{

    private ImageView btnMap;
    private ImageView btnListBar;
    private ImageView btnScan;
    private ImageView btnHistory;
    private ImageView btnProfile;
    private ImageView btnBarActual;
    MainActivity context;
    private ImageView btnExit;
    private Handler bridge;
    public FragmentOptions(MainActivity context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_options,null);
        bridge = new Handler(this::handleMessage);
        this.btnHistory = view.findViewById(R.id.btnHistory);
        this.btnScan = view.findViewById(R.id.btnScan);
        this.btnListBar = view.findViewById(R.id.btnListBar);
        this.btnMap = view.findViewById(R.id.btnMap);
        this.btnProfile = view.findViewById(R.id.btnProfile);
        this.btnBarActual = view.findViewById(R.id.btnBarActual);
        this.btnBarActual.setOnClickListener(this::onClick);
        this.btnProfile.setOnClickListener(this::onClick);
        this.btnHistory.setOnClickListener(this::onClick);
        this.btnScan.setOnClickListener(this::onClick);
        this.btnListBar.setOnClickListener(this::onClick);
        this.btnMap.setOnClickListener(this::onClick);
        this.btnExit = view.findViewById(R.id.btnExit);
        this.btnExit.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnHistory:
                startActivity(new Intent(getContext(), HistoryActivity.class));
                break;
            case R.id.btnScan:
                startActivity(new Intent(getContext(), ScanCodeActivity.class));
                break;
            case R.id.btnListBar:
                startActivity(new Intent(getContext(), ListBarActivity.class));
                break;
            case R.id.btnMap:
                this.context.administrator.popBackStack();
                break;
            case R.id.btnExit:
                logout();
                break;
            case R.id.btnProfile:
                startActivity(new Intent(getContext(), ProfileActivity.class));
                break;
            case R.id.btnBarActual:
                if(Constantes.establishmentVOActual == null){
                    messageToast("No ha iniciado sesión con ningun bar");
                    return;
                }
                Intent pantalla = new Intent(getContext(),BarActivity.class);
                pantalla.putExtra("establishment", Constantes.establishmentVOActual);
                startActivity(pantalla);
                break;
        }
    }


    @Override
    public boolean handleMessage(Message message) {
        return false;
    }
}
