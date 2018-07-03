package ilioncorp.com.jukebox.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.view.adapter.MenuListAdapter;
import ilioncorp.com.jukebox.view.adapter.PhotoListAdapter;
@SuppressLint("ValidFragment")
public class TabInfoBar extends Fragment {

    EstablishmentVO establishment;
    private android.widget.TextView tvTitle;
    private android.widget.TextView tvDescription;
    private ImageView ivMonday;
    private ImageView ivTuesday;
    private ImageView ivWednesday;
    private ImageView ivThursday;
    private ImageView ivFriday;
    private ImageView ivSaturday;
    private ImageView ivSunday;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvEmail;
    private TextView tvGenders;



    public TabInfoBar(EstablishmentVO establishment) {
        this.establishment=establishment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_info_bar, container, false);

        this.tvGenders =  rootView.findViewById(R.id.tvGenders);
        this.tvEmail = rootView.findViewById(R.id.tvEmail);
        this.tvAddress = rootView.findViewById(R.id.tvAddress);
        this.tvPhone = rootView.findViewById(R.id.tvPhone);
        this.tvDescription = rootView.findViewById(R.id.tvDescription);
        this.tvTitle = rootView.findViewById(R.id.tvTitle);
        this.ivSunday = rootView.findViewById(R.id.ivSunday);
        this.ivSaturday = rootView.findViewById(R.id.ivSaturday);
        this.ivFriday = rootView.findViewById(R.id.ivFriday);
        this.ivThursday = rootView.findViewById(R.id.ivThursday);
        this.ivWednesday = rootView.findViewById(R.id.ivWednesday);
        this.ivTuesday = rootView.findViewById(R.id.ivTuesday);
        this.ivMonday = rootView.findViewById(R.id.ivMonday);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] days = establishment.getSchedules().split("-");
        for (int i=0;i<=6;i++)
            if(days[i].contains("1"))
                openDays(i);
            else{
                closeDays(i);
            }
        this.tvTitle.setText(establishment.getName());
        this.tvDescription.setText(establishment.getDescription());
        this.tvAddress.setText(establishment.getAddress());
        this.tvEmail.setText(establishment.getEmail());
        this.tvGenders.setText(establishment.getGenders());
        this.tvPhone.setText(establishment.getPhone());
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment;
        Fragment fragmentPhotos;

        //if (fragment == null) {
            fragment = new MenuListAdapter(establishment.getMenuList());
            fragmentPhotos = new PhotoListAdapter(establishment);
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
            fm.beginTransaction().add(R.id.fragmentContainerPhotos,fragmentPhotos).commit();
        //}

    }

    private void closeDays(int i) {
        switch (i){
            case 0:
                ivMonday.setImageResource(R.drawable.close);
                break;
            case 1:
                ivTuesday.setImageResource(R.drawable.close);
                break;
            case 2:
                ivWednesday.setImageResource(R.drawable.close);
                break;
            case 3:
                ivThursday.setImageResource(R.drawable.close);
                break;
            case 4:
                ivFriday.setImageResource(R.drawable.close);
                break;
            case 5:
                ivSaturday.setImageResource(R.drawable.close);
                break;
            case 6:
                ivSunday.setImageResource(R.drawable.close);
                break;
        }
    }

    private void openDays(int i) {
        switch (i){
            case 0:
                ivMonday.setImageResource(R.drawable.open);
                break;
            case 1:
                ivTuesday.setImageResource(R.drawable.open);
                break;
            case 2:
                ivWednesday.setImageResource(R.drawable.open);
                break;
            case 3:
                ivThursday.setImageResource(R.drawable.open);
                break;
            case 4:
                ivFriday.setImageResource(R.drawable.open);
                break;
            case 5:
                ivSaturday.setImageResource(R.drawable.open);
                break;
            case 6:
                ivSunday.setImageResource(R.drawable.open);
                break;
        }
    }
}
