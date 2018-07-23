package ilioncorp.com.jukebox.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import ilioncorp.com.jukebox.model.dao.CreditsDAO;
import ilioncorp.com.jukebox.model.dao.ReproductionListDAO;
import ilioncorp.com.jukebox.model.dao.SessionDAO;
import ilioncorp.com.jukebox.model.dto.CreditsVO;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.view.YoutubeConnector;
import ilioncorp.com.jukebox.view.fragment.TabReproducing;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        Handler.Callback,View.OnClickListener {



    private YouTubePlayerView playerView;
    private View view;
    private Button btnSend;
    private ReproductionListVO song;
    private String idBar;
    private Handler bridge;
    private Handler bridgeCredits;
    private int credits;
    private CreditsDAO creditsDAO;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_player);
        bridge = new Handler(this);
        this.playerView = findViewById(R.id.player_view);
        this.playerView.initialize(YoutubeConnector.KEY, this);
        btnSend = findViewById(R.id.btnSendSong);
        btnSend.setOnClickListener(this);
        song = (ReproductionListVO) getIntent().getExtras().getSerializable("song");
        idBar = getIntent().getExtras().getString("idBar");
        bridgeCredits = new Handler(msg -> {
            int x;
            CreditsVO credits = (CreditsVO) msg.obj;
            this.credits = Integer.parseInt(credits.getCredits());
            return false;

        });
        creditsDAO = new CreditsDAO(bridgeCredits);
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
        SessionDAO session = new SessionDAO();
        this.view = view;
        session.checkSession(bridge,idBar);

    }

    @Override
    public boolean handleMessage(Message message) {
        String answer = (String)message.obj;
        if(answer.equals("active")) {
            if(checkCredits()) {
                new ReproductionListDAO(idBar, TabReproducing.bridge, true).sendSong(song);
                Snackbar.make(view, "Canción Enviada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Toast.makeText(this,"1 Credito Descontado",Toast.LENGTH_SHORT).show();
                creditsDAO.takeFromCredit(credits-1);
            }
            else
                Snackbar.make(view, "Creditos Insuficientes, por favor recarga", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

        }
        else if(answer.equals("inactive") || answer.equals(""))
            Snackbar.make(view, "Debes iniciar Sesión en el bar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        else if (answer.equals("vetoed"))
            Snackbar.make(view, "No puedes agendar musica, estas vetado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        return false;
    }

    private boolean checkCredits() {
        if(this.credits>0)
            return true;
        return false;
    }
}
