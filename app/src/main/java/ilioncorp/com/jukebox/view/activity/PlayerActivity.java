package ilioncorp.com.jukebox.view.activity;

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

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.CreditsDAO;
import ilioncorp.com.jukebox.model.dao.HistorySongDAO;
import ilioncorp.com.jukebox.model.dao.ReproductionListDAO;
import ilioncorp.com.jukebox.model.dao.SessionDAO;
import ilioncorp.com.jukebox.model.dto.CreditsVO;
import ilioncorp.com.jukebox.model.dto.HistorySongVO;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.YoutubeConnector;
import ilioncorp.com.jukebox.view.fragment.TabReproducing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        Handler.Callback,View.OnClickListener {



    private YouTubePlayerView playerView;
    private View view;
    private Button btnSend;
    private ReproductionListVO song;
    private String idBar;
    private String nombreBar;
    private Handler bridge;
    private Handler bridgeCredits;
    private int credits;
    private CreditsDAO creditsDAO;
    private boolean endProcces = false;
    private boolean enviada = false;
    private boolean creditosObtenidos = false;
    private HistorySongVO historySongVO;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_player);
        bridge = new Handler(this);
        this.playerView = findViewById(R.id.player_view);
        this.playerView.initialize(YoutubeConnector.KEY, this);
        btnSend = findViewById(R.id.btnSendSong);
        btnSend.setOnClickListener(this::onClick);
        song = (ReproductionListVO) getIntent().getExtras().getSerializable("song");
        idBar = getIntent().getExtras().getString("idBar");
        historySongVO = new HistorySongVO();
        bridgeCredits = new Handler(msg -> {
            CreditsVO credits = (CreditsVO) msg.obj;
            this.credits = Integer.parseInt(credits.getCredits());
            creditosObtenidos = true;
            return false;

        });
        creditsDAO = new CreditsDAO(bridgeCredits,idBar, Constantes.userActive.getUserUID());
        checkSong();




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
        if(!enviada) {
            session.checkSession(bridge, idBar, "no");
            enviada = true;
        }
        else
            Toast.makeText(this,"La canción ya fue enviada",Toast.LENGTH_SHORT).show();

    }

    private void checkSong() {
        Handler bridge = new Handler(message -> {

            enviada = (boolean) message.obj;
            endProcces = true;
            return false;
        });
        ReproductionListDAO reproductionListDAO = new ReproductionListDAO(idBar
        ,bridge);
        reproductionListDAO.findSong(song.getVideo_id());
    }

    @Override
    public boolean handleMessage(Message message) {
        String[] array = (String[]) message.obj;
        String answer = array[0];
        if(endProcces) {
            if (answer.equals("active")) {
                if (checkCredits()) {
                    new ReproductionListDAO(idBar, TabReproducing.bridge, true).sendSong(song);
                    HistorySongDAO historySongDAO = new HistorySongDAO(null,Constantes.userActive.getUserUID());
                    historySongVO.setVideoIdSong(song.getVideo_id());
                    historySongVO.setIdUser(Constantes.userActive.getUserUID());
                    historySongVO.setNameSong(song.getName());
                    historySongVO.setNameBar(Constantes.establishmentVOActual.getName());
                    historySongVO.setDateSong(getFechaHora());
                    historySongVO.setStateSong("En espera");
                    historySongVO.setThumnailSong("https://img.youtube.com/vi/"+song.getVideo_id()+"/mqdefault.jpg");
                    historySongDAO.putSong(historySongVO);
                    Snackbar.make(view, "Canción Enviada", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Toast.makeText(this, "1 Credito Descontado", Toast.LENGTH_SHORT).show();
                    creditsDAO.takeFromCredit(credits - 1,Constantes.userActive.getUserUID());
                } else
                    Snackbar.make(view, "Creditos Insuficientes, por favor recarga", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

            } else if (answer.equals("inactive") || answer.equals(""))
                Snackbar.make(view, "Debes iniciar Sesión en el bar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            else if (answer.equals("vetoed"))
                Snackbar.make(view, "No puedes agendar musica, estas vetado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }else
            Toast.makeText(this,"Por favor espere un momento",Toast.LENGTH_SHORT).show();
        return false;
    }

    private String getFechaHora() {
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return hourdateFormat.format(date);
    }

    private boolean checkCredits() {
        if(this.credits>0)
            return true;
        return false;
    }
}
