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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.view.generic.GenericActivity;
import pl.droidsonroids.gif.GifImageView;



public class RegisterActivity extends GenericActivity implements Handler.Callback,View.OnClickListener,Runnable,DialogInterface.OnClickListener{

    private android.widget.EditText etName;
    private android.widget.EditText etSurname;
    private android.widget.EditText etEmail;
    private android.widget.EditText etCellphone;
    private android.widget.DatePicker dpBirthday;
    private android.support.v7.widget.CardView cvBtnRegister;
    private com.facebook.drawee.view.SimpleDraweeView selectImage;
    private com.facebook.drawee.view.SimpleDraweeView view;
    private GifImageView gifRegister;

    public static final int MY_REQUEST_CAMERA   = 10;
    public static final int MY_REQUEST_WRITE_CAMERA   = 11;
    public static final int CAPTURE_CAMERA   = 12;

    public static final int MY_REQUEST_READ_GALLERY   = 13;
    public static final int MY_REQUEST_WRITE_GALLERY   = 14;
    public static final int MY_REQUEST_GALLERY   = 15;

    public File filen = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        setContentView(R.layout.activity_register);
        this.gifRegister = findViewById(R.id.gifRegister);
        this.selectImage = findViewById(R.id.selectImage);
        this.view = findViewById(R.id.view);
        this.cvBtnRegister =  findViewById(R.id.cvBtnRegister);
        this.dpBirthday =  findViewById(R.id.dpBirthday);
        this.etCellphone =  findViewById(R.id.etCellphone);
        this.etEmail = findViewById(R.id.etEmail);
        this.etSurname = findViewById(R.id.etSurname);
        this.etName = findViewById(R.id.etName);
        this.selectImage.setOnClickListener(this);
        cvBtnRegister.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

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
                Toast.makeText(this,"Image",Toast.LENGTH_SHORT).show();
                dialog();
                break;
            case R.id.cvBtnRegister:
                Toast.makeText(this,"Registrado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.view:
                break;
        }

    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Source of the photo").setNeutralButton("Cancel",this)
        .setPositiveButton("CAMERA",this).setNegativeButton("GALLERY",this);
        builder.create();
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        try {
            switch (i) {
                case DialogInterface.BUTTON_POSITIVE:
                    catchPhoto();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    getPhotos();
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

                selectImage.setImageURI(Uri.parse("file:///" + filen));
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
                    selectImage.setImageURI(Uri.parse("file:///" + filen));//fresco library

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("", "Error while creating temp file", e);
                }
                break;

        }
    }
    public File getFile() throws IOException {

        File fileDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (!fileDir.exists()){
            if (!fileDir.mkdirs()){
                return fileDir;
            }
        }


        File mediaFile = new File(fileDir.getPath() + File.separator + "temp.jpg");
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
    private void checkPermissionCA(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CAMERA);
        } else {
            catchPhoto();
        }
    }
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

        try {
            filen = getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(filen!=null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                Uri photocUri = Uri.fromFile(filen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photocUri);
                startActivityForResult(intent, CAPTURE_CAMERA);
            } catch (ActivityNotFoundException e) {

            }
        } else {
            Toast.makeText(this, "please check your sdcard status", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkPermissionRG(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_READ_GALLERY);
        } else {
            checkPermissionWG();
        }
    }
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
