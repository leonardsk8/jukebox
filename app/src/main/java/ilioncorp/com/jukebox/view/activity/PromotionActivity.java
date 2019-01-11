package ilioncorp.com.jukebox.view.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.PromotionsDAO;
import ilioncorp.com.jukebox.model.dao.PromotionsUserDAO;
import ilioncorp.com.jukebox.model.dto.PromotionsUserVO;
import ilioncorp.com.jukebox.model.dto.PromotionsVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.generic.GenericActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PromotionActivity extends GenericActivity implements View.OnClickListener,Handler.Callback{

    private FirebaseUser user;
    private TextView tvCodePromotion;
    private TextView tvExpirationDate;
    private TextView tvDescription;
    private TextView tvCountPro;
    private TextView tvTimesHas;
    private String finalCodePromotion;
    private PromotionsDAO promotionsDAO;
    private PromotionsUserDAO promotionsUserDAO;
    private android.support.v7.widget.CardView cvBtnCode;
    private String idBar;
    private PromotionsVO promotionsVO;
    private PromotionsUserVO promotionsUserVO;
    private Handler bridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String fechaSistema=formateador.format(fechaActual);
        bridge = new Handler(this::handleMessage);
        promotionsVO = (PromotionsVO) getIntent().getExtras().getSerializable("promotions");
        boolean vencido = verificarVencimiento(promotionsVO.getPro_expiration_date(),fechaSistema);
        idBar = getIntent().getStringExtra("idBar");
        user = FirebaseAuth.getInstance().getCurrentUser();
        tvCodePromotion = findViewById(R.id.tvCodePromotion);
        cvBtnCode = findViewById(R.id.cvBtnCode);
        tvExpirationDate = findViewById(R.id.tvExpirationDatePro);
        tvDescription = findViewById(R.id.tvDescriptionPro);
        tvCountPro = findViewById(R.id.tvCountPro);
        cvBtnCode.setOnClickListener(this::onClick);
        tvTimesHas = findViewById(R.id.tvTimeshasPro);
        if(!vencido) {
            init();
            promotionsUserDAO = new PromotionsUserDAO(bridge, idBar, this);
            promotionsUserDAO.getPromotion(finalCodePromotion);
        }
        else{
            tvCodePromotion.setText("Vencido");
            messageToast("La promoción ha vencido");

        }
        /*PromotionsUserVO promotionsUserVO = new PromotionsUserVO(promotionsVO.getPro_code(),promotionsVO.getPro_times()
                ,);*/
        //promotionsDAO.promotionCreate(promotionsUserVO);








    }
    /**
     * SE DEBE TENER EN CUENTA QUE EL CODIGO SE GENERA CON LOS ULTIMOS 3 DIGITOS DEL BAR, LOS ULTIMOS 3 DEL USUARIO
     * Y EL CODIGO ASIGNADO POR EL ADMINISTRADOR A LA PROMOCIÓN*/
    private void init() {
        finalCodePromotion = idBar.substring(idBar.length()-3);
        finalCodePromotion+= user.getUid().substring(user.getUid().length()-3);
        finalCodePromotion+=  promotionsVO.getPro_code();
        tvCodePromotion.setText(finalCodePromotion);
        tvCountPro.setText("Veces que puedes redimir: "+promotionsVO.getPro_limit());
        tvExpirationDate.setText("Vencimiento: "+promotionsVO.getPro_expiration_date());
        tvDescription.setText("Descripción: "+promotionsVO.getPro_description());
    }

    private boolean verificarVencimiento(String fecha1, String fechaActual) {
        boolean resultado=false;
        try {
            /**Obtenemos las fechas enviadas en el formato a comparar*/
            SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date fechaDate1 = formateador.parse(fecha1);
            Date fechaDate2 = formateador.parse(fechaActual);

            if ( fechaDate1.before(fechaDate2) ){
                resultado= true;
            }else{
                if ( fechaDate2.before(fechaDate1) ){
                    resultado= false;
                }else{
                    resultado= false;
                }
            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  "+e.getMessage());
        }
        return resultado;
    }


    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public boolean handleMessage(Message message) {
        promotionsUserVO = (PromotionsUserVO) message.obj;
        if(promotionsUserVO!=null)
            tvTimesHas.setText(getString(R.string.timeHas)+" "+promotionsUserVO.getProU_times());
        else {
            promotionsUserVO = new PromotionsUserVO();
            promotionsUserVO.setProU_code(finalCodePromotion);
            promotionsUserVO.setProU_idBar(idBar);
            promotionsUserVO.setProU_limit(promotionsVO.getPro_limit());
            promotionsUserVO.setProU_times("0");
            promotionsUserVO.setProU_userId(user.getUid());
            tvTimesHas.setText("Veces que has redimido este cupon 0");
        }
        promotionsUserDAO.promotionCreate(promotionsUserVO);
        return false;
    }
}
