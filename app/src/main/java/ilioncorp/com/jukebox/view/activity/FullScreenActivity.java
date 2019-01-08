package ilioncorp.com.jukebox.view.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.view.adapter.FullSizeAdapter;

public class FullScreenActivity extends AppCompatActivity {

    ViewPager viewPager;
    String[] images;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        if(savedInstanceState == null){
            Intent i =getIntent();
            images = i.getStringArrayExtra("IMAGES");
            position = i.getIntExtra("POSITION",0);
        }
        viewPager = findViewById(R.id.viewPager);
        FullSizeAdapter fullSizeAdapter = new FullSizeAdapter(this,images);
        viewPager.setAdapter(fullSizeAdapter);
        viewPager.setCurrentItem(position,true);
    }


}
