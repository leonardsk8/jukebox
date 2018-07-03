package ilioncorp.com.jukebox.model.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import ilioncorp.com.jukebox.model.dto.UserVO;
import ilioncorp.com.jukebox.model.generic.CRUD;
import ilioncorp.com.jukebox.utils.constantes.Constantes;

public class UserDAO extends CRUD implements ValueEventListener{



    UserVO userObj;
    FirebaseStorage storage;
    StorageReference storageReference;


    public UserDAO(Context context) {
        super();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        this.context=context;
        this.userObj = new UserVO();
    }
    public UserVO getUser() {
        return userObj;
    }
    public void updateUser(UserVO user){
        myRef.child("user").child(userID).setValue(user);
    }

    public void createUser(){
        userObj = new UserVO(user.getUid(),user.getDisplayName(),user.getEmail(),"","","",false);
        userObj.setToken(Constantes.userToken);
        userObj.setUserUrlImage(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        Constantes.userActive = userObj;
        myRef.child("user").child(userID).setValue(userObj);
        stopListener();
        if(!userObj.isUserProfileComplete())
            Constantes.openDialog=true;  //  openDialogProfile();
    }


    public void checkProfileUser(){
        listener = myRef.child("user").addValueEventListener(this);
    }

    private void ToastMessage(String s) {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    public void uploadImageUser(Uri photo){
        if(photo != null){
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child(UUID.randomUUID().toString());
            ref.putFile(photo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        boolean flag = false;
        Log.e("Entro","entro");
        for (DataSnapshot ds: dataSnapshot.getChildren()) {
            Log.e("SAME KEYS?",ds.getKey()+" ---- "+userID);
            if (ds.getKey().contains(userID)) {
                flag = true;
                Log.e("ALREADY","Usuario ya registrado");
                this.userObj = ds.getValue(UserVO.class);
                Constantes.userActive = this.userObj;
                if(!userObj.isUserProfileComplete())
                    Constantes.openDialog=true;//openDialogProfile();
                Log.e("USER",userObj.getUserUID());
                stopListener();
                break;
            }
        }
        if(!flag) {
                ToastMessage("Registrando usuario");
                createUser();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    public void stopListener(){
        if (myRef != null && listener != null) {
            myRef.child("user").removeEventListener(listener);
            Log.e("STOP","Listener stoped");
        }
    }
}
