package ilioncorp.com.jukebox.view.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ilioncorp.com.jukebox.BuildConfig;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.UserVO;
import ilioncorp.com.jukebox.view.generic.GenericActivity;
import pl.droidsonroids.gif.GifImageView;



public class RegisterActivity extends GenericActivity implements Handler.Callback,
        View.OnClickListener,Runnable,DialogInterface.OnClickListener{

    private android.widget.EditText etName;
    private android.widget.EditText etSurname;
    private android.widget.EditText etEmail;
    private android.widget.EditText etCellphone;
    private android.widget.DatePicker dpBirthday;
    private android.support.v7.widget.CardView cvBtnRegister;
    private com.facebook.drawee.view.SimpleDraweeView selectImage;
    private EditText etPassword;

    public static final int MY_REQUEST_CAMERA   = 10;
    public static final int MY_REQUEST_WRITE_CAMERA   = 11;
    public static final int CAPTURE_CAMERA   = 12;

    public static final int MY_REQUEST_READ_GALLERY   = 13;
    public static final int MY_REQUEST_WRITE_GALLERY   = 14;
    public static final int MY_REQUEST_GALLERY   = 15;

    private StorageReference mStorageRef;
    public File filen = null;
    private UserVO user;
    private Uri photo;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        this.etPassword = findViewById(R.id.etPassword);
        this.selectImage = findViewById(R.id.selectImage);
        this.cvBtnRegister =  findViewById(R.id.cvBtnRegister);
        this.dpBirthday =  findViewById(R.id.dpBirthday);
        this.etCellphone =  findViewById(R.id.etCellphone);
        this.etEmail = findViewById(R.id.etEmail);
        this.etSurname = findViewById(R.id.etSurname);
        this.etName = findViewById(R.id.etName);
        this.selectImage.setOnClickListener(this);
        cvBtnRegister.setOnClickListener(this);
        user = new UserVO();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // Invoca al método
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.selectImage:
                //Toast.makeText(this,"Image",Toast.LENGTH_SHORT).show();
                dialog();
                break;
            case R.id.cvBtnRegister:
                registerUser();
                break;
            case R.id.view:
                break;
        }

    }

    private void registerUser() {
       if(validateFields()){
           if(validateEmail()) {
               showCharging("Registrando\nUn momento por favor");
               if (photo != null) {
                   StorageReference photoRef = mStorageRef.child("images/" + user.getUserEmail() + ".jpg");
                   photoRef.putFile(photo)
                           .addOnSuccessListener(taskSnapshot -> {
                               Uri downloadUrl = taskSnapshot.getDownloadUrl();
                               registerUserInFireBaseAuth(downloadUrl);
                           })
                           .addOnFailureListener(exception -> {
                               hideCharging();
                               messageToast("Error subiendo imagen " + exception.getMessage());
                           });
               } else {
                   Uri uri = Uri.parse("https://pbs.twimg.com/profile_images/958172060206841856/xNhKM5Sn_400x400.png");
                   registerUserInFireBaseAuth(uri);
               }

           }

       }
       else
           messageToast("Complete los campos");
    }

    private boolean validateEmail() {
        String email = etEmail.getText().toString();
        if(!email.contains("@")) {
            messageToast("Ingrese un correo valido");
            return false;
        }
        if(email.split("@")[1].contains("gmail")) {
            messageToast("Inicie sesión por google");
            return false;
        }
        return  true;
    }

    private void registerUserInFireBaseAuth(Uri downloadUrl) {
        firebaseAuth.createUserWithEmailAndPassword(user.getUserEmail(), etPassword.getText().toString().trim())
                .addOnCompleteListener(this, task -> {
                    //checking if success
                    if(task.isSuccessful()){
                        setAttributesUser(downloadUrl);
                        finish();
                        //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    }else{
                        hideCharging();
                        messageToast("Error Registrando "+task.getException().getMessage());
                    }

                });
    }

    private void setAttributesUser(Uri downloadUrl) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null & downloadUrl != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(this.user.getUserName())
                    .setPhotoUri(downloadUrl)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(task -> {
                    hideCharging();
                    messageToast("Registro Completado");

            });
        }
    }

    private boolean validateFields() {
        String name = etName.getText().toString();
        String surName = etSurname.getText().toString();
        String email = etEmail.getText().toString();
        String cellphone = etCellphone.getText().toString();
        String password = etPassword.getText().toString();
        String dateBirth = checkDigit(dpBirthday.getMonth()+1)+"/"+checkDigit(dpBirthday
                .getDayOfMonth())+"/"+dpBirthday.getYear();
        if(!name.isEmpty() & !surName.isEmpty() & !email.isEmpty() & !cellphone.isEmpty()
                & !password.isEmpty() & !dateBirth.isEmpty()) {
            user.setUserBirthday(dateBirth);
            user.setGender("");
            user.setUserEmail(email);
            user.setUserName(name+" "+surName);
            return true;
        }
        return false;
    }
    public String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Source of the photo")
                .setNeutralButton("Cancel",this::onClick)
                .setPositiveButton("CAMERA",this::onClick)
                .setNegativeButton("GALLERY",this::onClick);
        builder.create();
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        try {
            switch (i) {
                case DialogInterface.BUTTON_POSITIVE:
                    checkPermissionCW();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    checkPermissionWG();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:

                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            Log.e("msg", "photo not get");
            return;
        }
        Fresco.getImagePipeline().clearCaches();
        switch (requestCode) {
            case CAPTURE_CAMERA:
                photo = Uri.parse("file:///" + filen);
                selectImage.setImageURI(photo);
                break;
            case MY_REQUEST_GALLERY:
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    filen = getFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(filen);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                    photo = Uri.parse("file:///" + filen);
                    selectImage.setImageURI(photo);//fresco library

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("", "Error while creating temp file", e);
                }
                break;

        }
    }
    public File getFile() {
        File fileDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (!fileDir.exists()){
            if (!fileDir.mkdirs()){
                return fileDir;
            }
        }
        return new File(fileDir.getPath() + File.separator + "temp.jpg");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CAMERA:
                checkPermissionCW();
                break;
            case MY_REQUEST_WRITE_CAMERA:
                checkPermissionCA();
                break;
            case MY_REQUEST_READ_GALLERY:
                checkPermissionWG();
                break;
            case MY_REQUEST_WRITE_GALLERY:
                checkPermissionRG();
                break;
        }
    }
    /**
     * PERMISO PARA LA CAMARA*/
    private void checkPermissionCA(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CAMERA);
        } else {
            catchPhoto();
        }
    }
    /**PERMISO PARA ESCRIBIR EN MEMORIA CON FOTO DEL TELEFONO*/
    private void checkPermissionCW(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_WRITE_CAMERA);
        } else {
            checkPermissionCA();
        }
    }
    private void catchPhoto() {
        filen = getFile();
        if(filen!=null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                //Uri photocUri = Uri.fromFile(filen);
                Uri photocUri = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        filen);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photocUri);
                startActivityForResult(intent, CAPTURE_CAMERA);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "please check your sdcard status", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * PERMISO PARA LEER GALERIA*/
    private void checkPermissionRG(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_READ_GALLERY);
        } else {
            checkPermissionWG();
        }
    }
    /**PERMISO PARA ESCRIBIR EN GALERIA*/
    private void checkPermissionWG(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_WRITE_GALLERY);
        } else {
            getPhotos();
        }
    }
    private void getPhotos() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, MY_REQUEST_GALLERY);
    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    @Override
    public void run() {

    }
}
