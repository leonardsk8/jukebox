package ilioncorp.com.jukebox.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.view.activity.ChargeAccountActivity;
import ilioncorp.com.jukebox.view.activity.HistoryActivity;
import ilioncorp.com.jukebox.view.activity.ListBarActivity;
import ilioncorp.com.jukebox.view.activity.ProfileActivity;
import ilioncorp.com.jukebox.view.activity.ScanCodeActivity;
import ilioncorp.com.jukebox.view.generic.GenericFragment;
import ilioncorp.com.jukebox.view.activity.MainActivity;

@SuppressLint("ValidFragment")
public class FragmentOptions extends GenericFragment implements View.OnClickListener{

    private android.widget.ImageButton btnMap;
    private android.widget.ImageButton btnChargeAccount;
    private android.widget.ImageButton btnListBar;
    private android.widget.ImageButton btnScan;
    private android.widget.ImageButton btnHistory;
    private android.widget.ImageButton btnProfile;
    MainActivity context;
    private ImageButton btnExit;

    public FragmentOptions(MainActivity context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_options,null);
        this.btnHistory = view.findViewById(R.id.btnHistory);
        this.btnScan = view.findViewById(R.id.btnScan);
        this.btnListBar = view.findViewById(R.id.btnListBar);
        this.btnChargeAccount = view.findViewById(R.id.btnChargeAccount);
        this.btnMap = view.findViewById(R.id.btnMap);
        this.btnProfile = view.findViewById(R.id.btnProfile);
        this.btnProfile.setOnClickListener(this::onClick);
        this.btnHistory.setOnClickListener(this::onClick);
        this.btnScan.setOnClickListener(this::onClick);
        this.btnListBar.setOnClickListener(this::onClick);
        this.btnChargeAccount.setOnClickListener(this::onClick);
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
            case R.id.btnChargeAccount:
                startActivity(new Intent(getContext(), ChargeAccountActivity.class));
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
        }
    }



}
