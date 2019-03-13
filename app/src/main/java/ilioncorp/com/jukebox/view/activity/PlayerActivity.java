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
import ilioncorp.com.jukebox.model.dao.*;
import ilioncorp.com.jukebox.model.dto.CreditsVO;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.model.dto.HistorySongVO;
import ilioncorp.com.jukebox.model.dto.ReproductionListVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.YoutubeConnector;
import ilioncorp.com.jukebox.view.fragment.TabListReproducing;
import ilioncorp.com.jukebox.view.fragment.TabReproducing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        Handler.Callback,View.OnClickListener {


    public static String APROBADA = "aprobadas";
    public static String ENVIADAS = "enviadas";
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
    private EstablishmentVO establishmentVO;
    private EstablishmentDAO establishmentDAO;
    private ReproductionListDAO reproductionListDAO;
    private ReportesDAO reportesDAO;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_player);
        bridge = new Handler(this);
        this.playerView = findViewById(R.id.player_view);
        this.playerView.initialize(YoutubeConnector.KEY, this);
        btnSend = findViewById(R.id.btnSendSong);
        btnSend.setOnClickListener(this::onClick);
        btnSend.setEnabled(false);
        song = (ReproductionListVO) getIntent().getExtras().getSerializable("song");
        idBar = getIntent().getExtras().getString("idBar");
        historySongVO = new HistorySongVO();
        bridgeCredits = new Handler(msg -> {
            CreditsVO credits = (CreditsVO) msg.obj;
            this.credits = Integer.parseInt(credits.getCredits());
            creditosObtenidos = true;
            return false;

        });
        Handler bridgeEstablishment = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                List<EstablishmentVO> listEstablishment = (List<EstablishmentVO>) message.obj;
                if(listEstablishment != null && listEstablishment.size()>0)
                establishmentVO = listEstablishment.get(0);
                return false;
            }
        });
        creditsDAO = new CreditsDAO(bridgeCredits,idBar, Constantes.userActive.getUserUID());
        establishmentDAO = new EstablishmentDAO(bridgeEstablishment);
        establishmentDAO.getBar(idBar);
        checkSong();
        reportesDAO = new ReportesDAO(idBar);



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

        }
        else
            Toast.makeText(this,"La canción ya fue enviada",Toast.LENGTH_SHORT).show();

    }

    private void checkSong() {
        Handler bridge = new Handler(message -> {
            btnSend.setEnabled(true);
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
                    String state="En espera";
                    HistorySongDAO historySongDAO = new HistorySongDAO(null,Constantes.userActive.getUserUID());
                    if(!establishmentVO.isRequestApproved()) {
                        song.setApproved(true);
                        state="Aprobada";
                        reportesDAO.putCanciones(Constantes.userActive.getUserUID(),song.getVideo_id(),
                                APROBADA);
                    }else{
                        reportesDAO.putCanciones(Constantes.userActive.getUserUID(),song.getVideo_id(),
                                ENVIADAS);
                    }
                    reproductionListDAO = new ReproductionListDAO(idBar,
                            TabListReproducing.bridge, true);
                    song.setNum((TabListReproducing.listSongs.size()+1)+"");
                    song.setSum(0);
                    reproductionListDAO.sendSong(song);
                    historySongVO.setVideoIdSong(song.getVideo_id());
                    historySongVO.setIdUser(Constantes.userActive.getUserUID());
                    historySongVO.setNameSong(song.getName());
                    historySongVO.setNameBar(Constantes.establishmentVOActual.getName());
                    historySongVO.setDateSong(getFechaHora());
                    historySongVO.setStateSong(state);
                    historySongVO.setThumnailSong("https://img.youtube.com/vi/"+song.getVideo_id()+"/mqdefault.jpg");
                    historySongVO.setIdBar(idBar);
                    historySongDAO.putSong(historySongVO);
                    Snackbar.make(view, "Canción Enviada", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    enviada = true;
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
