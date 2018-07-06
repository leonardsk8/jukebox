package ilioncorp.com.jukebox.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;


@SuppressLint("ValidFragment")
public class TabBar extends Fragment implements Runnable {

    EstablishmentVO establishment;
    public static FragmentManager fm;
    public static Fragment fragment;
    Handler bridge;
    int tab;

    public TabBar(EstablishmentVO establishment) {
        this.establishment = establishment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_bar, container, false);
        fm = getActivity().getSupportFragmentManager();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragment = new TabInfoBar(establishment);
        fm.beginTransaction().replace(R.id.contentBar, fragment).commit();
    }



    public void changeFragment(Fragment tab){
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.contentBar);
        getActivity().getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        fm.beginTransaction().replace(R.id.contentBar,tab).commit();
    }

    public void startFragment(Handler bridge, int i){
        this.bridge=bridge;
        this.tab = i;
        new Thread(this).start();
    }

    @Override
    public void run() {
        Fragment currentFragment=null;
        while(currentFragment!=null){
            currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.contentBar);
        }
        Message msg = new Message();
        msg.arg1 = this.tab;
        bridge.sendMessage(msg);

    }
}
