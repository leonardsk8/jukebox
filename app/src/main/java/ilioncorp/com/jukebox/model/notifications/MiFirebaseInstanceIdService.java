package ilioncorp.com.jukebox.model.notifications;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ilioncorp.com.jukebox.model.dao.UserDAO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("TOKEN ASIGNADO",token);
        sendToken(token);
    }
    private void sendToken(String token) {
        Constantes.userToken = token;
    }
}
