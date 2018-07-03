package ilioncorp.com.jukebox.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.UserDAO;
import ilioncorp.com.jukebox.model.dto.UserVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private com.facebook.drawee.view.SimpleDraweeView imageProfile;
    private android.widget.EditText etNameProfile;
    private android.widget.EditText etEmailProfile;
    private android.widget.DatePicker dpBirthdayProfile;
    private android.widget.Spinner spinnerGenderProfile;
    private android.widget.EditText etAboutProfile;
    private android.support.v7.widget.CardView btnSaveProfie;
    private FirebaseUser user;
    private UserDAO daoUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profile);

        this.btnSaveProfie =  findViewById(R.id.btnSaveProfie);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.etAboutProfile =  findViewById(R.id.etAboutProfile);
        this.spinnerGenderProfile =  findViewById(R.id.spinnerGenderProfile);
        this.dpBirthdayProfile =  findViewById(R.id.dpBirthdayProfile);
        this.etEmailProfile =  findViewById(R.id.etEmailProfile);
        this.etNameProfile = findViewById(R.id.etNameProfile);
        this.imageProfile = findViewById(R.id.imageProfile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        daoUser = new UserDAO(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenderProfile.setAdapter(adapter);
        UserVO userVO = Constantes.userActive;
        if(userVO==null)
            userVO = new UserVO();
        if(user!=null){
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            this.imageProfile.setImageURI(photoUrl);
            etNameProfile.setText(name);
            etEmailProfile.setText(email);
            etEmailProfile.setEnabled(false);
            etNameProfile.setEnabled(false);
            if(userVO.isUserProfileComplete()){
                String[] date = userVO.getUserBirthday().split("/");
                dpBirthdayProfile.init(Integer.parseInt(date[2]),Integer.parseInt(date[0])-1,Integer.parseInt(date[1]),null);
                etAboutProfile.setText(userVO.getUserAbout());
                spinnerGenderProfile.setSelection(0);
            }
        }

        btnSaveProfie.setOnClickListener(this);

    }
    public String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onClick(View view) {
        String date = checkDigit(dpBirthdayProfile.getMonth()+1)+"/"+checkDigit(dpBirthdayProfile
        .getDayOfMonth())+"/"+dpBirthdayProfile.getYear();
        String about;
        if(!etAboutProfile.getText().toString().isEmpty())
             about = etAboutProfile.getText().toString();
        else
             about = "";
        if(!spinnerGenderProfile.getSelectedItem().toString().isEmpty()){
            UserVO userObject = new UserVO();
            userObject.setUserUID(user.getUid());
            userObject.setUserName(user.getDisplayName());
            userObject.setUserEmail(user.getEmail());
            userObject.setUserBirthday(date);
            userObject.setUserUrlImage("");
            userObject.setUserProfileComplete(true);
            userObject.setUserAbout(about);
            daoUser.updateUser(userObject);
            Toast.makeText(this,"EXITO",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
