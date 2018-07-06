package ilioncorp.com.jukebox.model.generic;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

public interface ItemClickListener {
    void onItemClick(View v, int pos, ImageView img);
}



