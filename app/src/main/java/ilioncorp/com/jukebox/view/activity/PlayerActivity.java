package ilioncorp.com.jukebox.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.ReproductionListDAO;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.view.YoutubeConnector;
import ilioncorp.com.jukebox.view.fragment.TabReproducing;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {



    private YouTubePlayerView playerView;

    private Button btnSend;
    private ReproductionListVO song;
    private String idBar;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_player);
        this.playerView =  findViewById(R.id.player_view);
       this.playerView.initialize(YoutubeConnector.KEY, this);
       btnSend = findViewById(R.id.btnSendSong);
       btnSend.setOnClickListener(this);
       song = (ReproductionListVO) getIntent().getExtras().getSerializable("song");
       idBar= getIntent().getExtras().getString("idBar");
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean restored) {
        if(!restored){
            player.cueVideo(song.getVideo_id());
        }
    }

    @Override
    public void onClick(View view) {
        new ReproductionListDAO("",idBar, TabReproducing.bridge,true).sendSong(song);
        Snackbar.make(view, "Canción Enviada", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
