package ilioncorp.com.jukebox.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import ilioncorp.com.jukebox.R;

public class RecuperarActivity extends AppCompatActivity {

    private EditText inputEmail;

    private Button btnReset, btnBack;

    private FirebaseAuth auth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recuperar);
        inputEmail =  findViewById(R.id.email);
        btnReset =  findViewById(R.id.btn_reset_password);
        btnBack =  findViewById(R.id.btn_back);
        progressBar =  findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        btnBack.setOnClickListener(v -> finish());
        btnReset.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Ingrese el email registrado", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RecuperarActivity.this, "Hemos enviado un " +
                                    "correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RecuperarActivity.this, "Fallo envio" +
                                    "de email", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    });
        });
    }

}