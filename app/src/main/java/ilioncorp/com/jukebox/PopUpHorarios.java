package ilioncorp.com.jukebox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class PopUpHorarios extends AppCompatActivity implements View.OnClickListener{

    private String[] horarios;
    private android.widget.TextView dayMonday;
    private android.widget.TextView dayTusday;
    private android.widget.TextView dayWednesday;
    private android.widget.TextView dayThursday;
    private android.widget.TextView dayFriday;
    private android.widget.TextView daySaturday;
    private android.widget.TextView daySunday;
    private android.widget.TextView titleHorario;
    private android.widget.TextView tvCerrarHorario;
    private android.support.v7.widget.CardView cvBtnCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_horarios);
        this.cvBtnCode = findViewById(R.id.cvBtnCode);
        this.tvCerrarHorario =  findViewById(R.id.tvCerrarHorario);
        this.titleHorario =  findViewById(R.id.titleHorario);
        this.daySunday =  findViewById(R.id.daySunday);
        this.daySaturday =  findViewById(R.id.daySaturday);
        this.dayFriday =  findViewById(R.id.dayFriday);
        this.dayThursday =  findViewById(R.id.dayThursday);
        this.dayWednesday =  findViewById(R.id.dayWednesday);
        this.dayTusday =  findViewById(R.id.dayTusday);
        this.dayMonday =  findViewById(R.id.dayMonday);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int) (dm.widthPixels * .8), (int) (dm.heightPixels * .6));
        horarios = getIntent().getStringArrayExtra("schedules");
        this.dayMonday.setText("Lunes: "+horarios[0]);
        this.dayTusday.setText("Martes: "+horarios[1]);
        this.dayWednesday.setText("Miercoles: "+horarios[2]);
        this.dayThursday.setText("Jueves: "+horarios[3]);
        this.dayFriday.setText("Viernes: "+horarios[4]);
        this.daySaturday.setText("Sabado: "+horarios[5]);
        this.daySunday.setText("Domingo: "+horarios[6]);
        this.tvCerrarHorario.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
